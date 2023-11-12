package org.example.service;

public class VersionCompare {
    /**
     *
        编写代码比较版本号大小
        版本号是由数字+分隔符"."组成，比大小时，请按从左到右的顺序依次比较它们的数字。比较数字大小时，只需比较数字大小， 忽略任何前导零后的整数值 。也就是说，1=001，如果某个版本号赎数字有缺失，视为0，也就是说5.1=5.1.0。
        样例：
        输入: version1 = "1.01", version2 = "1.1.0"
        输出：1.01 = 1.1.0
        输入：version1 = "7.5.2.4", version2 = "7.5.3"
        输出：7.5.2.4 < 7.5.3
        输入：version1 = "1.0.1", version2 = "1"
        输出：1.0.1 > 1
     * @param v1
     * @param v2
     * @return
     */
    public static String versionCompare(String v1,String v2){
        String[] v1Arr = v1.split("\\.");
        String[] v2Arr = v2.split("\\.");
        int maxLength = Math.max(v1Arr.length,v2Arr.length);
        for (int i = 0; i < maxLength; i++) {
            int v1Value = 0;
            int v2Value = 0;
            if(v1Arr.length > i){
                v1Value = Integer.parseInt(v1Arr[i]);
            }
            if(v2Arr.length > i){
                v2Value = Integer.parseInt(v2Arr[i]);
            }
            if(v1Value > v2Value){
                return v1 + ">" + v2;
            }else if (v1Value < v2Value){
                return v1 + "<" + v2;
            }
        }
        return v1 + "=" + v2;
    }

    public static void main(String[] args) {
        System.out.println(versionCompare("1.01","1.1.0"));
        System.out.println(versionCompare("7.5.2.4","7.5.3"));
        System.out.println(versionCompare("1.0.1","1"));
    }
}
