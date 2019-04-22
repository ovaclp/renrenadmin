package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.DetpdomainnumdctEntity;

import java.util.Map;

/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-21 15:56:47
 */
public interface DetpdomainnumdctService extends IService<DetpdomainnumdctEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

