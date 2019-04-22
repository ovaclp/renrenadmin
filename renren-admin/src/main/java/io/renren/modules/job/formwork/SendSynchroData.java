package io.renren.modules.job.formwork;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.config.RestTemplateConfig;
import io.renren.common.utils.CodeUtils;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.device.service.DevicedyninfonewService;
import io.renren.modules.device.service.DeviceinfoService;
import io.renren.modules.job.entity.SysDataSyncEntity;
import io.renren.modules.job.service.SysDataSyncService;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
//@RunWith(SpringRunner.class)
@Import({RestTemplateConfig.class})
public class SendSynchroData {

    @Resource
    private RestTemplate restTemplate;
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysDataSyncService sysDataSyncService;

    @Autowired
    private DeviceinfoService deviceinfoService;

    @Autowired
    private DevicedyninfonewService devicedyninfonewService;

    public void sendSyncData() {



        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<String, String>();
        Map<String, Object> map = new HashMap<>();
        map.put("is_current", 1);
        List<SysDeptEntity> dept = sysDeptService.selectByMap(map);
        if (dept!=null&&dept.size()>0){
            List<SysDeptEntity> list = sysDeptService.findSysDeptList();//查询存在上级的数据（SQL判断查询parentId不为空的数据）
            String parentUrl = dept.get(0).getParentIp();

            String urlparam="http://" + parentUrl + ":8080/renren-admin/device/deviceinfo/devlist";
            if(!CodeUtils.urlOnline(urlparam)){
                System.out.println("-----------第三方接口连接失败---------------");
                return ;

            }


            SysDataSyncEntity sysDept = findOneSysDataSync("centerInfoSync");
            List<String> deptIds = new ArrayList<String>();
            List<String> deviceIds = new ArrayList<String>();
            long currenttime = System.currentTimeMillis();
            Date date = new Date(currenttime);
            SysDataSyncEntity sysDataSyncEntity = new SysDataSyncEntity();
            sysDataSyncEntity.setLastsynctime(date);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    deptIds.add(list.get(i).getDeptId());
                    long lastModiyTime = list.get(i).getLastModifyTime().getTime();
                    long lastSyncTime = sysDept.getLastsynctime().getTime();
                    if (lastModiyTime > lastSyncTime) {
                        list.get(i).setLastModifyTime(date);
                    } else {
                        list.remove(i);
                    }
                }
                if (list != null && list.size() > 0) {
                    //System.out.println("---发送数据"+list.size()+"---");
                    requestParam.set("centerInfoSync", JSON.toJSONString(list));
                    try {
                        SynchroDataFormWork result = restTemplate.postForObject("http://" + parentUrl + ":8080/renren-admin/device/deviceinfo/devlist", requestParam, SynchroDataFormWork.class);
                        if (result!=null && Boolean.valueOf(result.getCenterInfoSync())){
                            SysDataSyncEntity sysData = new SysDataSyncEntity();
                            sysData.setSyncType("centerInfoSync");
                            EntityWrapper<SysDataSyncEntity> wrapper = new EntityWrapper<SysDataSyncEntity>();
                            wrapper.setEntity(sysData);
                            sysDataSyncService.update(sysDataSyncEntity,wrapper);
                        }else{
                            System.out.println("-----------上级保存注册中心信息失败---------------");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("-----------同步注册中心失败---------------");
                    }
                }

                List<DeviceinfoEntity> deviceInfoList = deviceinfoService.selectList(null);//根据DeptId查询设备（参数是List<String>）
                SysDataSyncEntity sysDevice = findOneSysDataSync("deviceInfoSync");
                if (deviceInfoList != null && deviceInfoList.size() > 0) {
                    for (int i = 0; i < deviceInfoList.size(); i++) {
                        deviceIds.add(deviceInfoList.get(i).getDeviceid());
                        long lastModifyTime = deviceInfoList.get(i).getLastmodifytime().getTime();
                        long lastSyncTime = sysDevice.getLastsynctime().getTime();
                        if (lastModifyTime > lastSyncTime) {
                            deviceInfoList.get(i).setLastmodifytime(date);
                        } else {
                            deviceInfoList.remove(i);
                        }
                    }
                    if (deviceInfoList != null && deviceInfoList.size() > 0) {
                        /*System.out.println("---发送设备基本信息数据"+deviceInfoList.size()+"---");*/
                        requestParam.set("deviceInfoSync", JSON.toJSONString(deviceInfoList));
                        try {
                            SynchroDataFormWork result = restTemplate.postForObject("http://" + parentUrl + ":8080/renren-admin/device/deviceinfo/devlist", requestParam, SynchroDataFormWork.class);
                            if (result!=null && Boolean.valueOf(result.getCenterInfoSync())){
                                SysDataSyncEntity sysData = new SysDataSyncEntity();
                                sysData.setSyncType("deviceInfoSync");
                                EntityWrapper<SysDataSyncEntity> wrapper = new EntityWrapper<SysDataSyncEntity>();
                                wrapper.setEntity(sysData);
                                sysDataSyncService.update(sysDataSyncEntity,wrapper);
                            }else{
                                System.out.println("-----------上级保存设备基础信息失败---------------");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println("-----------同步设备基础失败---------------");
                        }
                    }
                }
                List<DevicedyninfonewEntity> devicedyninfonewEntityList = devicedyninfonewService.selectList(null);//根据deviceId查询设备最新数据（参数List<String>）
                SysDataSyncEntity sysDeviceInfo = findOneSysDataSync("deviceDynSync");
                if (devicedyninfonewEntityList != null && devicedyninfonewEntityList.size() > 0) {
                    for (int i = 0; i < devicedyninfonewEntityList.size(); i++) {
                        long lastModifyTime = devicedyninfonewEntityList.get(i).getLastmodifytime().getTime();
                        long lastSyncTime = sysDeviceInfo.getLastsynctime().getTime();
                        if (lastModifyTime > lastSyncTime) {
                            devicedyninfonewEntityList.get(i).setLastmodifytime(date);
                        } else {
                            devicedyninfonewEntityList.remove(i);
                        }
                    }
                    if (devicedyninfonewEntityList != null && devicedyninfonewEntityList.size() > 0) {
                        requestParam.set("deviceDynSync", JSON.toJSONString(devicedyninfonewEntityList));
                        try {
                            SynchroDataFormWork result = restTemplate.postForObject("http://" + parentUrl + ":8080/renren-admin/device/deviceinfo/devlist", requestParam, SynchroDataFormWork.class);
                            if (result!=null && Boolean.valueOf(result.getCenterInfoSync())){
                                SysDataSyncEntity sysData = new SysDataSyncEntity();
                                sysData.setSyncType("deviceDynSync");
                                EntityWrapper<SysDataSyncEntity> wrapper = new EntityWrapper<SysDataSyncEntity>();
                                wrapper.setEntity(sysData);
                                sysDataSyncService.update(sysDataSyncEntity,wrapper);
                            }else{
                                System.out.println("-----------上级保存设备动态信息失败---------------");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println("-----------同步设备动态失败---------------");
                        }
                    }
                }
            }
        }

    }
    private SysDataSyncEntity findOneSysDataSync(String sysType){
        return sysDataSyncService.findOneSysDataSync(sysType);//根据sync_type查询同步条件
    }

}