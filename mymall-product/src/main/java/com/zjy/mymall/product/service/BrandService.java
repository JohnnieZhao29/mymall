package com.zjy.mymall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.common.utils.PageUtils;
import com.zjy.mymall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-25 21:59:00
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

