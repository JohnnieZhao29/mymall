package com.zjy.mymall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.common.utils.PageUtils;
import com.zjy.mymall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-26 11:45:57
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

