package com.zjy.mymall.memeber.dao;

import com.zjy.mymall.memeber.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-26 11:33:49
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
