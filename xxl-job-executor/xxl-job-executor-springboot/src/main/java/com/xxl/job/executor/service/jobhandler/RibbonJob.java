package com.xxl.job.executor.service.jobhandler;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.parser.RequestMethod;
import com.xxl.job.executor.parser.SimpleUriParser;
import com.xxl.job.executor.parser.SimpleUriParser.SimpleRequest;

/**
 * @author wuyingjie <13693653307@163.com>
 * Created on 2020-09-01
 */
@Component
public class RibbonJob {

    private static Logger logger = LoggerFactory.getLogger(RibbonJob.class);

    @Autowired
    private RestTemplate restTemplate;

    @XxlJob("ribbonJobHandler")
    public ReturnT<String> ribbonJobHandler(String params) throws Exception {
        logger.info("ribbonJobHandler. params:{}", params);
        // param parse
        if (StringUtils.isBlank(params)) {
            XxlJobLogger.log("param["+ params +"] invalid.");
            return ReturnT.FAIL;
        }
        SimpleRequest request;
        if (params.startsWith("http")) {
            request = new SimpleRequest();
            request.setUrl(params);
            request.setMethod(RequestMethod.GET);
        } else {
            request = SimpleUriParser.parse(params);
        }

        if (request == null || StringUtils.isBlank(request.getUrl())
                || request.getMethod() == null) {
            XxlJobLogger.log("request ["+ request +"] invalid.");
            return ReturnT.FAIL;
        }

        String result = null;
        if (request.getMethod() == RequestMethod.GET) {
            result = restTemplate.getForObject(request.getUrl(), String.class);
        } else if (request.getMethod() == RequestMethod.POST) {
            result = restTemplate.postForObject(request.getUrl(), request.getBody(), String.class);
        }
        XxlJobLogger.log("ribbonHandler ------ request:" + request + "------- result:" + result);
        return ReturnT.SUCCESS;
    }


}
