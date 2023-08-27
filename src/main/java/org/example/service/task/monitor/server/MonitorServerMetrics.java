package org.example.service.task.monitor.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.ServerMetrics;
import org.example.mapper.ServerMetricsMapper;
import org.example.service.kafka.KafkaProducerService;
import org.example.util.ObjectMapperSnakeCaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 定时监控服务器指标程序
 */
@Service
@Slf4j
public class MonitorServerMetrics {
    private final HardwareAbstractionLayer hardware;
    SystemInfo systemInfo;
    String topic = "serverMetricsTopic";
    @Autowired
    ServerMetricsMapper serverMetricsMapper;
    @Autowired
    KafkaProducerService kafkaProducerService;
    @Autowired
    ObjectMapperSnakeCaseUtil objectMapperSnakeCaseUtil;
    public MonitorServerMetrics() {
        this.systemInfo = new SystemInfo();
        this.hardware = systemInfo.getHardware();
    }

//    @Scheduled(fixedRate = 1000)
    public void collectServerMetrics() throws JsonProcessingException {
        oshi.hardware.CentralProcessor.TickType[] tickTypes = oshi.hardware.CentralProcessor.TickType.values();
        long[] oldTicks = new long[tickTypes.length];
        CentralProcessor processor = hardware.getProcessor();
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(oldTicks) * 100;

        GlobalMemory memory = hardware.getMemory();
        long usedMemory = memory.getTotal() - memory.getAvailable();
        double memoryUsage = (double) usedMemory / memory.getTotal() * 100;

        long totalDiskSpace = 0;
        long usedDiskSpace = 0;
        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        for (OSFileStore fileStore : fileSystem.getFileStores()) {
            totalDiskSpace += fileStore.getTotalSpace();
            usedDiskSpace += fileStore.getTotalSpace() - fileStore.getUsableSpace();
        }
        double storageUsage = (double) usedDiskSpace / totalDiskSpace * 100;

        ServerMetrics serverMetrics = new ServerMetrics();
        serverMetrics.setCreateTime(LocalDateTime.now());
        serverMetrics.setCpuUsage(new BigDecimal(cpuLoad).setScale(2, BigDecimal.ROUND_HALF_UP));
        serverMetrics.setMemoryUsage(new BigDecimal(memoryUsage).setScale(2, BigDecimal.ROUND_HALF_UP));
        serverMetrics.setNetworkBandwidthUsage(new BigDecimal(-1));
        serverMetrics.setStorageUsage(new BigDecimal(storageUsage).setScale(2, BigDecimal.ROUND_HALF_UP));
        serverMetrics.setServerId("192.168.1.107");
        serverMetricsMapper.insert(serverMetrics);
        kafkaProducerService.sendMessage(topic, objectMapperSnakeCaseUtil.toJSONString(serverMetrics));
    }

}
