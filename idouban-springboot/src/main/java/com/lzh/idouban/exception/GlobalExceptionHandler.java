package com.lzh.idouban.exception;

import com.lzh.idouban.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * @author 林泽鸿
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有异常
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常：", e);
        
        // 判断是API请求还是页面请求
        String header = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(header) || request.getRequestURI().startsWith("/api/")) {
            return Result.error("系统繁忙，请稍后重试");
        }
        
        // 页面请求返回错误页面
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMsg", e.getMessage());
        mav.setViewName("error");
        return mav;
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Object handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常：{}", e.getMessage());
        
        String header = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(header) || request.getRequestURI().startsWith("/api/")) {
            return Result.error(e.getMessage());
        }
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMsg", e.getMessage());
        mav.setViewName("error");
        return mav;
    }

}
