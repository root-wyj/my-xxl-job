package com.xxl.job.executor.parser;

import org.apache.commons.lang.StringUtils;

import lombok.Data;
import lombok.Getter;

/**
 * @author wuyingjie <13693653307@163.com>
 * Created on 2020-09-17
 */
public class SimpleUriParser {

    public static SimpleRequest parse(String params) {
        if (StringUtils.isBlank(params)) {
            return null;
        }

        SimpleRequest request = new SimpleRequest();
        String[] httpParams = params.split("\n");
        for (String httpParam: httpParams) {
            if (httpParam.startsWith("url:")) {
                request.setUrl(httpParam.substring(httpParam.indexOf("url:") + 4).trim());
            }
            if (httpParam.startsWith("method:")) {
                request.setMethod(RequestMethod.get(httpParam.substring(httpParam.indexOf("method:") + 7).trim().toUpperCase()));
            }
            if (httpParam.startsWith("data:")) {
                request.setBody(httpParam.substring(httpParam.indexOf("data:") + 5).trim());
            }
        }
        return request;
    }

    @Data
    public static class SimpleRequest {
        private String url;
        private RequestMethod method;
        private String body;
    }

}
