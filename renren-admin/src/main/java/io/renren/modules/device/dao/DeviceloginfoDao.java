package io.renren.modules.device.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.device.entity.DeviceloginfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
public interface DeviceloginfoDao extends BaseMapper<DeviceloginfoEntity> {

    @Delete("delete from deviceloginfo WHERE  DevID =#{devid} and  LogTime >=#{startTime} and  LogTime<=#{endTime}")
    int deleteDevidLogByDate(String devid, Timestamp startTime,Timestamp endTime);

    /*@Select("select from deviceloginfo WHERE  DevID =#{devid} and  LogTime >=#{startTime} and  LogTime<=#{endTime}")
    List<DeviceloginfoEntity> queryDevidLogByDate(String devid, Timestamp startTime,Timestamp endTime);*/


    @Select("select DevID,LogLevel ,LogTime,LogContent from  deviceloginfo  where DevID=#{devid} ")
    List<DeviceloginfoEntity>  queryDByDevidLog(String devid);


    @Select("Select DeviceID from deviceinfo,sys_dept where deviceinfo.SysNumber=sys_dept.dept_id  and  sys_dept.is_current=1")
    List<DeviceinfoEntity> queryDevidList();



    @Select("Select RecNumber,DevID,LogLevel,LogTime,LogContent from deviceloginfo where deviceloginfo.DevID=#{devid}  and  LogTime >=#{start} and  LogTime<=#{end}")
    List<DeviceloginfoEntity> queryDevidLog(String devid, Timestamp start,Timestamp end);


}
