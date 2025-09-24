package cn.bravedawn.v1;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author : depers
 * @program : httpclient-demo
 * @description:
 * @date : Created in 2023/2/1 15:30
 */
public class GetHttpRequestExample {

    /**
     * 使用httpClient的get请求代码演示
     * 参考文章：https://www.javaguides.net/2018/10/apache-httpclient-get-http-request-example.html
     */

    private final static String base_url = "https://api.github.com/repos/depers/jasper-db/contents/";


    public static void main(String[] args) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(base_url);
        httpget.addHeader("Authorization", "Bearer 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");

        ResponseHandler< String > responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };

        String responseBody = httpclient.execute(httpget, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);

    }
}
