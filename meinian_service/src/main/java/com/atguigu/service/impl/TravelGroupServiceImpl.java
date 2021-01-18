package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-05 19:54
 */
@Service(interfaceClass = TravelGroupService.class)
@Transactional      //开启事务注解
public class TravelGroupServiceImpl implements TravelGroupService {
    //
    @Autowired
    TravelGroupDao travelGroupDao;

    /**
     * 团队游添加功能的实现
     * @param travelGroup  团队游数据
     * @param travelItemIds 自由行id 集合
     */
    @Override
    public void add(TravelGroup travelGroup, Integer[] travelItemIds) {
        //1、向团体游项目中添加值,数据dao层返回自增长的主键
        travelGroupDao.add(travelGroup);
        System.out.println(travelGroup.getId());
          //2、获取从团体游的当前id，将该id和选中自由行的id值存放到中间表中
        setGroupAndTravelItem(travelGroup.getId(), travelItemIds);

    }

    /**
     * 分页查询团体游数据
     * @param currentPage 当前页
     * @param pageSize 每页显示数量
     * @param queryString 查询条件
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //1、借助分页插件开始分页
        PageHelper.startPage(currentPage,pageSize);
        //2、调用dao层放回数据结果集
        Page<TravelGroup> travelGroups=travelGroupDao.findPage(queryString);
        return new PageResult(travelGroups.getTotal(),travelGroups.getResult());
    }

    /**
     * 查询表中该旅游团对应的自由项项目
     * @param id
     * @return
     */
    @Override
    public List<Integer> findTravelItemIdByTravelgroupId(Integer id) {
        return travelGroupDao.findTravelItemIdByTravelgroupId(id);
    }

    /**
     * 修改功能的实现,分成两张表修改，团队项目表和团体-自由行中间表
     * @param travelGroup
     * @param travelItemIds
     */
    @Override
    public void edit(TravelGroup travelGroup, Integer[] travelItemIds) {
        //1、修改旅游团数据
        travelGroupDao.edit(travelGroup);
        //2、删除该id对应的中间表数据
        travelGroupDao.delteTravelItemByGroupId(travelGroup.getId());
        //3、将新的自由行项目添加到对应的id上
        setGroupAndTravelItem(travelGroup.getId(),travelItemIds);
    }

    /**
     * 从数据库中查询所有的团体游数据
     * @return
     */
    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }

    /**
     * 删除用团队游项目，先删除中间表数据，再删除团队表中数据
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //1、从中间表中删除数据
        travelGroupDao.delteTravelItemByGroupId(id);

        //2、从团队表中删除该项数据
        travelGroupDao.delteById(id);
    }

    /**
     * 根据id查询团队游数据
     * @param id
     * @return
     */
    @Override
    public TravelGroup findById(Integer id) {
        return travelGroupDao.findById(id);
    }

    /**
     * 私有方法，用于遍历自由项的值，向中间表中遍历添加数据
     * @param id            团体游的id
     * @param travelItemIds 自由项的所有id值
     */
    private void setGroupAndTravelItem(Integer id, Integer[] travelItemIds) {
        if (travelItemIds != null && travelItemIds.length > 0) {
            Map<String, Integer> map = new HashMap<>();
            for (Integer travelItemId : travelItemIds) {
                map.put("travelGroup",id);
                map.put("travelItem",travelItemId);
                travelGroupDao.setTravelGroupAndTravelItem(map);
            }
        }
    }
}
