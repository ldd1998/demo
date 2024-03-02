package org.example.provider;

import org.apache.dubbo.config.annotation.Service;
import org.example.service.DubboUserService;

@Service
public class DubboProviderUserService implements DubboUserService {
    @Override
    public String getUser(String userId) {
        return userId;
    }
}
