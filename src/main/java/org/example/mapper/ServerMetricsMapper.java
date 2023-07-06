package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.ServerMetrics;

/**
* @author ldd
* @description 针对表【server_metrics(服务器监控指标表)】的数据库操作Mapper
* @createDate 2023-07-06 19:41:48
* @Entity generator.entity.ServerMetrics
*/
@Mapper
public interface ServerMetricsMapper extends BaseMapper<ServerMetrics> {

}




