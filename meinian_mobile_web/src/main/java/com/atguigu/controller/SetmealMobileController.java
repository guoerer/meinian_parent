package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijian
 * @create 2021-01-12 10:52
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    SetmealService setmealService;

    /**
     * 获取所有的旅游套餐信息，供用户去选择指定的套餐
     *
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmealAll() {
        try {
            List<Setmeal> setmealAll = setmealService.getSetmealAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setmealAll);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 根据套餐id，查询套餐中所有的详细信息，包括跟团游信息和自由行信息
     *
     * @param id 套餐的id
     * @return 套餐详细信息
     */
    @RequestMapping("/findById")
    public Result findById(String id) {
        try {
            Setmeal setmeal = setmealService.findDetailsById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据id值获取基本的套餐数据
     * @return
     */
    @RequestMapping("/getSetMealById")
    public Result getSetMealById(String id) {
        try {
            Setmeal setmeal = setmealService.getSetmealById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }
}
