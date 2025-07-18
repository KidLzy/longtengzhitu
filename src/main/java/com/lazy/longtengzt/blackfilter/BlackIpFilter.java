package com.lazy.longtengzt.blackfilter;


import com.lazy.longtengzt.utils.NetUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 全局 IP 黑名单过滤请求拦截器
 * @author Lazy
 * @create 2024-10-22 1:28
 */
// WebFilter 拦截 HTTP 请求，并可以根据逻辑决定是否继续执行请求。
@WebFilter(urlPatterns = "/*", filterName = "blackIpFilter")
public class BlackIpFilter implements Filter{

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String ipAddress = NetUtils.getIpAddress(((HttpServletRequest) servletRequest));
        if(BlackIpUtils.isBlackIp(ipAddress)){
            servletResponse.setContentType("text/json;charset=UTF-8");
            servletResponse.getWriter().write("{\"errorCode\":\"-1\",\"errorMsg\":\"黑名单IP，禁止访问\"}");
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
