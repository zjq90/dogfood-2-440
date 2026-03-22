package com.idouban.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 字符编码过滤器
 * 统一设置请求和响应的字符编码为UTF-8
 * 
 * @author iDouban Team
 */
@WebFilter(filterName = "CharsetFilter", urlPatterns = "/*")
public class CharsetFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CharsetFilter.class);
    private static final String UTF8 = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("字符编码过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 设置请求编码
        req.setCharacterEncoding(UTF8);

        // 设置响应编码
        resp.setCharacterEncoding(UTF8);
        resp.setContentType("text/html;charset=" + UTF8);

        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        logger.info("字符编码过滤器销毁");
    }
}
