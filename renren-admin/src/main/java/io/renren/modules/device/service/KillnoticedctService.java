package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.KillnoticedctEntity;

import java.util.Map;

/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-04 17:35:14
 */
public interface KillnoticedctService extends IService<KillnoticedctEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

