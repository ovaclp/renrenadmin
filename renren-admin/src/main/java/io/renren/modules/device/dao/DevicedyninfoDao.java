package io.renren.modules.device.dao;

import java.util.ArrayList;
import java.util.List;

import io.renren.modules.device.entity.DevicedyninfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.entity.DeviceidIpv4DevstateEntity;
import org.apache.ibatis.annotations.Select;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
public interface DevicedyninfoDao extends BaseMapper<DevicedyninfoEntity> {

    //设备最新状态信息数据,内存表
    //ArrayList<DeviceidIpv4DevstateEntity> queryDeviceBydevid();
   // ArrayList<DevicedyninfonewEntity> queryDeviceBydevid();

    //查询最新设备状态
    @Select("select   devicedyninfo.*   from devicedyninfo where devicedyninfo.DevID=#{devid} ORDER BY LastModifyTime DESC")
    List<DevicedyninfoEntity> devidState(String  devid);

    //查询设备最新状态信息
    List<DevicedyninfoEntity> queryDevdynPage();

	
}
