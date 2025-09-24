package cn.bravedawn.v2;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @Author : depers
 * @Date : Created in 2025-09-24 14:53
 */
public class CustomRequestInterceptor implements HttpRequestInterceptor {


    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {

    }
}
