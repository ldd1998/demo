package org.example.service.task.monitor.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.DemoApplicationForTest;
import org.example.service.kafka.KafkaConsumerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DemoApplicationForTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MonitorServerMetricsTest {

    @Autowired
    MonitorServerMetrics monitorServerMetrics;
    @Autowired
    KafkaConsumerService kafkaConsumerService;
    @Test
    public void collectServerMetricsTest() throws JsonProcessingException {
        MonitorServerMetrics monitorServerMetrics = new MonitorServerMetrics();
        monitorServerMetrics.collectServerMetrics();
    }
    @Test
    public void kafkaReceive(){
        kafkaConsumerService.consumeMessage();
    }
}