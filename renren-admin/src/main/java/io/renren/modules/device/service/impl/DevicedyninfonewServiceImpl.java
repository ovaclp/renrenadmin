package io.renren.modules.device.service.impl;

import io.renren.modules.device.service.DevicedyninfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.device.dao.DevicedyninfonewDao;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.service.DevicedyninfonewService;


@Service("devicedyninfonewService")
public class DevicedyninfonewServiceImpl extends ServiceImpl<DevicedyninfonewDao, DevicedyninfonewEntity> implements DevicedyninfonewService {

    @Autowired
    public DevicedyninfoService devicedyninfoService;
    @Autowired
    public DevicedyninfonewService devicedyninfonewService;

    public boolean flag=true;

    public int count=0;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Page<DevicedyninfonewEntity> page = this.selectPage(
                new Query<DevicedyninfonewEntity>(params).getPage(),
                new EntityWrapper<DevicedyninfonewEntity>().orderBy("DevID").orderBy("LastModifyTime",false)
        );

        return new PageUtils(page);
    }

    public DevicedyninfonewEntity queryDevnewState(String devid){
        DevicedyninfonewEntity  devicedyninfonewEntity=new DevicedyninfonewEntity();
        List<DevicedyninfonewEntity> devicedyninfonewEntities=baseMapper.devnewState(devid);
        if(devicedyninfonewEntities.size()==1){
            devicedyninfonewEntity=devicedyninfonewEntities.get(0);
            return  devicedyninfonewEntity;
        }else{
            return null;
        }

    }


    public List<DevicedyninfonewEntity>  queryDevidsBySysnum(String sysNum){
        return  this.baseMapper.queryDevidsBySysNum(sysNum);
    }


    @Override
    public List<DevicedyninfonewEntity> queryDeviceBydevid(){
        return baseMapper.queryDeviceBydevid();
    }

}
