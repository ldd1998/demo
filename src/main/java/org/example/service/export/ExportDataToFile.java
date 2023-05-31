package org.example.service.export;

import cn.hutool.core.io.file.FileWriter;

/**
 * @author ldd
 * 导出数据到文件
 */
public class ExportDataToFile {
    public static void exportUserToTxt(String metaStr,int perCount,int countSize,String path,String fileName){
        FileWriter writer = new FileWriter(path+fileName);
        StringBuilder stringBuilder = new StringBuilder();
        for (int k = 0; k < perCount; k++) {
            for (int i = 0; i < countSize; i++) {
                stringBuilder.append(metaStr);
            }
            writer.append(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
    }
}
