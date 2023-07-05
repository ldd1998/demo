package org.example.elasticsearch;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.xcontent.XContentType;
import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = DemoApplicationForTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class ElasticsearchServiceTest {
    @Autowired
    ElasticsearchService elasticsearchService;

    /**
     * 只有30天有效期，暂时不破解了,也不测试
     * @throws IOException
     */
//    @Test
    public void selectListEs() throws IOException {
        elasticsearchService.selectListEs();
    }

    /**
     * Java High Level REST Client 连接测试,只有7.x版本,因此不做测试了
     * @throws IOException
     */
//    @Test
    public void test02() throws IOException {
        // 创建客户端连接
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.1.107", 9200, "http")));

        IndexRequest request = new IndexRequest("posts");
        request.id("1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        client.close();
    }

    /**
     * elasticsearch执行sql
     * @throws IOException
     */
    @Test
    public void elsticsearchPost() throws IOException {
        RestClient restClient = RestClient.builder(
                new HttpHost("192.168.1.107", 9200, "http")).build();

        Request request = new Request(
                "POST",
                "/_sql");

        // 相当于?name=zhangsan2
//        request.addParameter("name", "zhangsan2");
        // 设置body请求体
        request.setJsonEntity("{\n" +
                "  \"query\": \"SELECT * FROM user WHERE age = 30\"\n" +
                "}");
        Response response = restClient.performRequest(request);
        RequestLine requestLine = response.getRequestLine();
        HttpHost host = response.getHost();
        int statusCode = response.getStatusLine().getStatusCode();
        Header[] headers = response.getHeaders();
        String responseBody = EntityUtils.toString(response.getEntity());
        restClient.close();
    }
    /**
     * elasticSearch性能测试
     * 325/s，单线程并不是很快
     * cpu占用10%不到
     */
    @Test
    public void esInsertTest() throws IOException {
        RestClient restClient = RestClient.builder(
                new HttpHost("192.168.1.107", 9200, "http")).build();
        for (int i = 0; i < 10000; i++) {
            Request request = new Request(
                    "POST",
                    "/user/_doc/"+i);
            request.setJsonEntity("{\n" +
                    "  \"id\":1,\n" +
                    " \"name\":\"zhangsan\",\n" +
                    " \"age\":30,\n" +
                    " \"create_time\":\"2023-01-01\",\n" +
                    " \"update_time\":\"2023-01-01\",\n" +
                    " \"name_test1\":\"1\"\n" +
                    "}");
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody+","+i);
        }
        restClient.close();
    }

    /**
     * elasticSearch多线程性能测试
     * 3250/s，多线程感觉一般，20个线程，40个线程差不多应该请求速度不够，不知道是不是花在了线程切换上
     * 服务器cpu占用15%
     */
    @Test
    public void esInsertThreadTest() throws IOException, InterruptedException {
        RestClient restClient = RestClient.builder(
                new HttpHost("192.168.1.107", 9200, "http")).build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20,20,1, TimeUnit.HOURS,new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            int a= i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    Request request = new Request(
                            "POST",
                            "/user/_doc/"+a);
                    request.setJsonEntity("{\n" +
                            "  \"id\":1,\n" +
                            " \"name\":\"zhangsan\",\n" +
                            " \"age\":30,\n" +
                            " \"create_time\":\"2023-01-01\",\n" +
                            " \"update_time\":\"2023-01-01\",\n" +
                            " \"name_test1\":\"1\"\n" +
                            "}");
                    Response response = null;
                    try {
                        response = restClient.performRequest(request);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String responseBody = null;
                    try {
                        responseBody = EntityUtils.toString(response.getEntity());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(responseBody+","+a);
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        restClient.close();
    }

    /**
     * 采用不要太多线程切换的情况试一下
     * 2,777/s，20线程，效果不好
     * 内网带宽发送接收都在10Mbps/s左右应该没有达到上限呀
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void insertUserThreadTest() throws IOException, InterruptedException {
        elasticsearchService.insertUserThread(40,500);
    }

    /**
     * 5秒20W条，4W/s网络达到了100Mbps/s，应该是达到了瓶颈
     * 部署到同一台机器上，6.6W/s，此时cpu达到了瓶颈
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void insertUserThreadBlukTest() throws IOException, InterruptedException {
        elasticsearchService.insertUserThreadBluk(10,10);
    }
}