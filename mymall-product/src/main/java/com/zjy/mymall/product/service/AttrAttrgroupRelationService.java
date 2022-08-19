package com.zjy.mymall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.common.utils.PageUtils;
import com.zjy.mymall.product.entity.AttrAttrgroupRelationEntity;

import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-25 21:59:00
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

