package org.example.service.task.monitor.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DemoApplicationForTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MonitorServerMetricsTest {

    @Test
    public void collectServerMetricsTest() throws JsonProcessingException {
        MonitorServerMetrics monitorServerMetrics = new MonitorServerMetrics();
        monitorServerMetrics.collectServerMetrics();
    }
}