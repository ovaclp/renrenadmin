package io.renren.modules.device.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.config.AddressipRecord;
import io.renren.common.config.MinaClientConfig;
import io.renren.common.utils.CodeUtils;
import io.renren.common.utils.InitDataListener;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.device.entity.DeviceEntity;
import io.renren.modules.device.entity.DevicedyninfoEntity;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.service.DevicedyninfoService;
import io.renren.modules.device.service.DevicedyninfonewService;
import io.renren.modules.job.formwork.SynchroDataFormWork;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.device.service.DeviceinfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.client.RestTemplate;


/**
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
@Api(value = "/DeviceinfoController",tags = "deviceinfo",description = "关于设备基本信息的操作接口")
@RestController
@RequestMapping("device/deviceinfo")
public class DeviceinfoController {
    @Autowired
    private DeviceinfoService deviceinfoService;


    @Autowired
    private DevicedyninfoService devicedyninfoService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private DevicedyninfonewService devicedyninfonewService;

   /* @Autowired
    private MinaClientConfig minaClientConfig;*/


    /**
     * 列表
     */
    @ApiOperation(value = "设备基本信息列表",notes = "根据params参数来获取设备基本信息")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("device:deviceinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = deviceinfoService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 所有设备列表
     */
    @RequestMapping("/alllist")
    public List<DeviceEntity> alllist() {
        List<DeviceEntity> menuList = deviceinfoService.queryDevice();

        return menuList;
    }


    /**
     * 信息
     */
    @ApiOperation(value = "获取设备基本信息记录",notes = "根据行记录号recnumber获取设备基本信息")
    @RequestMapping(value = "/info/{recnumber}",method = RequestMethod.GET)
    @RequiresPermissions("device:deviceinfo:info")
    public R info(@PathVariable("recnumber") Integer recnumber) {
        System.out.println("选择行号:" + recnumber);
        DeviceinfoEntity deviceinfo = deviceinfoService.selectById(recnumber);
        //System.out.println("dierdi:"+deviceinfo.getRecnumber());
        return R.ok().put("deviceinfo", deviceinfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:deviceinfo:register")
    public R save(@RequestBody DeviceinfoEntity deviceinfo) {

        Date date = new Date();
        deviceinfo.setRegdate(date);
        Timestamp timestamp = new Timestamp(date.getTime());
        deviceinfo.setLastmodifytime(timestamp);
        deviceinfoService.insert(deviceinfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:deviceinfo:update")
    public R update(@RequestBody DeviceinfoEntity deviceinfo) {
        ValidatorUtils.validateEntity(deviceinfo);
        deviceinfoService.updateAllColumnById(deviceinfo);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:deviceinfo:delete")
    public R delete(@RequestBody Integer[] recnumbers) {
        deviceinfoService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }


    /**
     * 监控中心间基本信息被调接口：从代理查询设备的基本信息
     * @param deviceid
     * @return
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value = "远程查询设备最新基本信息",notes = "跨级调用设备信息中心间接口")
    @ApiImplicitParam( name ="deviceid",value = "设备唯一标识")
    @RequestMapping(value = "/queryonedev/{deviceid}")
    public R queryonedev(@PathVariable("deviceid") String deviceid) throws UnsupportedEncodingException {

        //从代理获取基本信息
        DeviceinfoEntity deviceinfoEntity = deviceinfoService.queryallInfoById(deviceid);


        Date time= new Date();
        Timestamp timestamp=new Timestamp(time.getTime());
        deviceinfoEntity.setLastmodifytime(timestamp);

        Map<String,Object> map=new HashMap<>();
        map.put("DeviceID",deviceid);
        List<DeviceinfoEntity> list=deviceinfoService.selectByMap(map);
        if(list!=null&&list.size()>0){
            deviceinfoEntity.setRecnumber(list.get(0).getRecnumber());
        }
        deviceinfoService.updateById(deviceinfoEntity);

        //从设备基本信息表获取设备监控中心编号，并赋值到deviceinfoEntity对象上
        String sysNumber = deviceinfoService.devid_sysNumber(deviceid);
        deviceinfoEntity.setSysnumber(sysNumber);


        return R.ok().put("devinfo", deviceinfoEntity);

    }




    /**
     * 单条选中基本信息所在行
     * @param deviceinfo   前端选中所在行记录
     * @return
     */

    @ApiOperation(value = "查询设备最新基本信息",notes="根据设备唯一标识来查询设备最新基本信息")
    @ApiImplicitParam(name = "deviceinfo",value = "设备基本信息行记录")
    @RequestMapping(value = "/mes")
    @RequiresPermissions("device:deviceinfo:mes")
    public R mes(@RequestBody DeviceinfoEntity deviceinfo) throws UnsupportedEncodingException {


        Map<String,Object>  maper=new HashMap<>();
        maper.put("DevID",deviceinfo.getDeviceid());
        List<DevicedyninfoEntity> listnum=devicedyninfoService.selectByMap(maper);
        if(listnum.size()==0){
            return R.error().put("msg","设备不存在！");
        }
        Map<String,Object>  mapp=new HashMap<>();
        mapp.put("DevID",deviceinfo.getDeviceid());
        List<DevicedyninfonewEntity> list=devicedyninfonewService.selectByMap(mapp);
        if(list!=null&&list.size()>0){
            DevicedyninfonewEntity devicedyninfonewEntity=list.get(0);
            if(devicedyninfonewEntity.getDevstate().equals("1")|devicedyninfonewEntity.getDevstate().equals("2")){
                return  R.error().put("msg","设备离线或者已毙杀，无法查询！");

            }
        }



        ////判断设备是不是属于本级监管中心  iscurrent=0：不是 ；iscurrent=1：是
        String  iscurrent = deviceinfoService.is_Current(deviceinfo.getDeviceid());
        if (iscurrent!=null&&"1".equals(iscurrent)) {
            //是本级监管中心
            //从代理获取设备基本信息，执行主键更新策略
            DeviceinfoEntity deviceinfoEntity = deviceinfoService.queryallInfoById(deviceinfo.getDeviceid());
            Date time= new Date();
            Timestamp timestamp=new Timestamp(time.getTime());
            deviceinfoEntity.setLastmodifytime(timestamp);

            if(deviceinfoEntity!=null){
                deviceinfoEntity.setRecnumber(deviceinfo.getRecnumber());
                deviceinfoService.updateById(deviceinfoEntity);
                return R.ok();
            }else{
                return R.error().put("msg","查询失败！");
            }

        } else {
            //不是属于本级监管中心
            //查询设备指数监控中心的ip地址,中心的32位唯一标识
            List<Map<String, Object>> baseList = deviceinfoService.queryDev_ipid(deviceinfo.getDeviceid());
            String centerid = null;
            String ipaddress = null;
            if (baseList.size() == 1) {
                for (Map<String, Object> mapper : baseList) {
                    centerid = (String) mapper.get("center_id");
                    ipaddress = (String) mapper.get("ip_addr");
                }
            } else {
                return R.error().put("msg","查询设备监控中心id和ip失败！");
            }
            //构造restTemplete参数map
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("ip_address", ipaddress);
            map.add("devid", deviceinfo.getDeviceid());
            map.add("center_id", centerid);


            String urlparam="http://" + ipaddress + ":8080/renren-admin/device/deviceinfo/queryonedev/" + deviceinfo.getDeviceid();
            if(!CodeUtils.urlOnline(urlparam)){
                return R.error().put("msg","远程查询基本信息接口连接调用失败！") ;

            }
            //监控中心静态信息
            R r = restTemplate.postForObject("http://" + ipaddress + ":8080/renren-admin/device/deviceinfo/queryonedev/" + deviceinfo.getDeviceid(), map, R.class);
            LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>) r.get("devinfo");
            DeviceinfoEntity deviceinfoMinSet = new DeviceinfoEntity();
            deviceinfoMinSet.setDeviceid((String) linkedHashMap.get("deviceid"));
            deviceinfoMinSet.setDeviceipv4((String) linkedHashMap.get("deviceipv4"));
            deviceinfoMinSet.setSysnumber((String) linkedHashMap.get("sysnumber"));
            deviceinfoMinSet.setRecnumber(deviceinfo.getRecnumber());
            Date time = new Date();
            Timestamp timestamp = new Timestamp(time.getTime());
            deviceinfoMinSet.setLastmodifytime(timestamp);
            boolean  flag=deviceinfoService.updateById(deviceinfoMinSet);
            if(flag){
                return R.ok();
            }else{
                return R.error().put("msg","远程查询失败！");
            }

        }
    }


    /**
     * 所有设备静态信息列表
     */
    @ApiOperation(value = "设备信息,中心信息同步",notes = "设备信息监控平台信息同步")
    @ApiImplicitParam(name="synchroDataFormWork",value = "同步的基本信息类型")
    @RequestMapping(value = "/devlist")
    public SynchroDataFormWork devlist(SynchroDataFormWork synchroDataFormWork) {

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

                        List<DeviceinfoEntity> deviceinfoEnti = deviceinfoService.selectByMap(map);//根据同步上来的数据的DeviceId查询是否存在这条数据
                        if (deviceinfoEnti!=null&&deviceinfoEnti.size()>0){//如果存在，说明之前同步过，数据后期有做修改。将库中存在的数据修改成同步上来的数据
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
                    JSONArray centerInfoJsonArr = JSONArray.parseArray(deviceDynSync);
                    List<DevicedyninfonewEntity> devicedyninfoEntitieList = centerInfoJsonArr.toJavaList(DevicedyninfonewEntity.class);
                    for (DevicedyninfonewEntity devicedyninfoEntitie:devicedyninfoEntitieList){
                        DevicedyninfoEntity one =new DevicedyninfoEntity();
                        one.setDevid(devicedyninfoEntitie.getDevid());
                        one.setEncservicestate(devicedyninfoEntitie.getEncservicestate());
                        one.setKeyversion(devicedyninfoEntitie.getKeyversion());
                        one.setKeyupdatestate(devicedyninfoEntitie.getKeyupdatestate());
                        one.setTunnelconnectivity(devicedyninfoEntitie.getTunnelconnectivity());
                        one.setDevipstate(devicedyninfoEntitie.getDevipstate());
                        one.setAlgrescontion(devicedyninfoEntitie.getAlgrescontion());
                        one.setKeyrescondition(devicedyninfoEntitie.getKeyrescondition());
                        one.setDevstate(devicedyninfoEntitie.getDevstate());
                        one.setKernelprogcheck(devicedyninfoEntitie.getKernelprogcheck());
                        one.setRngCheck(devicedyninfoEntitie.getRngCheck());
                        one.setNotice(devicedyninfoEntitie.getNotice());
                        one.setLastmodifytime(devicedyninfoEntitie.getLastmodifytime());
                        devicedyninfoService.insert(one);

                        Map<String, Object> map = new HashMap<>();
                        map.put("DevID", devicedyninfoEntitie.getDevid());
                        List<DevicedyninfonewEntity> devicedyninfonewEntity = devicedyninfonewService.selectByMap(map);//根据同步上来的数据的devId查询是否存在这条数据
                        if (devicedyninfonewEntity!=null&&devicedyninfonewEntity.size()>0){//如果存在，说明之前同步过，数据后期有做修改。将库中存在的数据修改成同步上来的数据
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
            e.printStackTrace();
            synchroDataForm.setCenterInfoSync("false");
        }
        return synchroDataForm;
    }


}

