package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.DevtypeinfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-11-07 15:59:57
 */
public interface DevtypeinfoService extends IService<DevtypeinfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

