package org.example.service.export;

import cn.hutool.core.date.DateField;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;

/**
 * @author ldd
 * 导出数据到文件
 */
public class ExportDataToFile {
    public static void exportUserToTxt(int perCount,int countSize,String path,String fileName){
        FileWriter writer = new FileWriter(path+fileName);
        StringBuilder stringBuilder = new StringBuilder();
        for (int k = 0; k < perCount; k++) {
            for (int i = 0; i < countSize; i++) {
                String metaStr =getMataStr();
                stringBuilder.append(metaStr);
            }
            writer.append(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
    }

    private static String getMataStr() {
        String name = "ldd"+ RandomUtil.randomInt(0,100);
        int age = RandomUtil.randomInt(0,100);
        String time = RandomUtil.randomDate(new Date(), DateField.SECOND,0,10000000).toString();
        String metaStr =  "1\t"+name+"\t"+age+"\t"+time+"\t"+time+"\t\\N\n";
        return metaStr;
    }
}
