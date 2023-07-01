package org.example.elasticsearch;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ldd
 * elasticsearch操作示例
 */
@Service
public class ElasticsearchService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RestClient restClient;

    /**
     * 执行sql，x-pack，收费，暂时放弃
     */
    @DS("es")
    public void selectListEs() {
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        System.out.println(users);
    }

    public void insertUserThread(int threadCount, int perCount) throws InterruptedException, IOException {
        RestClient restClient = RestClient.builder(
                new HttpHost("192.168.1.107", 9200, "http")).build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 20, 1, TimeUnit.HOURS, new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(perCount * threadCount);
        for (int i = 0; i < threadCount; i++) {
            for (int k = 0; k < perCount; k++) {
                int a = i * perCount + k;
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Request request = new Request(
                                "POST",
                                "/user/_doc/" + a);
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
                        System.out.println(responseBody + "," + a);
                        countDownLatch.countDown();
                    }
                });
            }
        }
        countDownLatch.await();
        restClient.close();
    }
}
