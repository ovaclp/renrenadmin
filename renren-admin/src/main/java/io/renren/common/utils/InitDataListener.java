package io.renren.common.utils;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.device.entity.DevicedyninfoEntity;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.service.DevicedyninfoService;

import io.renren.modules.device.service.DevicedyninfonewService;
import io.renren.modules.device.service.DeviceinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


@Component
//@Order(1)
public class InitDataListener implements ApplicationRunner {




    @Resource
    private DeviceinfoService deviceinfoService;
    @Autowired
    public DevicedyninfonewService devicedyninfonewService;

    private static List<DevicedyninfonewEntity> listnew=new ArrayList<DevicedyninfonewEntity>() ;

    public  static  List<DevicedyninfonewEntity> getListnew() {
        return listnew;
    }

    static  boolean flag=false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        myTimer();
    }


    public  void  myTimer(){


        Timer timer=new Timer();
        timer.schedule(new TimerTask() {


            @Override
            public void run() {

                if(flag) {
                    if (listnew.size() > 0) {
                        for (DevicedyninfonewEntity tmp : listnew) {
                           System.out.println("1111---"+tmp.toString());
                        }
                        for (DevicedyninfonewEntity user : listnew) {
                           //是否本级设备
                            String  iscurrent = deviceinfoService.is_Current(user.getDevid());
                            if (iscurrent!=null&&"1".equals(iscurrent)) {
                                if("0".equals(user.getDevstate())|| "1".equals(user.getDevstate())){
                                    if (user.getCountnum() == 0) {
                                       DevicedyninfonewEntity devicedyninfonewEntity = new DevicedyninfonewEntity();
                                       devicedyninfonewEntity.setDevstate("1");
                                       DevicedyninfonewEntity devicenewEntity = new DevicedyninfonewEntity();
                                       devicenewEntity.setDevid(user.getDevid());
                                       EntityWrapper<DevicedyninfonewEntity> wrapper = new EntityWrapper<>();
                                       wrapper.setEntity(devicenewEntity);
                                       wrapper.orderBy("LastModifyTime",false).last("Limit 1");
                                       devicedyninfonewService.update(devicedyninfonewEntity, wrapper);
                                    }
                                }//若是毙杀不处理

                            }

                        }
                        for (DevicedyninfonewEntity devicedyninfonew: listnew) {
                           devicedyninfonew.setCountum(0);
                        }

                    }
                    flag=true;
                }else{
                    List<DevicedyninfonewEntity> list=(ArrayList)devicedyninfonewService.selectList(null);
                    if(list!=null&&list.size()>0){
                        listnew.clear();
                        for(int i=0;i<list.size();i++){
                            DevicedyninfonewEntity devicedyninfonewEntity=list.get(i);
                            listnew.add(devicedyninfonewEntity);
                        }
                        List<DevicedyninfonewEntity>  devicedyninfonewEntityList = devicedyninfonewService.queryDeviceBydevid();
                        for (DevicedyninfonewEntity devicedyninfonew: devicedyninfonewEntityList) {
                            devicedyninfonew.setCountum(0);
                        }
                        listnew = devicedyninfonewEntityList;
                        for (DevicedyninfonewEntity tmp : listnew) {
                           System.out.println("内存表初始化---"+tmp.toString());
                        }
                        flag=true;

                    }else{
                        flag=true;

                    }
                }

            //8*60*1000 五分钟
            }
        },0,600000);
    }



}
