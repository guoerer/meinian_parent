package com.atguigu.job;

import com.atguigu.constant.RedisConstant;
import com.atguigu.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**自动执行删除垃圾图片的工具类
 * @author lijian
 * @create 2021-01-07 13:37
 */
public class ClearImgJob {
    @Autowired      //自动装配redis数据库连接池，通过redis数据库求交集去除云服务数据库多余的图片
    JedisPool jedisPool;

    /**
     * 图片清理实现方法，通过spring容器注册到定时任务组件quartz上
     * 定时执行该方法
     */
    public void clearImg(){
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = sdiff.iterator();//通过迭代器来遍历被删除的图片
        while(iterator.hasNext()){      //通过迭代器判断是否有下一个值，
            String pic = iterator.next();//将迭代器位置下移到下一个值中,set集合（无序，不可重复单例集合，底层链表-增删快）。取set集合中的值
            System.out.println("被删除的图片是："+pic);
            QiniuUtils.deleteFileFromQiniu(pic);//调用七牛云工具类删除多余的垃圾文件
            //删除redis中-存贮云服务（SETMEAL_PIC_RESOURCES）多余的value值
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
        }
    }
}
