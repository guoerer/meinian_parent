package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 团体旅游项目控制层
 *
 * @author lijian
 * @create 2021-01-05 19:51
 */
@RestController
@RequestMapping("/travelgroup")
public class TravelGroupController {
    @Reference
    TravelGroupService travelGroupService;

    /**
     * 查询所有的团体游数据显示到套餐页面供套餐项目添加使用
     *
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<TravelGroup> travelGroups = travelGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, travelGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

    /**
     * 团体旅游项目添加功能的实现
     * @param travelGroup   表单封装好的团体数据
     * @param travelItemIds 选中的自由行项目
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TravelGroup travelGroup, Integer[] travelItemIds) {
        try {
            travelGroupService.add(travelGroup, travelItemIds);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }

    /**
     * 分页查询团体游数据显示到页面中
     *
     * @param queryPageBean 查询条件
     * @return PageResult 封装好的结果集
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = travelGroupService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 查询团体旅游数据，用于数据回显
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result test(Integer id) {
        try {
            TravelGroup travelGroup = travelGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

    /**
     * 查询该团体有的自由行项目，用于编辑前页面数据的回显
     *
     * @param id 根据团队的id值进行查询
     * @return
     */
    @RequestMapping("/findTravelItemIdByTravelgroupId")
    public Result findTravelItemIdByTravelgroupId(Integer id) {
        try {
            List<Integer> travelItems = travelGroupService.findTravelItemIdByTravelgroupId(id);
            return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, travelItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }

    /**
     * 修改功能的实现
     *
     * @param travelGroup
     * @param travelItemIds
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody TravelGroup travelGroup, Integer[] travelItemIds) {
        try {
            travelGroupService.edit(travelGroup, travelItemIds);
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }
    }

    /**
     * 删除功能的实现
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            travelGroupService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }
}
