package io.renren.modules.device.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.device.entity.DevicedyninfoEntity;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.entity.DeviceidIpv4DevstateEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
public interface DevicedyninfoService extends IService<DevicedyninfoEntity> {

    PageUtils queryPage(Map<String, Object> params);


    //ArrayList<DevicedyninfonewEntity> queryDeviceBydevid();
    /**
     * 返回MMD所有状态信息
     */


    DevicedyninfoEntity queryallInfoById(String deviceid);

    /**
     * 毙杀MMD
     */
     Boolean KilldevById(String deviceid);
    /**
     * 返回MMD的VID指定的两个属性的状态信息(密钥更新状态/密码算法资源情况)
     */
    //DevicedyninfoEntity queryltInfoById(String deviceid);
    DevicedyninfoEntity devState(String devid);
    List<DevicedyninfoEntity>  query_DevdynPage();
   // List<DevicedyninfoEntity> selectMyPage(Page rowBounds, @Param("ew") Wrapper<DevicedyninfoEntity> wrapper);
    //Page<DevicedyninfoEntity> selectMyPage(Page rowBounds, Wrapper<DevicedyninfoEntity> wrapper);
}

