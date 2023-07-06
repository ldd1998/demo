package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.ServerMetrics;
import org.example.mapper.ServerMetricsMapper;
import org.example.service.serverMetrics.ServerMetricsService;
import org.springframework.stereotype.Service;

/**
* @author ldd
* @description 针对表【server_metrics(服务器监控指标表)】的数据库操作Service实现
* @createDate 2023-07-06 19:41:48
*/
@Service
public class ServerMetricsServiceImpl extends ServiceImpl<ServerMetricsMapper, ServerMetrics> implements ServerMetricsService {

}




