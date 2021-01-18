package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**order
 * @author lijian
 * @create 2021-01-11 19:24
 */
public interface OrdersettingDao {
    int selectByDate(Date orderDate);

    void add(OrderSetting orderSetting);

    void edit(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(Map<String, Object> map);

    int findReservationsByDate(Date date);

    int findNumberByDate(Date date);

    void editReservationsByDate(@Param("reservations") int reservation, @Param("orderDate") Date date);
}
