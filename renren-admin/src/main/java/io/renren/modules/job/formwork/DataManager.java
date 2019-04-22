package io.renren.modules.job.formwork;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.device.service.DevicedyninfonewService;
import io.renren.modules.device.service.DeviceinfoService;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dataManager")
public class DataManager {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private DeviceinfoService deviceinfoService;

    @Autowired
    private DevicedyninfonewService devicedyninfonewService;

    @RequestMapping("/consumeSynchroData")
    @ResponseBody
    public SynchroDataFormWork consumeData(SynchroDataFormWork synchroDataFormWork){

        SynchroDataFormWork synchroDataForm = new SynchroDataFormWork();
        try {
            if (synchroDataFormWork!=null){
                String centerInfo = synchroDataFormWork.getCenterInfoSync();
                if (centerInfo!=null && !"".equals(centerInfo)){
                    JSONArray centerInfoJsonArr = JSONArray.parseArray(centerInfo);
                    List<SysDeptEntity> sysDeptEntityList = centerInfoJsonArr.toJavaList(SysDeptEntity.class);
                    for (SysDeptEntity sysDeptEntity:sysDeptEntityList) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("dept_id", sysDeptEntity.getDeptId());
                        SysDeptEntity sysDeptEnti = sysDeptService.selectByMap(map).get(0);//根据同步上来数据的DeptId查询是否存在这条数据
                        sysDeptEntity.setIsCurrent(0);
                        if (sysDeptEnti!=null){//如果存在，说明之前同步过，数据后期有做修改。将库中存在的数据修改成同步上来的数据
                            SysDeptEntity sysDept=new SysDeptEntity();
                            sysDept.setDeptId(sysDeptEntity.getDeptId());
                            EntityWrapper<SysDeptEntity> wrapper=new EntityWrapper<>();
                            wrapper.setEntity(sysDept);
                            sysDeptService.update(sysDeptEntity,wrapper);//
                        }else{//不存在，说明这条数据是新数据，第一次同步过来。新增入库
                            sysDeptService.insert(sysDeptEntity);
                        }
                    }
                }
                String deviceInfoSync =  synchroDataFormWork.getDeviceInfoSync();
                if (deviceInfoSync!=null && !"".equals(deviceInfoSync)){
                    JSONArray centerInfoJsonArr = JSONArray.parseArray(deviceInfoSync);
                    List<DeviceinfoEntity> deviceinfoEntityEntityList = centerInfoJsonArr.toJavaList(DeviceinfoEntity.class);
                    for (DeviceinfoEntity deviceinfoEntity:deviceinfoEntityEntityList) {
                        deviceinfoEntity.setAgentnum(null);
                        Map<String, Object> map = new HashMap<>();
                        map.put("DeviceID", deviceinfoEntity.getDeviceid());

                        DeviceinfoEntity deviceinfoEnti = deviceinfoService.selectByMap(map).get(0);//根据同步上来的数据的DeviceId查询是否存在这条数据
                        if (deviceinfoEnti!=null){//如果存在，说明之前同步过，数据后期有做修改。将库中存在的数据修改成同步上来的数据
                            DeviceinfoEntity deptEntity=new DeviceinfoEntity();
                            deptEntity.setDeviceid(deviceinfoEntity.getDeviceid());
                            EntityWrapper<DeviceinfoEntity> wrapper=new EntityWrapper<>();
                            wrapper.setEntity(deptEntity);
                            deviceinfoService.update(deviceinfoEntity,wrapper);
                        }else{//不存在，说明这条数据是新数据，第一次同步过来。新增入库
                            deviceinfoService.insert(deviceinfoEntity);
                        }
                    }
                }

                String deviceDynSync = synchroDataFormWork.getDeviceDynSync();
                if (deviceDynSync!=null&&!"".equals(deviceDynSync)){
                    JSONArray centerInfoJsonArr = JSONArray.parseArray(deviceInfoSync);
                    List<DevicedyninfonewEntity> devicedyninfoEntitieList = centerInfoJsonArr.toJavaList(DevicedyninfonewEntity.class);
                    for (DevicedyninfonewEntity devicedyninfoEntitie:devicedyninfoEntitieList){
                        Map<String, Object> map = new HashMap<>();
                        map.put("DevID", devicedyninfoEntitie.getDevid());
                        DevicedyninfonewEntity devicedyninfonewEntity = devicedyninfonewService.selectByMap(map).get(0);//根据同步上来的数据的devId查询是否存在这条数据
                        if (devicedyninfonewEntity!=null){//如果存在，说明之前同步过，数据后期有做修改。将库中存在的数据修改成同步上来的数据

                            DevicedyninfonewEntity devicedyn=new DevicedyninfonewEntity();
                            devicedyn.setDevid(devicedyninfoEntitie.getDevid());
                            EntityWrapper<DevicedyninfonewEntity> wrapper=new EntityWrapper<>();
                            wrapper.setEntity(devicedyn);
                            devicedyninfonewService.update(devicedyninfoEntitie,wrapper);
                        }else{//不存在，说明这条数据是新数据，第一次同步过来。新增入库
                            devicedyninfonewService.insert(devicedyninfoEntitie);
                        }
                    }
                }
            }
            synchroDataForm.setCenterInfoSync("true");
        }catch (Exception e){
            synchroDataForm.setCenterInfoSync("false");
        }
        return synchroDataForm;
    }
}

