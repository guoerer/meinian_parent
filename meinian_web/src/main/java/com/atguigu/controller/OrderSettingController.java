package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrdersettingService;
import com.atguigu.utils.POIUtils;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**预约管理的控制层
 * @author lijian
 * @create 2021-01-11 18:52
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    //使用dubbo框架组件，远程调用service层的接口方法
    @Reference
    private OrdersettingService ordersettingService;

    /**
     * 上传Excel文件到数据库中
     * excelFile:前台传来文件的属性名
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){

        try {
            //1、使用poi工具类，解析Excel文件，读取文件中的内容
            List<String[]> lists = POIUtils.readExcel(excelFile);
            //2、将从模板文件中读取的值，封装成orderSetting对行
            List<OrderSetting> orderSettings = new ArrayList<>();
            //3、迭代lists中读取的Excel中的每一行数据将每行数据封装成orderSetting 对象
            for (String[] str : lists) {
                OrderSetting orderSetting = new OrderSetting(new Date(str[0]), Integer.parseInt(str[1]));
                orderSettings.add(orderSetting);
            }
            //4、调用业务层保存数据
            ordersettingService.add(orderSettings);
            //5、返回结果
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param date :日期类型格式：2021-1
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            //返回date月的所有预约信息，date格式：2021-1
            List<Map> list= ordersettingService.getOrderSettingByMonth(date);
            //返回结果集e
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);

        }
    }

    /**
     * 修改预约设置信息
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            //修改预约信息
            ordersettingService.editNumberByDate(orderSetting);
            //返回结果集e
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);

        }
    }
}
