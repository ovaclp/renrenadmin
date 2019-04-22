package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.device.entity.DeviceloginfoEntity;

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
public interface DeviceloginfoService extends IService<DeviceloginfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<DeviceloginfoEntity> query_Devid(Map<String, Object > params);
    //查询本级代理的设备日志
    List<DeviceloginfoEntity> querylogByDate(Map<String, String > params);
    //查询单个设备devid在数据库的日志内容
    List<DeviceloginfoEntity> queryDByDevid(String devid);
    //查询本级监控中心的devid列表
    List<DeviceinfoEntity>  queryDevList();


}

