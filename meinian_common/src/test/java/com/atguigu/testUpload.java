package com.atguigu;

import com.atguigu.utils.QiniuUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author lijian
 * @create 2021-01-06 20:44
 */
public class testUpload {
    @Test
    public void test() throws IOException {
        //QiniuUtils.upload2Qiniu("D:\\temp\\90\\jjy94.jpg","jjy94.jpg");

        //QiniuUtils.deleteFileFromQiniu("jjy94.jpg");

        File file = new File("C:\\Users\\li\\Desktop\\上传图片\\111.png");
        //init array with file length
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        //通过封装好的工具来进行文件的上传，通过字节流方式调用工具类传值
        QiniuUtils.upload2Qiniu(bytesArray,"111.png");
    }

}
