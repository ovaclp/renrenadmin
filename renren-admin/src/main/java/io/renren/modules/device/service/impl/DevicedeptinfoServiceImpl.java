package io.renren.modules.device.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.device.dao.DevicedeptinfoDao;
import io.renren.modules.device.entity.DevicedeptinfoEntity;
import io.renren.modules.device.service.DevicedeptinfoService;


@Service("devicedeptinfoService")
public class DevicedeptinfoServiceImpl extends ServiceImpl<DevicedeptinfoDao, DevicedeptinfoEntity> implements DevicedeptinfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DevicedeptinfoEntity> page = this.selectPage(
                new Query<DevicedeptinfoEntity>(params).getPage(),
                new EntityWrapper<DevicedeptinfoEntity>()
        );

        return new PageUtils(page);
    }

}
