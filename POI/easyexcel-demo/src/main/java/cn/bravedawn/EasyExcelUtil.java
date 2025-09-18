package cn.bravedawn;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-09-16 09:03
 */

@Slf4j
public class EasyExcelUtil {


    /**
     * 写入List到Excel中
     *
     * @param outputStream 输出流，可以是HttpServletResponse的输出流，也可以是本地文件的输出流
     * @param headClass    表头定义的类
     * @param sheetName    sheet名称
     * @param resultList   写入list数据
     * @param <T>
     */
    public static <T> void exportDataToExcel(OutputStream outputStream, Class<T> headClass, String sheetName, List<T> resultList) {
        EasyExcel.write(outputStream, headClass).sheet(sheetName).doWrite(resultList);
    }


    /**
     * 通过模板写入excel
     *
     * @param outputStream        写入文件输出流
     * @param templateInputStream 模版输入流
     * @param headClass           表头定义的类
     * @param sheetName           sheet名称
     * @param resultList          写入list数据
     * @param headRowIndex        表头行数
     * @param <T>
     */
    public static <T> void exportDataToExcelByTemplate(OutputStream outputStream, InputStream templateInputStream,
                                                       Class<T> headClass, String sheetName,
                                                       List<T> resultList, int headRowIndex) {
        try (ExcelWriter excelWriter = EasyExcel.write(outputStream)
                .withTemplate(templateInputStream)
                .needHead(false)
                .relativeHeadRowIndex(headRowIndex)
                .build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).head(headClass).build();
            excelWriter.write(resultList, writeSheet);
        }
    }


    /**
     * 下载文件
     *
     * @param request          请求
     * @param response         响应
     * @param inputStream         被下载的文件
     * @param downloadFileName 文件名
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response,
                                    InputStream inputStream, String downloadFileName) {

        try (BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
            // 将中文按照url百分号编码，url编码会将空格变为+号，这里将其替换为合法的空格表示
            String fileName = URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            response.setContentType(request.getSession().getServletContext().getMimeType(fileName));
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error("导出数据出现异常", e);
            throw new IllegalStateException("导出数据异常");
        }
    }


    /**
     * 读取excel
     * @param file 文件
     * @param cls 表头类class
     * @param sheetName sheet名称
     * @param headRowIndex 表头行数
     * @return
     * @param <T>
     */
    public static <T> List<T> readExcel(File file, Class<T> cls, String sheetName, int headRowIndex) {
        List<T> list = new ArrayList<>();
        EasyExcel.read(file, cls, new AnalysisEventListener<T>() {
                    @Override
                    public void invoke(T data, AnalysisContext context) {
                        list.add(data);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        log.info("excel数据已读取完成, file={}", file.getName());
                    }
                })
                .headRowNumber(headRowIndex)
                .sheet(sheetName)
                .doRead();
        return list;
    }


}
