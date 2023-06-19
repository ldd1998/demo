package org.example.redis;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.example.config.RedisTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisTestConfig.class)
public class RedisUseTest {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * String类型
     */
    @Test
    public void stringOps(){
        redisTemplate.opsForValue().set("StringTest","1");
    }

    /**
     * List1类型
     */
    @Test
    public void lsitOps(){
        redisTemplate.opsForList().leftPush("listKey","1");
        redisTemplate.opsForList().leftPush("listKey","2");
    }
    /**
     * Set类型
     */
    @Test
    public void setOps(){
        redisTemplate.opsForSet().add("setKey","1");
        redisTemplate.opsForSet().add("setKey","2");
    }
    /**
     * ZSet类型
     */
    @Test
    public void zSetOps(){
        redisTemplate.opsForZSet().add("zSetKey","1",1);
        redisTemplate.opsForZSet().add("zSetKey","2",2);
    }
    /**
     * hash类型
     */
    @Test
    public void hashOps(){
        redisTemplate.opsForHash().put("hashTypeKey1","hashKey1","1");
        redisTemplate.opsForHash().put("hashTypeKey1","hashKey2","2");
        Object o1 = redisTemplate.opsForHash().get("hashTypeKey1", "hashKey1");
        Object o2 = redisTemplate.opsForHash().get("hashTypeKey1", "hashKey2");
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

        System.out.println(o1);
        System.out.println(o2);

    }
    /**
     * 布隆过滤器，解决缓存穿透
     */
    @Test
    public void bitMapTest(){
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.unencodedCharsFunnel(), 1000000, 0.001);
        bloomFilter.put("test:1000");
        boolean mightContain1 = bloomFilter.mightContain("test:1000");
        boolean mightContain2 = bloomFilter.mightContain("test:1001");
        System.out.println(mightContain1+"，可能存在");
        System.out.println(mightContain2+"，一定不存在");
    }

}
