package cn.bravedawn.io.resources;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;


/**
 * Spring中读取resources目录下的文件内容
 */
public class ReadResourcesFileExample2 {

    public static void main(String[] args) {
        // 使用ClassPathResource直接读取
        Resource resource = new ClassPathResource("/doc/test.txt");
        try {
            String content = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), "UTF-8");
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
