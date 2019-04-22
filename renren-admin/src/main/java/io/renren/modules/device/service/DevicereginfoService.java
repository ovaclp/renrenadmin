package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.DevicereginfoEntity;

import java.util.Map;

/**
 * 密码设备注册表
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-08 17:05:55
 */
public interface DevicereginfoService extends IService<DevicereginfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

