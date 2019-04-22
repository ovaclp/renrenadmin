package io.renren.modules.device.dao;

import io.renren.modules.device.entity.DevicedyninfonewEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-26 12:40:15
 */
public interface DevicedyninfonewDao extends BaseMapper<DevicedyninfonewEntity> {

    @Select("select devicedyninfonew.*   from devicedyninfonew where devicedyninfonew.DevID=#{devid}")
    List<DevicedyninfonewEntity>  devnewState(String devid);


    List<DevicedyninfonewEntity> queryDeviceBydevid();


    @Select("SELECT devicedyninfonew.* FROM deviceinfo,devicedyninfonew WHERE deviceinfo.SysNumber=#{sysNum} AND  deviceinfo.DeviceID=devicedyninfonew.DevID")
    List<DevicedyninfonewEntity>  queryDevidsBySysNum(String sysNum);
}
