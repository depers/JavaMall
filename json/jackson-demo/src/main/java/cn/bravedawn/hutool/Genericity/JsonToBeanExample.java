package cn.bravedawn.hutool.Genericity;

import cn.bravedawn.common.CommonRespDTO;
import cn.bravedawn.common.EmailDTO;
import cn.bravedawn.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @Author : depers
 * @Date : Created in 2025-09-25 17:23
 */
public class JsonToBeanExample {


    public static void main(String[] args) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setSendEmail("dev_fengxiao@163.com");
        emailDTO.setReceiverEmail("123@qq.com");
        emailDTO.setSubject("邮件");
        emailDTO.setCarbonCopyEmail("123@163.com");

        CommonRespDTO<EmailDTO> commonRespDTO = new CommonRespDTO<>();
        commonRespDTO.setCode("0000");
        commonRespDTO.setMsg("请求成功");
        commonRespDTO.setData(emailDTO);

        String jsonStr = JsonUtil.toJsonStr(commonRespDTO);
        System.out.println(jsonStr);


        CommonRespDTO<EmailDTO> commonRespDTO1 = JsonUtil.parseReference(jsonStr, new TypeReference<CommonRespDTO<EmailDTO>>() {});
        System.out.println(commonRespDTO1);
    }
}
