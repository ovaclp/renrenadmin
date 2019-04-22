package io.renren.modules.device.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.device.dao.KillnoticedctDao;
import io.renren.modules.device.entity.KillnoticedctEntity;
import io.renren.modules.device.service.KillnoticedctService;


@Service("killnoticedctService")
public class KillnoticedctServiceImpl extends ServiceImpl<KillnoticedctDao, KillnoticedctEntity> implements KillnoticedctService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<KillnoticedctEntity> page = this.selectPage(
                new Query<KillnoticedctEntity>(params).getPage(),
                new EntityWrapper<KillnoticedctEntity>()
        );

        return new PageUtils(page);
    }

}
