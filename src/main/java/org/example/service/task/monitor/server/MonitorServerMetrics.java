package org.example.service.task.monitor.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.example.entity.ServerMetrics;
import org.example.mapper.ServerMetricsMapper;
import org.example.service.elasticsearch.ElasticsearchService;
import org.example.service.kafka.KafkaProducerService;
import org.example.util.ObjectMapperSnakeCaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

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

    @Scheduled(fixedRate = 1000)
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
        log.info("监控服务器指标："+serverMetrics);
    }
    /**
     * 消费kafka消息，放到elastic
     */

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @KafkaListener(topics = "serverMetricsTopic", groupId = "serverMetricsGroup")
    public void monitorToEsConsumer(ConsumerRecord<String, String> record) {
//        log.info("接收服务器指标信息: " + record.value());
    }


    @Autowired
    private KafkaConsumer<String, String> consumer;
    @Autowired
    ElasticsearchService elasticsearchService;
    public void consumeMessage() {
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            consumer.poll(1000);
            if (!consumer.assignment().isEmpty()) break;
        }
        consumer.seek(new TopicPartition(topic,0),0);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : records) {
                elasticsearchService.insertDataToEs("server_metrics",record.value());
                System.out.printf("自定义接收服务器指标信息offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }

            // 执行你的逻辑...

            // 手动提交偏移量
//            consumer.commitSync();
        }
    }
}
