package io.renren.modules.device.dao;

import io.renren.modules.device.entity.AgentinfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-04 09:33:27
 */
public interface AgentinfoDao extends BaseMapper<AgentinfoEntity> {


    @Select("SELECT agentinfo.* FROM agentinfo , deviceinfo WHERE deviceinfo.DeviceID=#{devid} AND deviceinfo.AgentNum=agentinfo.RecNumber")
    List<AgentinfoEntity> agengid(String devid);


}
