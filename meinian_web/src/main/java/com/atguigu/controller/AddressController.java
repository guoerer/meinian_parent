package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**分店地图页面的的展示
 * @author lijian
 * @create 2021-01-17 14:26
 */
@RequestMapping("/address")
@RestController
public class AddressController {
    @Reference
    AddressService addressService;

    /**
     * 查找分店地图的详细信息
     * @return
     */
    @RequestMapping("/findAllMaps")
    public Map<String, Object> findAllMaps(){
       List<Address> addressList= addressService.findAllMaps();
        //1、定义分店坐标集合
        List<Map> gridnMaps = new ArrayList<>();
        //2、定义分店的集合名称
        List<Map> nameMaps = new ArrayList<>();
        //3、遍历集合取出经纬度
        for (Address address : addressList) {
            Map gridnMap = new HashMap();
            //获取经度
            gridnMap.put("lng",address.getLng());
            //获取纬度
            gridnMap.put("lat",address.getLat());
            gridnMaps.add(gridnMap);
            //获取地址的名字
            Map<String, Object> nameMap = new HashMap<>();
            nameMap.put("addressName",address.getAddressName());
            nameMaps.add(nameMap);
        }
        //向前端存入数据集合
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("gridnMaps",gridnMaps);
        resultMap.put("nameMaps",nameMaps);
        return resultMap;
    }

    /**
     * 分页查询分公司的地址
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=null;
        pageResult=addressService.findPage(queryPageBean);
        return pageResult;
    }
    //添加功能
    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address){
        addressService.addAddress(address);
        return new Result(true, "分公司地址添加成功");
    }
    //删除功能
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        addressService.deletById(id);
        return new Result(true, "分公司地址删除成功");
    }
}
