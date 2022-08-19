package com.zjy.mymall.order.dao;

import com.zjy.mymall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-26 11:42:27
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
