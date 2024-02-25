package org.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author ：jerry
 * @date ：Created in 2022/11/7 09:52
 * @description：
 * @version: V1.1
 */
@ConfigurationProperties(prefix = "hbase")
public class HbaseProperties {
    private Map<String, String> config;
 
    public Map<String, String> getConfig() {
        return config;
    }
 
    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}