package io.renren.modules.device.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.device.dao.DevicereginfoDao;
import io.renren.modules.device.entity.DevicereginfoEntity;
import io.renren.modules.device.service.DevicereginfoService;


@Service("devicereginfoService")
public class DevicereginfoServiceImpl extends ServiceImpl<DevicereginfoDao, DevicereginfoEntity> implements DevicereginfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DevicereginfoEntity> page = this.selectPage(
                new Query<DevicereginfoEntity>(params).getPage(),
                new EntityWrapper<DevicereginfoEntity>()
        );

        return new PageUtils(page);
    }

}
