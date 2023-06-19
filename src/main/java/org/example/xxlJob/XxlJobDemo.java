package org.example.xxlJob;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class XxlJobDemo {
    int i = 0;
    @XxlJob("xxlJobDemo")
    public void test01(){
        XxlJobHelper.log("执行次数："+ i++);
        System.out.println("执行次数："+ i++);
    }
}
