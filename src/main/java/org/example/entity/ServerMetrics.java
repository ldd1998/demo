package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务器监控指标表
 * @TableName server_metrics
 */
@TableName(value ="server_metrics")
@Data
public class ServerMetrics implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所监控的服务器的ID
     */
    @TableField(value = "server_id")
    private String serverId;

    /**
     * CPU使用率，范围从0到100
     */
    @TableField(value = "cpu_usage")
    private BigDecimal cpuUsage;

    /**
     * 内存使用率，范围从0到100
     */
    @TableField(value = "memory_usage")
    private BigDecimal memoryUsage;

    /**
     * 存储使用率，范围从0到100
     */
    @TableField(value = "storage_usage")
    private BigDecimal storageUsage;

    /**
     * 网络带宽使用率，范围从0到100
     */
    @TableField(value = "network_bandwidth_usage")
    private BigDecimal networkBandwidthUsage;

    /**
     * 记录时间戳
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
}