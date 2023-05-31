package org.example.insertSpeed;


import org.example.service.export.ExportDataToFile;
import org.junit.jupiter.api.Test;

public class ExportDataToFileTest {
    @Test
    public void exportTest(){
        String metaStr = "1017491,\tldd\t,20,\t2023-05-29 21:12:55\t,\t2023-05-29 21:12:55\t,\\N\n";
//        String path = "C:\\Users\\ldd\\Desktop\\";
        String path = "../";
        String name = "users.csv";
        ExportDataToFile.exportUserToTxt(metaStr,10,10000000,path,name);
    }
}
