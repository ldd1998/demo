package org.example.insertSpeed;


import org.example.service.export.ExportDataToFile;
import org.junit.jupiter.api.Test;

public class ExportDataToFileTest {
    @Test
    public void exportTest(){
        String path = "../";
        String fileName = "users.csv";
        ExportDataToFile.exportUserToTxt(10,1000000,path,fileName);
    }
}
