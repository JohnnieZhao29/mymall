package com.zjy.mymall.memeber.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.common.utils.PageUtils;
import com.zjy.mymall.memeber.entity.GrowthChangeHistoryEntity;

import java.util.Map;

/**
 * 成长值变化历史记录
 *
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-26 11:33:50
 */
public interface GrowthChangeHistoryService extends IService<GrowthChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

