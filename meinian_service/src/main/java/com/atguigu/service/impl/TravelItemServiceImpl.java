package com.atguigu.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author lijian
 * @create 2021-01-04 19:10
 */
@Transactional  //事务注解
@Service(interfaceClass = TravelItemService.class)
public class TravelItemServiceImpl implements TravelItemService {
    @Autowired
    private TravelItemDao travelItemDao;

    /**
     * service层中添加功能的实现，调用dao层中的add方法。
     * @param travelItem
     */
    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    /**
     * 分页查询功能的service层实现方法
     * @param currentPage ：当前页面
     * @param pageSize ：页面数
     * @param queryString ：查询条件
     * @return pageResult: 返回分页查询的结果集包含 总记录数和当前页结果
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //使用mybatis中 pagehelper插件进行分页查询
        //1、开启分页功能
        PageHelper.startPage(currentPage,pageSize);//limit(a,b)
        Page<TravelItem> pages=travelItemDao.findPage(queryString); //加入分页查询的条件
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    /**
     * 自由行删除功能的实现，删除时判断是否存在旅行团数据，如有则不进行删除
     * @param id 自由的id主键
     */
    @Override
    public void delete(Integer id) {
        //查询中间表是否存在该数据
        int count=travelItemDao.findCountByTravelItemId(id);
        if (count>0){
            throw new RuntimeException(MessageConstant.DELETE_TRAVELITEMFORID_FAIL);
        }
        travelItemDao.delete(id);
    }

    /**
     * 查询数据，数据回显
     * @param id
     * @return
     */
    @Override
    public TravelItem findById(Integer id) {
        return travelItemDao.findById(id);
    }

    /**
     * 查询所有自由行数据
     * @return
     */
    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }

    /**
     *修改功能的实现
     * @param travelItem
     */
    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }
}

