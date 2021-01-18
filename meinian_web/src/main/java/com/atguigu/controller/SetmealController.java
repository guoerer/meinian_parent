package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author lijian
 * @create 2021-01-06 21:34
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;
    /**
     * 上传图片到七牛云服务器上
     * 直接调用七牛云封装好的工具类，上传数据到云服务中，没有进入service层
     *
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        try {
            //1、获得原始文件名，重命名，调用七牛云封装好的工具类，上传文件到云服务器中
            String originalFilename = imgFile.getOriginalFilename();//获取原有文件名
            int lastIndexOf = originalFilename.lastIndexOf(".");//获取文件名下标的位置
            String subFileType = originalFilename.substring(lastIndexOf);//截取文件的后缀名
            String uuidName = UUID.randomUUID().toString().replaceAll("-", "");//产生唯一标识的文件名，防止文件被覆盖掉
            String fileName = uuidName + subFileType;//给文件取新的文件名
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //将上传到云服务的数据存到redis中，通过和数据库中提交的数据求差去除多余的图片
           jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);//回显文件名，用于图片回显
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_SUCCESS);
        }
    }

    /**
     * 向套餐中添加数据
     *
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] travelgroupIds) {
        try {
            setmealService.add(setmeal, travelgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 分页查询
     *
     * @param queryPageBean 查询条件
     * @return
     */
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult page = setmealService.findPage(queryPageBean);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, page);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
