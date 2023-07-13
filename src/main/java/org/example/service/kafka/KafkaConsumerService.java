package org.example.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.example.service.elasticsearch.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Service
public class KafkaConsumerService {
    String topic = "serverMetricsTopic";
    /**
     * 消费kafka消息，放到elastic
     */
    @KafkaListener(topics = "serverMetricsTopic", groupId = "myGroup")
    public void monitorToEsConsumer(ConsumerRecord<String, String> record) {
        log.info("@KafkaListener接收: " + record.value());
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
                log.info("自定义接收kafka："+record.value());
            }

            // 执行你的逻辑...

            // 手动提交偏移量
//            consumer.commitSync();
        }
    }
}
