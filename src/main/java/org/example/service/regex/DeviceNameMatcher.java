package org.example.service.regex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DeviceNameMatcher {
    public static void main(String[] args) {

        // 使用正则表达式替换第二个"."前面的字符为空
        String input = "a.b.c线";
        String r = ".*\\.|线";
//        String r = "\\.(.*?)线";
        // 使用正则表达式替换"."前面的字符为空字符串
        String result = input.replaceAll(r, "");

        System.out.println(result);

        // 创建一个简单的HashMap
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("A", 1);
        hashMap.put("B", 2);
        hashMap.put("C", 3);

        // 遍历并移除键
        Iterator<Map.Entry<String, Integer>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String key = entry.getKey();
            int value = entry.getValue();

            // 根据条件判断是否移除键
            if (shouldRemoveKey(key, value)) {
                iterator.remove();
            }
        }

        // 打印移除键后的HashMap
        System.out.println("HashMap after key removal: " + hashMap);
    }

    private static boolean shouldRemoveKey(String key, int value) {
        // 根据特定条件判断是否移除键的逻辑
        // 这里只是一个示例，具体情况根据实际需求修改
        return value % 2 == 0; // 移除偶数值对应的键
    }
}
