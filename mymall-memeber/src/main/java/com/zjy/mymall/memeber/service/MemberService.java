package com.zjy.mymall.memeber.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.common.utils.PageUtils;
import com.zjy.mymall.memeber.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-26 11:33:50
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

