package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;

import com.atguigu.service.MemberService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**数据报表数据的控制层
 * @author lijian
 * @create 2021-01-16 18:30
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    MemberService memberService;
    @Reference
    SetmealService setmealService;
    @Reference
    ReportService reportService;
    /**
     *从逻辑层查询数据，生成从当前月一整的会员增长数据。折线图
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //1、获取当前月份，获取当前月份往前一整年的所有的月份数据
        Calendar calendar = Calendar.getInstance();//单例模式
        calendar.add(Calendar.MONTH,-12);//往后倒12个月
        List<String> list = new ArrayList<>();
        for (int i = 0; i <12 ; i++) {
            calendar.add(Calendar.MONTH,1);//当前月向后一个月
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));//将日历格式化成yyyy-MM形式
        }
        //2、map集合向前端返回我们查询封装好的数据
        /*前台格式样式需求
            "data":{
            "months":["2019-01","2019-02","2019-03","2019-04"],
            "memberCount":[3,4,8,10]    },
         */
        Map<String, Object> map = new HashMap<>();
        // ① 将12 个月存到map集合中
        map.put("months",list);
        // ② 查询过去12个月的数据将，存放在map集合中
        List<Integer> memberCount=memberService.findMemberByDate(list);
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    /**
     * 查询数据库中热门套餐的占比，返回map集合。在前台进行饼状图显示
     * 前台需要的数据格式：
     *              "setmealNames":["套餐1","套餐2","套餐3"],
     *             "setmealCount":[
     *                             {"name":"套餐1","value":10},
     *                             {"name":"套餐2","value":30},
     *                             {"name":"套餐3","value":25}
     *              ]
     *              将数据封装成     map集合形式返回：data--->Map
                                                     setmealNames--->List<String>
                                                     setmealCount--->List<Map>
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        //1、获得套餐名称和套餐名称对应的数据
        List<Map<String,Object>> listCount =setmealService.findSetmealCount();


        //2、遍历查出的list集合，取出map 中name对应的套餐名字，添加到list<String>集合中，
        List<String> listName = new ArrayList<>();
        for (Map<String, Object>  mapName: listCount) {
            String name = (String) mapName.get("name");
            listName.add(name);//["套餐1","套餐2","套餐3"],
        }
        // 返回给前端封装的map集合
        Map<String, Object>  ResultMap= new HashMap<>();//用于返回给前端页面的格式：map:{"setmealNames",list<string>},{"setmealCount",list<map>}
        ResultMap.put("setmealCount",listCount);
        ResultMap.put("setmealNames",listName);

        return  new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,ResultMap);
    }

    /**
     * 查询运营数据的详细信息。返回map集合
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map=reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {

            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);

        }

    }

    /**
     * 导出运营报表
     */
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse  response) throws Exception {
      //调远程服务获取表单数据
        Map<String, Object> result = reportService.getBusinessReportData();
        //取出返回的结果数据，将数据写入到Excel文件中
        String reportDate = (String) result.get("reportDate");
        Integer todayNewMember = (Integer) result.get("todayNewMember");
        Integer totalMember = (Integer) result.get("totalMember");
        Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
        Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
        Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
        Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
        Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
        Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
        Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
        Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
        //获取Excel模板文件的绝对路径,File.separator：系统（根据平台不同而不同）默认的分割符
        String temlateRealPath=request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
        //读取模板文件创建Excel表格对象
        XSSFWorkbook  workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
        XSSFSheet sheet = workbook.getSheetAt(0);//获得第一个工作表
        XSSFRow row = sheet.getRow(2);
        row.getCell(5).setCellValue(reportDate);//设置日期

        row=sheet.getRow(4);//获取行
        row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
        row.getCell(7).setCellValue(totalMember);//总会员数
        row = sheet.getRow(5);
        row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
        row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

        row = sheet.getRow(7);
        row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
        row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

        row = sheet.getRow(8);
        row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
        row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

        row = sheet.getRow(9);
        row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
        row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

        int rowNum=12;//模板文件从第12行开始存放套餐数据
        for (Map map : hotSetmeal) {
            String name = (String) map.get("name");//套餐名
            Long setmeal_count = (Long) map.get("setmeal_count");//预约数量
            BigDecimal proportion = (BigDecimal) map.get("proportion");//占比

            row=sheet.getRow(rowNum++);
            row.getCell(4).setCellValue(name);
            row.getCell(5).setCellValue(setmeal_count);
            row.getCell(6).setCellValue(proportion.doubleValue());

        }

        //通过输出流进行文件下载
        ServletOutputStream out = response.getOutputStream();
        //下载的数据类型.excel表格
        response.setContentType("application/vnd.ms-excel");
        //下载的形式（附件方式下载），filename：默认文件名
        response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

        workbook.write(out);
        //刷新此输出流，并强制写出所有缓冲的输出字节。
        out.flush();
        //关闭流操作

        out.close();
        workbook.close();

    }
}
