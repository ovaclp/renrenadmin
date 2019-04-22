package io.renren.modules.device.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.device.dao.DevtypeinfoDao;
import io.renren.modules.device.entity.DevtypeinfoEntity;
import io.renren.modules.device.service.DevtypeinfoService;


@Service("devtypeinfoService")
public class DevtypeinfoServiceImpl extends ServiceImpl<DevtypeinfoDao, DevtypeinfoEntity> implements DevtypeinfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DevtypeinfoEntity> page = this.selectPage(
                new Query<DevtypeinfoEntity>(params).getPage(),
                new EntityWrapper<DevtypeinfoEntity>()
        );

        return new PageUtils(page);
    }

}
