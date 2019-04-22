package io.renren.modules.device.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.device.dao.DevlogcontentinfoDao;
import io.renren.modules.device.entity.DevlogcontentinfoEntity;
import io.renren.modules.device.service.DevlogcontentinfoService;


@Service("devlogcontentinfoService")
public class DevlogcontentinfoServiceImpl extends ServiceImpl<DevlogcontentinfoDao, DevlogcontentinfoEntity> implements DevlogcontentinfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DevlogcontentinfoEntity> page = this.selectPage(
                new Query<DevlogcontentinfoEntity>(params).getPage(),
                new EntityWrapper<DevlogcontentinfoEntity>()
        );

        return new PageUtils(page);
    }

}
