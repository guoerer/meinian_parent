package com.atguigu;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**POI :java用于处理office的工具类
 * Excel读取和写入数据
 * @author lijian
 * @create 2021-01-11 9:25
 */
public class TestPoI {
    /**
     *从Excel中读取数据
     */
    @Test
    public void test() throws IOException {
        //创建Excel文件的工作浦
        Workbook   workbook = new XSSFWorkbook("C:\\Users\\li\\Desktop\\上传图片\\ordersetting_template.xlsx");
        //获取Excel中文件的工作表，可根据顺序、名称获取
        Sheet sheetAt = workbook.getSheetAt(1);
        //遍历工作表获取行对象
        for (Row row :sheetAt){     //遍历行数据
            for(Cell cell:row){
                //获取单元格中的值
                String value = cell.getStringCellValue();//只获取字符的数据类型，数值类型需转换成字符类型
                System.out.println(value);
            }
        }
        //关闭工作簿
        workbook.close();
    }

    /**
     * 导出数据成为Excel格式
     * @throws IOException
     */
    @Test
    public void importExcel() throws IOException {
        //在内存中创建一个Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表，指定工作表名称
        XSSFSheet sheet = workbook.createSheet("尚硅谷");

        //创建行，0表示第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格，0表示第一个单元格
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("小明");
        row1.createCell(2).setCellValue("10");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小王");
        row2.createCell(2).setCellValue("20");

        //通过输出流将workbook对象下载到磁盘
        FileOutputStream out = new FileOutputStream("D:\\atguigu.xlsx");
        workbook.write(out);
        out.flush();//刷新
        out.close();//关闭
        workbook.close();
    }

}
