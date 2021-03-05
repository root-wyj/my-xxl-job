package com.xxl.job.executor.parser;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wuyingjie <13693653307@163.com>
 * Created on 2020-09-17
 */
public enum RequestMethod {

    GET,
    POST,
    ;

    public static RequestMethod get(String method) {
        for (RequestMethod item : values()) {
            if (StringUtils.equalsIgnoreCase(item.name(), method)) {
                return item;
            }
        }
        return null;
    }
}
