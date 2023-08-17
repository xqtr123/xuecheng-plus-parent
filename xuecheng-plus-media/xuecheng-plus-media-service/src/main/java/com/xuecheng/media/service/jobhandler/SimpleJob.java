package com.xuecheng.media.service.jobhandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleJob {

    @XxlJob("testjob")
    public void testJob(){
        log.debug("开始执行.......");
    }
}
