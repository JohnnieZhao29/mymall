package com.zjy.mymall.order.dao;

import com.zjy.mymall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-26 11:42:27
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
