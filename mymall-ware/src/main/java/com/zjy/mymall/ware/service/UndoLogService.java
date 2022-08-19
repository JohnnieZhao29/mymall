package com.zjy.mymall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.common.utils.PageUtils;
import com.zjy.mymall.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author johnnie
 * @email 1608918778@qq.com
 * @date 2022-07-26 11:45:58
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

