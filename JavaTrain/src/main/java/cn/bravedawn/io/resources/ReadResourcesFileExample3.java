package cn.bravedawn.io.resources;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;


/**
 * Spring中读取resources目录下的文件内容
 */
public class ReadResourcesFileExample3 {

    public static void main(String[] args) {
        // 使用ResourceLoader
        ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();
        Resource resource2 = resourceLoader.getResource("classpath:/doc/test.txt");
        try {
            String content2 = new String(FileCopyUtils.copyToByteArray(resource2.getInputStream()), "UTF-8");
            System.out.println(content2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
