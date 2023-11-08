package org.example.interview;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocalCacheAndConcurrentTest {
    @Test
    public void test01() throws Exception {
        LocalCacheAndConcurrent.AServer aServer = new LocalCacheAndConcurrent().new AServer();
        aServer.getShopInfo(null);
    }
}