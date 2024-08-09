package com.sparta.msa_exam.product.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CustomHeaderInterceptor implements HandlerInterceptor {

    @Value("${server.port}")
    private String serverPort;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        response.addHeader("Server-Port", serverPort);
        System.out.println("Server-Port added: " + serverPort);  // 로그 추가
    }
}
