package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealAndGroupDao;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-06 21:45
 */
@Service(interfaceClass = SetmealService.class)     //使用dubbo注解服务到控制中心，事务
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;
    @Autowired
    SetmealAndGroupDao setmealAndGroupDao;

    @Autowired
    JedisPool jedisPool;

    /**
     * 添加套餐游数据到数据库中，基础数据：套餐表；关联团体游数据：中间表中
     *
     * @param setmeal        套餐基本数据
     * @param travelgroupIds 套餐-团体数据
     */
    @Override
    public void add(Setmeal setmeal, Integer[] travelgroupIds) {
        setmealDao.add(setmeal);//1、添加基本套餐数据到套餐表
        addSetmealAndGroupDao(setmeal.getId(), travelgroupIds);//2、添加套餐-团体游数据到中间表中
        //3、向redis数据库中添加存入到mysql中提交的图片地址，后面用于通过求差集来清理云服务器上垃圾图片
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    /**
     * 分页查询套餐数据
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //1、借助分页插件开始分页开始
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //2、从数据库中查询套餐表数据
        Page<Setmeal> setmeals = setmealDao.findPage(queryPageBean.getQueryString());
        return new PageResult(setmeals.getTotal(), setmeals.getResult());
    }

    /**
     * 查询所有的setmeal数据
     * @return
     */
    @Override
    public List<Setmeal> getSetmealAll() {
        return setmealDao.getSetmealAll();
    }

    /**
     * 查询套餐表中的所有数据，包括跟团游数据 细节
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailsById(String id) {
        return setmealDao.findDetailsById(id);
    }

    /**
     * 根据id查询套餐数据
     * @return
     * @param id
     */
    @Override
    public Setmeal getSetmealById(String id) {
        return setmealDao.getSetmealById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    //向中间表中添加数据
    private void addSetmealAndGroupDao(Integer id, Integer[] travelgroupIds) {
        if (travelgroupIds != null && travelgroupIds.length > 0) {
            Map<String, Integer> map = new HashMap<>();
            for (Integer travelgroupId : travelgroupIds) {
                map.put("setmealId", id);
                map.put("travelgroupId", travelgroupId);
                setmealAndGroupDao.add(map);
            }

        }
    }
}
