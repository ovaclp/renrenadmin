package io.renren.modules.device.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.DeviceEntity;
import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.sys.entity.SysMenuEntity;

import java.io.UnsupportedEncodingException;
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
public interface DeviceinfoService extends IService<DeviceinfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<DeviceEntity> queryDevice();
    String devid_sysNumber(String devid);
    String  is_Current(String  devid);
    int killStrategy(String devid);
    List<Map<String,Object>>  queryDev_ipid(String  devid);

    /**
     * 返回MMD的VID指定条目的基本信息
     * @param deviceid
     * @return
     */
    DeviceinfoEntity queryallInfoById(String deviceid) throws UnsupportedEncodingException;



}

