package org.example.interview;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

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
            Map map = new HashMap();
            for (int i = 0; i < shopIds.size(); i++) {
                map.put(shopIds.get(i),"name"+shopIds.get(i));
            }
            return map;
        }

        /**
         * 已提供方法2、入参不大于10个
         */
        public Map<Integer,String > getShopAddress(List<Integer> shopIds){
            Map map = new HashMap();
            for (int i = 0; i < shopIds.size(); i++) {
                map.put(shopIds.get(i),"address"+shopIds.get(i));
            }
            return map;
        }
    }

    public class AServer{
        private ShopService shopService = new ShopService();
        // 缓存，这里使用的map做简化版的本地缓存，也可以使用Guava Cache替代，可以设置失效时间
        Map<Integer,ShopInfo> cache = new ConcurrentHashMap<>();
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

            // 对需要查询的id进行遍历，缓存中有的就从缓存中取并移除id
            Iterator<Integer> iterator = shopIds.iterator();
            while (iterator.hasNext()) {
                Integer id = iterator.next();
                // 如果缓存里存在就移除元素，并从缓存中获取
                if (cache.containsKey(id)) {
                    iterator.remove();
                    resultMap.put(id,cache.get(id));
                    System.out.println("缓存中获取到元素"+id);
                }
            }

            // 对剩余部分进行分割，以最大10个每份放到shopIdLists列表中
            List<List<Integer>> shopIdLists = new ArrayList<>();
            for (int i = 0; i < shopIds.size(); i += 10) {
                shopIdLists.add(shopIds.subList(i, Math.min(i + 10, shopIds.size())));
            }

            // 对shopIdLists进行多线程并发获取数据
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
    }

    /**
     * 计算商品的优惠标签
     * @param originalPrice 原价
     * @param finalPrice 到手价
     * @param historicalPriceList 历史到手价
     * @return 优惠标签
     */
    public static String computeProductPromoTag(BigDecimal originalPrice, BigDecimal finalPrice, List<BigDecimal> historicalPriceList){
        // 如果是历史最低，则输出“历史最低”
        // 如果是近三个月最低则输出“近三个月最低”
        // 如果有折扣，则输出“xx折扣”，保留一位小数，如果最后一位是0则不显示
        // 兜底输出“到手价”

        if (historicalPriceList.isEmpty()) {
            return "到手价";
        }

        BigDecimal minHistoricalPrice = historicalPriceList.get(0);
        BigDecimal minLastThreeMonthsPrice = historicalPriceList.get(historicalPriceList.size() - 1);
        boolean hasDiscount = false;

        for (int i = historicalPriceList.size() - 1; i >= 0; i--) {
            BigDecimal price = historicalPriceList.get(i);
            if (price.compareTo(minHistoricalPrice) < 0) {
                minHistoricalPrice = price;
            }

            if (price.compareTo(minLastThreeMonthsPrice) < 0 && historicalPriceList.size() - i < 120) {
                minLastThreeMonthsPrice = price;
            }
        }
        if(finalPrice.compareTo(minHistoricalPrice) < 0){
            return "历史最低价";
        }
        if(finalPrice.compareTo(minLastThreeMonthsPrice) < 0 ){
            return "近三个月最低";
        }
        if(finalPrice.compareTo(originalPrice) < 0){
            BigDecimal discount = originalPrice.subtract(finalPrice).divide(originalPrice, 1, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(10));
            String discountTag = discount.toString();
            if (discountTag.endsWith(".0")) {
                discountTag = discountTag.replace(".0", "");
            }
            return discountTag + "折";
        }
        return "到手价";
    }
    public static void main(String[] args) {
//        // 测试用的价格列表
//        List<BigDecimal> historicalPriceList = new ArrayList<>();
//        historicalPriceList.add(new BigDecimal("30.0"));
//        historicalPriceList.add(new BigDecimal("40.0"));
//        // 补充近三月数据
//        for (int i = 0; i < 120; i++) {
//            historicalPriceList.add(new BigDecimal("45.0"));
//        }
//        historicalPriceList.add(new BigDecimal("50.0"));
//
//        // 测试用例1: 历史最低价
//        BigDecimal originalPrice1 = new BigDecimal("100.0");
//        BigDecimal finalPrice1 = new BigDecimal("20.0");
//        System.out.println(computeProductPromoTag(originalPrice1, finalPrice1, historicalPriceList));
//
//        // 测试用例2: 近三个月最低价
//        BigDecimal originalPrice2 = new BigDecimal("100.0");
//        BigDecimal finalPrice2 = new BigDecimal("43.0");
//        System.out.println(computeProductPromoTag(originalPrice2, finalPrice2, historicalPriceList));
//
//        // 测试用例3: 折扣
//        BigDecimal originalPrice3 = new BigDecimal("100.0");
//        BigDecimal finalPrice3 = new BigDecimal("50.0");
//        System.out.println(computeProductPromoTag(originalPrice3, finalPrice3, historicalPriceList));
//
//        // 测试用例4: 到手价
//        BigDecimal originalPrice4 = new BigDecimal("100.0");
//        BigDecimal finalPrice4 = new BigDecimal("100.0");
//        System.out.println(computeProductPromoTag(originalPrice4, finalPrice4, historicalPriceList));
//

        AServer aServer = new LocalCacheAndConcurrent().new AServer();
        List<Integer> shopIds = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            shopIds.add(i);
        }
        try {
            Map<Integer, ShopInfo> shopInfoMap = aServer.getShopInfo(shopIds);
            for (Integer key : shopInfoMap.keySet()) {
                System.out.println("正常获取结果："+shopInfoMap.get(key).name+"，"+shopInfoMap.get(key).name);
            }
            // 再次调用测试缓存中是否能正常获取
            Map<Integer, ShopInfo> shopInfoMap2 = aServer.getShopInfo(shopIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
