package org.example.elasticsearch;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.SneakyThrows;
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
        RestClient restClient = RestClient.builder(new HttpHost("192.168.1.107", 9200, "http")).build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 20, 1, TimeUnit.HOURS, new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(perCount * threadCount);
        for (int i = 0; i < threadCount; i++) {
            for (int k = 0; k < perCount; k++) {
                int a = i * perCount + k;
                threadPoolExecutor.execute(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        User user = new User();
                        user.setAge(1);
                        user.setName("ldd");
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                        String userJson = objectMapper.writeValueAsString(user);

                        Request request = new Request("POST", "/user/_doc/" + a);
                        request.setJsonEntity(userJson);
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
