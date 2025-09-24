package cn.bravedawn.v2;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @Author : depers
 * @Date : Created in 2025-09-24 14:52
 */
public class CustomResponseInterceptor implements HttpResponseInterceptor {


    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {

    }
}
