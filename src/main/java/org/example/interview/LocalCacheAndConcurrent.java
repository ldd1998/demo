package org.example.interview;

import io.swagger.models.auth.In;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.K;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class LocalCacheAndConcurrent {
    // 使用多线程进行异步调用
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,10,
            10, TimeUnit.HOURS,new LinkedBlockingDeque<>());
    public class ShopInfo{
        private Integer id;
        private String name;
        private String address;
    }
    public class ShopService{
        /**
         * 已提供方法1、入参不大于10个
         */
        public Map<Integer,String> getShopName(List<Integer> shopIds){
            return Collections.emptyMap();
        }

        /**
         * 已提供方法2、入参不大于10个
         */
        public Map<Integer,String > getShopAddress(List<Integer> shopIds){
            return Collections.emptyMap();
        }
    }

    public class AServer{
        private ShopService shopService = new ShopService();

        /**
         *  根据商户id批量查询商户信息，入参shopIds不大于100个
         *  要求：
         *  1、并行调用shopService提供的getShopName和getShopAddress方法获取商户名称和地址，构造shopInfo
         *  2、从shopService中获取数据时需要满足shopService入参shopIds不大于10个要求即分批调用
         *  3、实现本地缓存，优先从缓存中获取数据，如果本地缓存中没有数据，再从ShopService获取数据
         */
        public Map<Integer,ShopInfo> getShopInfo(List<Integer> shopIds) throws Exception {
            // 异常参数判断
            if(CollectionUtils.isEmpty(shopIds)){
                throw new IllegalArgumentException("入参不可为null");
            }
            if(shopIds.size()>100){
                throw new IllegalArgumentException("入参大小大于100");
            }

            // 最后返回结果集，保证多线程并发安全
            Map<Integer,ShopInfo> resultMap = new ConcurrentHashMap<>();

            // 缓存，这里使用的map做简化版的本地缓存，也可以使用Guava Cache替代，可以设置失效时间
            Map<Integer,ShopInfo> cache = new ConcurrentHashMap<>();

            // 对需要查询的id进行遍历，缓存中有的就从缓存中取并移除id
            Iterator<Integer> iterator = shopIds.iterator();
            while (iterator.hasNext()) {
                Integer id = iterator.next();
                // 如果缓存里存在就移除元素，并从缓存中获取
                if (cache.containsKey(id)) {
                    iterator.remove();
                    resultMap.put(id,cache.get(id));
                }
            }

            // 对剩余部分进行分割，以最大10个每份放到shopIdLists列表中
            List<List<Integer>> shopIdLists = new ArrayList<>();
            for (int i = 0; i < shopIds.size(); i += 10) {
                shopIdLists.add(shopIds.subList(i, Math.min(i + 10, shopIds.size())));
            }


            CountDownLatch countDownLatch = new CountDownLatch(shopIdLists.size());

            for (int i = 0; i < shopIdLists.size(); i++) {
                int k = i;
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Integer> shopIdsSplit = shopIdLists.get(k);
                        Map<Integer, String> shopNameMap = shopService.getShopName(shopIdsSplit);
                        Map<Integer, String> addressMap = shopService.getShopAddress(shopIdsSplit);

                        for (Integer id : shopIdsSplit) {
                            ShopInfo shopInfo = new ShopInfo();
                            shopInfo.id = id;
                            shopInfo.name = shopNameMap.get(id);
                            shopInfo.address = addressMap.get(id);
                            resultMap.put(id,shopInfo);
                            // 放入缓存
                            cache.put(id,shopInfo);
                            countDownLatch.countDown();
                        }
                    }
                });
            }
            countDownLatch.await();
            return resultMap;
        }

        public<K,V> V get(K key) {
            return null;
        }
    }

    // 使用redis做基础缓存
    public class Cache<K,V>{
        private RedisTemplate<K, V> redisTemplate;
        AServer aServer = new AServer();
        public V get(K key){
            V value = redisTemplate.opsForValue().get(key);
            // 该键已过期，返回上次结果
            if(value == null){
                return getLastValue(key);
            }
            // 读后异步更新
            asyncUpdate(key);
            return value;
        }

        private V getLastValue(K key) {
            V value = redisTemplate.opsForValue().get("lastKey:"+key);
            asyncUpdate(key);
            return value;
        }

        public void asyncUpdate(K key){
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    // 异步读后更新，和过期后更新
                    V getValue = aServer.get(key);
                    redisTemplate.opsForValue().set(key,getValue);
                    // 更新上次值
                    redisTemplate.opsForValue().set((K) ("lastKey:"+key),getValue);
                }
            });

        }
        public V update(K key,V value){
            redisTemplate.opsForValue().set(key,value);;
            return value;
        }
    }
    public static void main(String[] args) {
        AServer aServer = new LocalCacheAndConcurrent().new AServer();

        List<Integer> shopIds = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            shopIds.add(i);
        }

        try {
            Map<Integer, ShopInfo> shopInfoMap = aServer.getShopInfo(shopIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
