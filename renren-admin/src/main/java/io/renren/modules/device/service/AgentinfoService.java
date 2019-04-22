package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.AgentinfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-04 09:33:27
 */
public interface AgentinfoService extends IService<AgentinfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<AgentinfoEntity> agent_id(String devid);

}

