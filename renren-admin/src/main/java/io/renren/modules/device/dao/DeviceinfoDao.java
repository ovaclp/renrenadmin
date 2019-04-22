package io.renren.modules.device.dao;

import io.renren.modules.device.entity.DeviceEntity;
import io.renren.modules.device.entity.DeviceinfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
public interface DeviceinfoDao extends BaseMapper<DeviceinfoEntity> {

    List<DeviceEntity> queryDevice();

    @Select("select is_current  from deviceinfo,sys_dept where deviceinfo.DeviceID=#{devid}  and  deviceinfo.SysNumber=sys_dept.dept_id and  sys_dept.is_current=1")
    String  isCurrent(String  devid);

    @Select("select SysNumber   from deviceinfo where deviceinfo.DeviceID=#{devid}")
    String devidSysNumber(String  devid);


    @Select("select is_current, center_id,ip_addr   from deviceinfo,sys_dept where deviceinfo.DeviceID=#{devid}  and  deviceinfo.SysNumber=sys_dept.dept_id")
    List<Map<String,Object>>  queryDevipid(String  devid);


    @Select("select kill_strategy  from deviceinfo,sys_dept where deviceinfo.DeviceID=#{devid}  and  deviceinfo.SysNumber=sys_dept.dept_id")
    int killStrategy(String devid);


	
}
