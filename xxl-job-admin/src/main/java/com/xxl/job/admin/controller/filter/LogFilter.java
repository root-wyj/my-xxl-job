package com.xxl.job.admin.controller.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wuyingjie <13693653307@163.com>
 * Created on 2020-09-15
 */
@Component
@Order(OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER - 104)
@Slf4j
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        // 警告:下面这两行千万不要换行
        filterChain.doFilter(wrappedRequest, response);
        String body = getBody(wrappedRequest);
        log.info("method: {}, path: {}, status: {}, userAgent: {}, query: {}, body: {}",
                request.getMethod(), request.getRequestURI(), response.getStatus(),
                request.getHeader("User-Agent"), request.getQueryString(), body);
    }

    private String getBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request,
                ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String payload;
                try {
                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    payload = "[unknown]";
                }
                return payload;
            }
        }
        return null;
    }
}
