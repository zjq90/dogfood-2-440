package com.lzh.idouban.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 * 拦截未登录用户的请求
 * @author 林泽鸿
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");

        if (userId == null) {
            log.warn("未登录访问被拦截：uri={}", request.getRequestURI());

            // 判断是AJAX请求还是普通请求
            String header = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(header)) {
                // AJAX请求返回401
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"未登录\",\"data\":null}");
            } else {
                // 普通请求重定向到登录页
                response.sendRedirect("/login");
            }
            return false;
        }

        return true;
    }

}
