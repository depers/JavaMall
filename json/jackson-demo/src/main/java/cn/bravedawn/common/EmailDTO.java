package cn.bravedawn.common;

import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2025-09-25 18:20
 */

@Data
public class EmailDTO {
    private String sendEmail;
    private String receiverEmail;
    private String carbonCopyEmail;
    private String subject;
}
