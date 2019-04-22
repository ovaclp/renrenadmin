package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.DevlogcontentinfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-11-09 15:00:25
 */
public interface DevlogcontentinfoService extends IService<DevlogcontentinfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

