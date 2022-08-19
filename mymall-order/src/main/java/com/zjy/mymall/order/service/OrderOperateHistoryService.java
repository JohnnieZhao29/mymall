package com.zjy.mymall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.common.utils.PageUtils;
import com.zjy.mymall.order.entity.OrderOperateHistoryEntity;

import java.util.Map;

/**
 * 订单操作历史记录
 *
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-26 11:42:28
 */
public interface OrderOperateHistoryService extends IService<OrderOperateHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

