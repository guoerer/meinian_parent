package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijian
 * @create 2021-01-04 14:47
 */
@RestController     //注册controller层，和@responsebody (通过json 格式将数据返回到前台)
@RequestMapping("/travelItem")
public class TravelItemController {
    //调用service层组件，实现添加功能
    //使用dubbo接口，通过zookerper来实现接口服务的注册
    @Reference
    private TravelItemService travelItemService;

    /**
     * 自由行添加功能
     *
     * @param travelItem ：从前台传入的表单数据：json格式
     * @return result，结果集-用于存放是否成功数据
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('TRAVELITEM_ADD')") //校验是否有添加自由的行为权限
    public Result add(@RequestBody TravelItem travelItem) {
        try {//没有错误说明添加自由行成功
            System.out.println(travelItem);
            travelItemService.add(travelItem);
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
    }

    /**
     * 分页查询功能
     * 前端分页查询自由行方法的实行，controller层返回调用service层的查询结果。
     * 将结果封装成一个分页结果封装对象-PageResult
     *
     * @param queryPageBean 前端封装好的查询条件：当前页码-currentPage，每页记录数-pageSize，查询条件-queryString
     * @return 分页结果对象 PageResult
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        System.out.println("queryPageBean = " + queryPageBean);
        PageResult pageResult = travelItemService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 自由行删除功能的实现,
     *
     * @param id 要删除的id
     * @return result 结果集
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE123')")//权限校验，使用TRAVELITEM_DELETE123测试
    public Result delete(Integer id) {
        try {
            travelItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    /**
     * 根据id查询表中自由行数据，数据回显
     *
     * @param id 查询自由的id值
     * @return result结果集
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        //调用service层，查询自由行的id
        try {
            TravelItem travelItem = travelItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }

    /**
     * 修改功能的实现
     * @param travelItem 修改表单数据json
     * @return
     */
    @RequestMapping("/edit")
    @PreAuthorize("hasAnyAuthority('TRAVELITEM_EDIT')")  //权限校验
    public Result edit(@RequestBody TravelItem travelItem) {
        try {
            travelItemService.edit(travelItem);
            return new Result(true, MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }

    /**
     * 查询所有的自由行数据供旅游团添加自由项目时选用
     * @return
     */
    @RequestMapping("/findAll")
    @PreAuthorize("hasAnyAuthority('TRAVELITEM_QUERY')") //权限校验
    public Result findAll(){
        try {
            List<TravelItem> travelItems=travelItemService.findAll();
            return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELITEM_FAIL);
        }

    }
}
