package io.renren.modules.device.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.device.dao.DetpdomainnumdctDao;
import io.renren.modules.device.entity.DetpdomainnumdctEntity;
import io.renren.modules.device.service.DetpdomainnumdctService;


@Service("detpdomainnumdctService")
public class DetpdomainnumdctServiceImpl extends ServiceImpl<DetpdomainnumdctDao, DetpdomainnumdctEntity> implements DetpdomainnumdctService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DetpdomainnumdctEntity> page = this.selectPage(
                new Query<DetpdomainnumdctEntity>(params).getPage(),
                new EntityWrapper<DetpdomainnumdctEntity>()
        );

        return new PageUtils(page);
    }

}
