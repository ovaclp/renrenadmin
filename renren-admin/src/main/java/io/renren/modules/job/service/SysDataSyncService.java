package io.renren.modules.job.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.job.entity.SysDataSyncEntity;

import java.util.Map;

/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2019-01-16 19:01:34
 */
public interface SysDataSyncService extends IService<SysDataSyncEntity> {

    PageUtils queryPage(Map<String, Object> params);

    SysDataSyncEntity findOneSysDataSync(String sysType );
}

