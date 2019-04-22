package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.DevicedeptinfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-11-09 21:32:24
 */
public interface DevicedeptinfoService extends IService<DevicedeptinfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

