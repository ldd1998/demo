package org.example.consumer.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.consumer.DubboConsumerService;
import org.springframework.stereotype.Service;

/**
 * 这里是spring的service
 */
@Service
public class DubboConsumerServiceImpl implements DubboConsumerService {
//    @DubboReference
//    DubboProviderService dubboProviderService;

    @Override
    public void consumerFun() {
//        String s = dubboProviderService.providerFun();
//        System.out.println(s);
    }
}
