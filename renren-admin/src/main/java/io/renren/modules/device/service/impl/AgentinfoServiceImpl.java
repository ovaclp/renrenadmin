package io.renren.modules.device.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.device.dao.AgentinfoDao;
import io.renren.modules.device.entity.AgentinfoEntity;
import io.renren.modules.device.service.AgentinfoService;


@Service("agentinfoService")
public class AgentinfoServiceImpl extends ServiceImpl<AgentinfoDao, AgentinfoEntity> implements AgentinfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AgentinfoEntity> page = this.selectPage(
                new Query<AgentinfoEntity>(params).getPage(),
                new EntityWrapper<AgentinfoEntity>()
        );

        return new PageUtils(page);
    }

    public List<AgentinfoEntity> agent_id(String devid){
        return this.baseMapper.agengid(devid);
    }


}
