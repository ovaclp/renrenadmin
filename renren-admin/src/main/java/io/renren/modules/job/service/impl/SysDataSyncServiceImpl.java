package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.SysDataSyncDao;
import io.renren.modules.job.entity.SysDataSyncEntity;
import io.renren.modules.job.service.SysDataSyncService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;



@Service("sysDataSyncService")
public class SysDataSyncServiceImpl extends ServiceImpl<SysDataSyncDao, SysDataSyncEntity> implements SysDataSyncService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SysDataSyncEntity> page = this.selectPage(
                new Query<SysDataSyncEntity>(params).getPage(),
                new EntityWrapper<SysDataSyncEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public SysDataSyncEntity findOneSysDataSync(String sysType ){
        return baseMapper.findOneSysDataSync(sysType);

    }

}
