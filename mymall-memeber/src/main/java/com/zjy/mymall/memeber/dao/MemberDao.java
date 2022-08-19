package com.zjy.mymall.memeber.dao;

import com.zjy.mymall.memeber.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-26 11:33:50
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
