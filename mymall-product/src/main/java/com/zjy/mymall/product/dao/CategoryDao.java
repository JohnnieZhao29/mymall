package com.zjy.mymall.product.dao;

import com.zjy.mymall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-25 21:59:00
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
