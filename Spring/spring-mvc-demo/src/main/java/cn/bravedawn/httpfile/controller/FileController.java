package cn.bravedawn.httpfile.controller;

import cn.bravedawn.httpfile.dto.DownloadFileRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author : depers
 * @Date : Created in 2025-09-10 15:29
 */

@Controller
public class FileController {



    @GetMapping("/downloadFile")
    public ResponseEntity<MultiValueMap<String, Object>> downloadFile(DownloadFileRequestDTO requestDTO) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        

        return new ResponseEntity<>()
    }
}
