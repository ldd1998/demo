//package org.example.elasticsearch;
//
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.RestHighLevelClientBuilder;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//
//import java.io.IOException;
//
//public class ElasticsearchExample {
//
//    public static void main(String[] args) throws IOException {
//        // 创建连接到Elasticsearch的客户端
//        RestClient httpClient = RestClient.builder(
//                new HttpHost("192.168.1.107", 9200)
//        ).build();
//
//        // Create the HLRC
//        RestHighLevelClient hlrc = new RestHighLevelClientBuilder(httpClient)
//                .setApiCompatibilityMode(true)
//                .build();
//
//        CreateIndexRequest request = new CreateIndexRequest("user1");
//
////        request.
//
//    }
//}
