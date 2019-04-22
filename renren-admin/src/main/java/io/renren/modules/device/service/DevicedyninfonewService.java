package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.controller.DevicedyninfonewController;
import io.renren.modules.device.entity.DevicedyninfonewEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-26 12:40:15
 */
public interface DevicedyninfonewService extends IService<DevicedyninfonewEntity> {

    PageUtils queryPage(Map<String, Object> params);
    DevicedyninfonewEntity queryDevnewState(String devid);
    List<DevicedyninfonewEntity>  queryDevidsBySysnum(String sysNum);
    List<DevicedyninfonewEntity> queryDeviceBydevid();
}
