package io.renren.modules.device.controller;

import java.sql.Timestamp;
import java.util.*;
import java.io.UnsupportedEncodingException;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.CodeUtils;
import io.renren.common.utils.InitDataListener;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.service.DevicedyninfonewService;
import io.renren.modules.device.service.DeviceinfoService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.device.entity.DevicedyninfoEntity;
import io.renren.modules.device.service.DevicedyninfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.client.RestTemplate;


/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
@Api(value = "/DevicedyninfoController",tags = "devicedyninfo",description = "关于设备动态信息的操作方法接口")
@RestController
@RequestMapping("device/devicedyninfo")
public class DevicedyninfoController {
    @Autowired
    private DevicedyninfoService devicedyninfoService;


    @Autowired
    private DevicedyninfonewService devicedyninfonewService;
    @Autowired
    private DeviceinfoService deviceinfoService;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SysDeptService sysDeptService;


    /**
     *
     * @param oldone 原数据库的设备devid最新的状态信息
     * @param newone 代理查询后的状态信息
     * @return
     */
    public boolean compareDyninfo(DevicedyninfonewEntity oldone,DevicedyninfoEntity newone){
        if(oldone.getEncservicestate()!=newone.getEncservicestate()) {
            return  false;
        }
        if(oldone.getKeyupdatestate()!=newone.getKeyupdatestate()) {
            return  false;
        }
        if(oldone.getTunnelconnectivity()!=newone.getTunnelconnectivity()) {
            return  false;
        }
        if(oldone.getDevipstate()!=newone.getDevipstate()) {
            return  false;
        }if(oldone.getRngCheck()!=newone.getRngCheck()) {
            return  false;
        }if(oldone.getKernelprogcheck()!=newone.getKernelprogcheck()) {
            return  false;
        }if(oldone.getAlgrescontion()!=newone.getAlgrescontion()) {
            return  false;
        }if(oldone.getKeyrescondition()!=newone.getKeyrescondition()) {
            return false;
        }

        return true;

    }

    /**
     * 列表
     */
    @ApiOperation(value = "设备动态信息列表",notes = "根据params参数来获取设备动态信息")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("device:devicedyninfo:list")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page = devicedyninfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "获取设备动态信息记录",notes = "根据行记录号recnumber获取设备动态信息")
    @RequestMapping(value = "/info/{recnumber}",method = RequestMethod.GET)
    //@RequiresPermissions("device:devicedyninfo:info")
    public R info(@PathVariable("recnumber") Integer recnumber){
        DevicedyninfoEntity devicedyninfo = devicedyninfoService.selectById(recnumber);
        return R.ok().put("devicedyninfo", devicedyninfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:devicedyninfo:save")
    public R save(@RequestBody DevicedyninfoEntity devicedyninfo){
        devicedyninfoService.insert(devicedyninfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:devicedyninfo:update")
    public R update(@RequestBody DevicedyninfoEntity devicedyninfo){
        ValidatorUtils.validateEntity(devicedyninfo);
        devicedyninfoService.updateAllColumnById(devicedyninfo);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:devicedyninfo:delete")
    public R delete(@RequestBody Integer[] recnumbers){
        devicedyninfoService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }



    /**
     * 监控中心间动态信息被调接口：从代理查询设备的状态信息
     * @param deviceid
     * @return
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value = "远程查询设备最新状态信息",notes = "跨级调用设备信息中心间接口")
    @ApiImplicitParam(name = "deviceid",value = "设备唯一标识")
    @RequestMapping(value ="/queryonedevdyn/{deviceid}",method = RequestMethod.GET)
    public  R  queryonedevdyn(@PathVariable("deviceid") String  deviceid) throws UnsupportedEncodingException  {

        DevicedyninfoEntity devicedyninfoEntity=devicedyninfoService.queryallInfoById(deviceid);

        //查询数据库devid的最新的设备状态，并赋值到devicedyninfoEntity对象
        String devState=devicedyninfoService.devState(deviceid).getDevstate();
        devicedyninfoEntity.setDevstate(devState);
        //插入数本地据库
        Date time= new Date();
        Timestamp timestamp=new Timestamp(time.getTime());
        devicedyninfoEntity.setLastmodifytime(timestamp);
        devicedyninfoService.insert(devicedyninfoEntity);
        //
        DevicedyninfonewEntity devicenew=new DevicedyninfonewEntity();
        devicenew.setDevid(deviceid);
        EntityWrapper<DevicedyninfonewEntity> wrappernew=new EntityWrapper<>();
        wrappernew.setEntity(devicenew);

        DevicedyninfonewEntity devicedyninfonewEntity=new DevicedyninfonewEntity();
        devicedyninfonewEntity.setDevid(deviceid);
        devicedyninfonewEntity.setEncservicestate(devicedyninfoEntity.getEncservicestate());
        devicedyninfonewEntity.setKeyversion(devicedyninfoEntity.getKeyversion());
        devicedyninfonewEntity.setKeyupdatestate(devicedyninfoEntity.getKeyupdatestate());
        devicedyninfonewEntity.setTunnelconnectivity(devicedyninfoEntity.getTunnelconnectivity());
        devicedyninfonewEntity.setDevipstate(devicedyninfoEntity.getDevipstate());
        devicedyninfonewEntity.setAlgrescontion(devicedyninfoEntity.getAlgrescontion());
        devicedyninfonewEntity.setKeyrescondition(devicedyninfoEntity.getKeyrescondition());
        devicedyninfonewEntity.setRngCheck(devicedyninfoEntity.getRngCheck());
        devicedyninfonewEntity.setKernelprogcheck(devicedyninfoEntity.getKernelprogcheck());
        devicedyninfonewEntity.setLastmodifytime(timestamp);
        devicedyninfonewService.update(devicedyninfonewEntity,wrappernew);


        return   R.ok().put("dyninfo",devicedyninfoEntity);

    }

    /**
     * 单条选中状态信息所在行
     * @param deviceinfo   前端选中所在行记录
     * @return
     */

    @ApiOperation(value = "查询设备最新状态信息",notes = "根据设备唯一标识来查询设备最新状态信息")
    @ApiImplicitParam(name = "deviceinfo",value = "设备状态信息行记录")
    @RequestMapping(value = "/mes",method = RequestMethod.GET)
    //@RequiresPermissions("device:devicedyninfo:mes")
    public R mes(@RequestBody DevicedyninfonewEntity deviceinfo){

        Map<String,Object>  maper=new HashMap<>();
        maper.put("DevID",deviceinfo.getDevid());
        List<DevicedyninfonewEntity> list=devicedyninfonewService.selectByMap(maper);
        if(list!=null&&list.size()>0){
            DevicedyninfonewEntity devicedyninfonewEntity=list.get(0);
            if(devicedyninfonewEntity.getDevstate().equals("1")|devicedyninfonewEntity.getDevstate().equals("2")){
                return  R.error().put("msg","设备离线或者已毙杀，无法查询！");

            }
        }

        //判断设备是不是属于本级监管中心  iscurrent=0：不是 ；iscurrent=1：是
        String iscurrent=deviceinfoService.is_Current(deviceinfo.getDevid());
        if(iscurrent!=null&&iscurrent.equals("1")){
            //是本级监管中心
            //从代理获取设备状态信息
            DevicedyninfoEntity devicedyninfoEntity=devicedyninfoService.queryallInfoById(deviceinfo.getDevid());
            //判断是更新还是插入(信息一致：更新；信息不同：插入)
            Boolean flag=false;
            if(!this.compareDyninfo(deviceinfo,devicedyninfoEntity)){
                //设置设备状态信息,执行插入操作
                devicedyninfoEntity.setDevstate("0");
                Date time=new Date();
                Timestamp timestamp=new Timestamp(time.getTime());
                devicedyninfoEntity.setLastmodifytime(timestamp);
                devicedyninfoService.insert(devicedyninfoEntity);

                DevicedyninfonewEntity devicenew=new DevicedyninfonewEntity();
                devicenew.setDevid(deviceinfo.getDevid());
                EntityWrapper<DevicedyninfonewEntity> wrappernew=new EntityWrapper<>();
                wrappernew.setEntity(devicenew);

                DevicedyninfonewEntity devicedyninfonewEntity=new DevicedyninfonewEntity();
                devicedyninfonewEntity.setDevid(deviceinfo.getDevid());
                devicedyninfonewEntity.setEncservicestate(devicedyninfoEntity.getEncservicestate());
                devicedyninfonewEntity.setKeyversion(devicedyninfoEntity.getKeyversion());
                devicedyninfonewEntity.setKeyupdatestate(devicedyninfoEntity.getKeyupdatestate());
                devicedyninfonewEntity.setTunnelconnectivity(devicedyninfoEntity.getTunnelconnectivity());
                devicedyninfonewEntity.setDevipstate(devicedyninfoEntity.getDevipstate());
                devicedyninfonewEntity.setAlgrescontion(devicedyninfoEntity.getAlgrescontion());
                devicedyninfonewEntity.setKeyrescondition(devicedyninfoEntity.getKeyrescondition());
                devicedyninfonewEntity.setRngCheck(devicedyninfoEntity.getRngCheck());
                devicedyninfonewEntity.setKernelprogcheck(devicedyninfoEntity.getKernelprogcheck());
                devicedyninfonewEntity.setLastmodifytime(timestamp);
                flag=devicedyninfonewService.update(devicedyninfonewEntity,wrappernew);
            }
            if(flag){
                return R.ok();
            }else{
                return R.error().put("msg","查询失败！");

            }
        }else{
            //不是属于本级监管中心
            //查询设备指数监控中心的ip地址,中心的32位唯一标识
            List<Map<String, Object>> baseList=deviceinfoService.queryDev_ipid(deviceinfo.getDevid());
            String centerid=null;
            String ipaddress=null;
            if(baseList.size()==1){
                for (Map<String, Object> mapper : baseList) {
                    centerid=(String)mapper.get("center_id");
                    ipaddress=(String)mapper.get("ip_addr");
                }

            }else{
                return R.error().put("msg","查询设备所属监控中心信息失败！");
            }
            //构造restTemplete参数map
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("ip_address",ipaddress);
            map.add("devid", deviceinfo.getDevid());
            map.add("center_id",centerid);


            String urlparam="http://"+ipaddress+":8080/renren-admin/device/devicedyninfo/queryonedevdyn/"+deviceinfo.getDevid();
            if(!CodeUtils.urlOnline(urlparam)){
                return R.error().put("msg","远程查询状态信息接口连接调用失败！") ;

            }
            R r=restTemplate.postForObject("http://"+ipaddress+":8080/renren-admin/device/devicedyninfo/queryonedevdyn/"+deviceinfo.getDevid(),map,R.class);
            LinkedHashMap<String,Object>  linkedHashMap=(LinkedHashMap<String,Object>)r.get("dyninfo");
            //远程调用遵循最小集原则,按照主键更新策略

            DevicedyninfoEntity devicedyn=new DevicedyninfoEntity();

            devicedyn.setDevid((String)linkedHashMap.get("devid"));
            devicedyn.setEncservicestate((String)linkedHashMap.get("encservicestate"));
            devicedyn.setDevstate((String)linkedHashMap.get("devstate"));
            devicedyn.setAlgrescontion(deviceinfo.getAlgrescontion());
            devicedyn.setKeyversion(deviceinfo.getKeyversion());
            devicedyn.setKernelprogcheck(deviceinfo.getKernelprogcheck());
            devicedyn.setRngCheck(deviceinfo.getRngCheck());
            devicedyn.setDevipstate(deviceinfo.getDevipstate());
            devicedyn.setKeyupdatestate(deviceinfo.getKeyupdatestate());
            devicedyn.setTunnelconnectivity(deviceinfo.getTunnelconnectivity());
            devicedyn.setKeyrescondition(deviceinfo.getKeyrescondition());

            Date time=new Date();
            Timestamp timestamp=new Timestamp(time.getTime());
            devicedyn.setLastmodifytime(timestamp);

            devicedyninfoService.insert(devicedyn);


            DevicedyninfonewEntity devicedyninfonewEntity=new DevicedyninfonewEntity();
            devicedyninfonewEntity.setDevid((String)linkedHashMap.get("devid"));
            devicedyninfonewEntity.setEncservicestate((String)linkedHashMap.get("encservicestate"));
            devicedyninfonewEntity.setDevstate((String)linkedHashMap.get("devstate"));
            devicedyninfonewEntity.setAlgrescontion(deviceinfo.getAlgrescontion());
            devicedyninfonewEntity.setKeyversion(deviceinfo.getKeyversion());
            devicedyninfonewEntity.setKernelprogcheck(deviceinfo.getKernelprogcheck());
            devicedyninfonewEntity.setRngCheck(deviceinfo.getRngCheck());
            devicedyninfonewEntity.setDevipstate(deviceinfo.getDevipstate());
            devicedyninfonewEntity.setKeyupdatestate(deviceinfo.getKeyupdatestate());
            devicedyninfonewEntity.setTunnelconnectivity(deviceinfo.getTunnelconnectivity());
            devicedyninfonewEntity.setKeyrescondition(deviceinfo.getKeyrescondition());
            devicedyninfonewEntity.setLastmodifytime(timestamp);


            DevicedyninfonewEntity devicenew=new DevicedyninfonewEntity();
            devicenew.setDevid(deviceinfo.getDevid());
            EntityWrapper<DevicedyninfonewEntity> wrappernew=new EntityWrapper<>();
            wrappernew.setEntity(devicenew);
            Boolean flag=devicedyninfonewService.update(devicedyninfonewEntity,wrappernew);

            if(flag){
                return  R.ok();
            }else{
                return R.error().put("msg","查询失败！");
            }


        }
    }


    /**
     * 监控中心间动态信息被调接口：从代理查询设备的状态信息
     * @param deviceid
     * @return
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value = "毙杀设备接口",notes = "远程毙杀设备接口")
    @ApiImplicitParam(name = "deviceid",value = "设备唯一标识")
    @RequestMapping(value ="/killReturn/{deviceid}",method = RequestMethod.GET)
    public  R  killReturn(@PathVariable("deviceid") String  deviceid) throws UnsupportedEncodingException  {

        Map<String,Object>  maper=new HashMap<>();
        maper.put("devid",deviceid);
        List<DevicedyninfonewEntity> listdyn=devicedyninfonewService.selectByMap(maper);
        if(listdyn!=null&&listdyn.size()>0){
            DevicedyninfonewEntity devicedyninfonewEntity=listdyn.get(0);
            if(devicedyninfonewEntity.getDevstate().equals("1")|devicedyninfonewEntity.getDevstate().equals("2")){
                return  R.error().put("msg","该设备已毙杀或者离线");
            }
        }
        DevicedyninfoEntity devicedyninfoEntity=new DevicedyninfoEntity();
        //查询毙杀策略   0:自动    1:通知
        int killstrategy=deviceinfoService.killStrategy(deviceid);
        //查询设备状态
        DevicedyninfonewEntity devstate=devicedyninfonewService.queryDevnewState(deviceid);


        if((0==killstrategy)&&devstate.getDevstate().equals("0")){
            if(devicedyninfoService.KilldevById(deviceid)){
                //获取最新消息
                devicedyninfoEntity.setDevid(devstate.getDevid());
                devicedyninfoEntity.setEncservicestate(devstate.getEncservicestate());
                devicedyninfoEntity.setDevstate("2");
                devicedyninfoEntity.setAlgrescontion(devstate.getAlgrescontion());
                devicedyninfoEntity.setKeyversion(devstate.getKeyversion());
                devicedyninfoEntity.setKernelprogcheck(devstate.getKernelprogcheck());
                devicedyninfoEntity.setRngCheck(devstate.getRngCheck());
                devicedyninfoEntity.setDevipstate(devstate.getDevipstate());
                devicedyninfoEntity.setKeyupdatestate(devstate.getKeyupdatestate());
                devicedyninfoEntity.setTunnelconnectivity(devstate.getTunnelconnectivity());
                devicedyninfoEntity.setKeyrescondition(devstate.getKeyrescondition());
                DevicedyninfoEntity deviceEntity=new DevicedyninfoEntity();
                deviceEntity.setDevid(deviceid);

                DevicedyninfonewEntity devicedyninfonewEntity=new DevicedyninfonewEntity();
                devicedyninfonewEntity.setDevstate("2");
                DevicedyninfonewEntity devicenewEntity=new DevicedyninfonewEntity();
                devicenewEntity.setDevid(deviceid);
                EntityWrapper<DevicedyninfonewEntity> wrappernew=new EntityWrapper<>();
                wrappernew.setEntity(devicenewEntity);
                Date time=new Date();
                Timestamp timestamp=new Timestamp(time.getTime());
                devicedyninfoEntity.setLastmodifytime(timestamp);
                devicedyninfonewEntity.setLastmodifytime(timestamp);
                devicedyninfoService.insert(devicedyninfoEntity);
                devicedyninfonewService.update(devicedyninfonewEntity,wrappernew);

                InitDataListener initDataListener=new InitDataListener();
                List<DevicedyninfonewEntity> initlist=initDataListener.getListnew();
                for (DevicedyninfonewEntity unit:initlist){
                    if(deviceid.equals(unit.getDevid())){
                        unit.setDevstate("2");
                        unit.setCountum(0);

                    }
                }

            }

        }else {
            //返回毙杀通知
            devicedyninfoEntity.setNotice("待毙杀");
            DevicedyninfonewEntity devicedyninfonewEntity=new DevicedyninfonewEntity();
            devicedyninfonewEntity.setDevid(deviceid);
            devicedyninfonewEntity.setNotice("待毙杀");

            DevicedyninfonewEntity devicenewEntity=new DevicedyninfonewEntity();
            devicenewEntity.setDevid(deviceid);
            EntityWrapper<DevicedyninfonewEntity> wrappernew=new EntityWrapper<>();
            wrappernew.setEntity(devicenewEntity);
            Date time=new Date();
            Timestamp timestamp=new Timestamp(time.getTime());
            devicedyninfonewEntity.setLastmodifytime(timestamp);
            devicedyninfonewService.update(devicedyninfonewEntity,wrappernew);

            DevicedyninfoEntity devicedyninf=new DevicedyninfoEntity();
            devicedyninf.setNotice("待毙杀");
            Map<String,Object> map=new HashMap<>();
            map.put("devid",deviceid);
            List<DevicedyninfonewEntity> list=devicedyninfonewService.selectByMap(map);
            DevicedyninfonewEntity devicedynnew=new DevicedyninfonewEntity();
            if(list!=null&&list.size()>0){
                devicedynnew=list.get(0);
            }
            devicedyninf.setDevid(deviceid);
            devicedyninf.setEncservicestate(devicedynnew.getEncservicestate());
            devicedyninf.setDevstate(devicedynnew.getDevstate());
            devicedyninf.setAlgrescontion(devicedynnew.getAlgrescontion());
            devicedyninf.setKeyversion(devicedynnew.getKeyversion());
            devicedyninf.setKernelprogcheck(devicedynnew.getKernelprogcheck());
            devicedyninf.setRngCheck(devicedynnew.getRngCheck());
            devicedyninf.setDevipstate(devicedynnew.getDevipstate());
            devicedyninf.setKeyupdatestate(devicedynnew.getKeyupdatestate());
            devicedyninf.setTunnelconnectivity(devicedynnew.getTunnelconnectivity());
            devicedyninf.setKeyrescondition(devicedynnew.getKeyrescondition());
            devicedyninf.setLastmodifytime(timestamp);

            devicedyninfoService.insert(devicedyninf);

        }
        return   R.ok().put("killinfo",devicedyninfoEntity);

    }

    


    /**
     * 前端选中设备所在记录行，点击毙杀按钮
     * @param devid
     * @return
     */
    @ApiOperation(value = "毙杀设备",notes = "毙杀设备接口")
    @ApiImplicitParam(name = "devid",value = "设备唯一标识")
    @RequestMapping(value = "/kill",method = RequestMethod.GET)
    @RequiresPermissions("device:devicedyninfo:kill")
    public R kill( @RequestBody String devid){
        //查看此设备有无状态信息
        Map<String,Object>  maper=new HashMap<>();
        maper.put("devid",devid);
        List<DevicedyninfoEntity> listnum=devicedyninfoService.selectByMap(maper);
        if(listnum.size()==0){
            return R.error().put("msg","设备不存在！");
        }

        //先查设备是否被必杀(修改成获取最新状态信息)
        DevicedyninfonewEntity devstate=devicedyninfonewService.queryDevnewState(devid);
        //System.out.println("12133"+devstate.toString());
        if(devstate.getDevstate().equals("2")||devstate.getDevstate().equals("1")){
             return R.error().put("msg","设备离线或者已毙杀，无法查询！");
        }
        //判断设备是否为本级设备
        //判断设备是不是属于本级监管中心  iscurrent=0：不是 ；iscurrent=1：是
        String  iscurrent=deviceinfoService.is_Current(devid);
        if(iscurrent!=null&&"1".equals(iscurrent)){
            //是本级监管中心
            //从代理获取设备状态信息
            //毙杀策略等等
            if(devicedyninfoService.KilldevById(devid)){
                //状态信息历史记录表构造删选条件
                DevicedyninfoEntity devicedyninfoEntity=new DevicedyninfoEntity();
                devicedyninfoEntity.setDevid(devstate.getDevid());
                devicedyninfoEntity.setEncservicestate(devstate.getEncservicestate());
                devicedyninfoEntity.setDevstate("2");
                devicedyninfoEntity.setAlgrescontion(devstate.getAlgrescontion());
                devicedyninfoEntity.setKeyversion(devstate.getKeyversion());
                devicedyninfoEntity.setKernelprogcheck(devstate.getKernelprogcheck());
                devicedyninfoEntity.setRngCheck(devstate.getRngCheck());
                devicedyninfoEntity.setDevipstate(devstate.getDevipstate());
                devicedyninfoEntity.setKeyupdatestate(devstate.getKeyupdatestate());
                devicedyninfoEntity.setTunnelconnectivity(devstate.getTunnelconnectivity());
                devicedyninfoEntity.setKeyrescondition(devstate.getKeyrescondition());
                Date time=new Date();
                Timestamp timestamp=new Timestamp(time.getTime());
                devicedyninfoEntity.setLastmodifytime(timestamp);

                devicedyninfoService.insert(devicedyninfoEntity);


                DevicedyninfonewEntity devicedyninfonewEntity=new DevicedyninfonewEntity();
                devicedyninfonewEntity.setDevstate("2");
                DevicedyninfonewEntity devicenewEntity=new DevicedyninfonewEntity();
                devicenewEntity.setDevid(devid);
                EntityWrapper<DevicedyninfonewEntity> wrappernew=new EntityWrapper<>();
                wrappernew.setEntity(devicenewEntity);
                devicedyninfonewEntity.setLastmodifytime(timestamp);
                boolean flag=devicedyninfonewService.update(devicedyninfonewEntity,wrappernew);


                InitDataListener initDataListener=new InitDataListener();
                List<DevicedyninfonewEntity> initlist=initDataListener.getListnew();
                for (DevicedyninfonewEntity unit:initlist){
                    if(devid.equals(unit.getDevid())){
                        unit.setDevstate("2");
                        unit.setCountum(0);

                    }
                }
                if(flag){
                    return R.ok();
                }else{
                    return R.error().put("msg","毙杀失败！");
                }



            }else{
                DevicedyninfonewEntity devicedyninfonewEntity=new DevicedyninfonewEntity();
                devicedyninfonewEntity.setNotice("毙杀失败");
                DevicedyninfonewEntity devicenewEntity=new DevicedyninfonewEntity();
                devicenewEntity.setDevid(devid);
                EntityWrapper<DevicedyninfonewEntity> wrappernew=new EntityWrapper<>();
                wrappernew.setEntity(devicenewEntity);
                Date time=new Date();
                Timestamp timestamp=new Timestamp(time.getTime());
                devicedyninfonewEntity.setLastmodifytime(timestamp);
                devicedyninfonewService.update(devicedyninfonewEntity,wrappernew);


                DevicedyninfoEntity devicedyninfoEntity=new DevicedyninfoEntity();
                devicedyninfoEntity.setDevid(devstate.getDevid());
                devicedyninfoEntity.setEncservicestate(devstate.getEncservicestate());
                devicedyninfoEntity.setDevstate("2");
                devicedyninfoEntity.setAlgrescontion(devstate.getAlgrescontion());
                devicedyninfoEntity.setKeyversion(devstate.getKeyversion());
                devicedyninfoEntity.setKernelprogcheck(devstate.getKernelprogcheck());
                devicedyninfoEntity.setRngCheck(devstate.getRngCheck());
                devicedyninfoEntity.setDevipstate(devstate.getDevipstate());
                devicedyninfoEntity.setKeyupdatestate(devstate.getKeyupdatestate());
                devicedyninfoEntity.setTunnelconnectivity(devstate.getTunnelconnectivity());
                devicedyninfoEntity.setKeyrescondition(devstate.getKeyrescondition());
                devicedyninfoEntity.setNotice("毙杀失败");
                devicedyninfoEntity.setLastmodifytime(timestamp);

                devicedyninfoService.insert(devicedyninfoEntity);
                return R.error().put("msg","设备毙杀失败");



            }
        }else{
            //不是属于本级监管中心
            //查询设备指数监控中心的ip地址,中心的32位唯一标识
            List<Map<String, Object>> baseList=deviceinfoService.queryDev_ipid(devid);
            String centerid=null;
            String ipaddress=null;
            if(baseList.size()==1){
                for (Map<String, Object> mapper : baseList) {
                    centerid=(String)mapper.get("center_id");
                    ipaddress=(String)mapper.get("ip_addr");
                }
            }else{
                return  R.error();
            }
            //构造restTemplete参数map
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("ip_address",ipaddress);
            map.add("devid", devid);
            map.add("center_id",centerid);


            String urlparam="http://"+ipaddress+":8080/renren-admin/device/devicedyninfo/killReturn/"+devid;
            if(!CodeUtils.urlOnline(urlparam)){
                return R.error().put("msg","远程设备毙杀接口连接调用失败！") ;

            }
            R r=restTemplate.postForObject("http://"+ipaddress+":8080/renren-admin/device/devicedyninfo/killReturn/"+devid,map,R.class);
            LinkedHashMap<String,Object>  linkedHashMap=(LinkedHashMap<String,Object>)r.get("killinfo");
              //远程调用遵循最小集原则,按照主键更新策略

            String note=(String)linkedHashMap.get("notice");
            if(StringUtils.isEmpty(note)){
                DevicedyninfoEntity devicedyninfoEntity=new DevicedyninfoEntity();
                devicedyninfoEntity.setDevid(devstate.getDevid());
                devicedyninfoEntity.setEncservicestate(devstate.getEncservicestate());
                devicedyninfoEntity.setDevstate("2");
                devicedyninfoEntity.setAlgrescontion(devstate.getAlgrescontion());
                devicedyninfoEntity.setKeyversion(devstate.getKeyversion());
                devicedyninfoEntity.setKernelprogcheck(devstate.getKernelprogcheck());
                devicedyninfoEntity.setRngCheck(devstate.getRngCheck());
                devicedyninfoEntity.setDevipstate(devstate.getDevipstate());
                devicedyninfoEntity.setKeyupdatestate(devstate.getKeyupdatestate());
                devicedyninfoEntity.setTunnelconnectivity(devstate.getTunnelconnectivity());
                devicedyninfoEntity.setKeyrescondition(devstate.getKeyrescondition());
                Date time=new Date();
                Timestamp timestamp=new Timestamp(time.getTime());
                devicedyninfoEntity.setLastmodifytime(timestamp);
                devicedyninfoService.insert(devicedyninfoEntity);

                DevicedyninfonewEntity devicedyninfonewEntity=new DevicedyninfonewEntity();
                devicedyninfonewEntity.setDevstate("2");
                DevicedyninfonewEntity devicenewEntity=new DevicedyninfonewEntity();
                devicenewEntity.setDevid(devid);
                EntityWrapper<DevicedyninfonewEntity> wrappernew=new EntityWrapper<>();
                wrappernew.setEntity(devicenewEntity);
                devicedyninfonewEntity.setLastmodifytime(timestamp);
                devicedyninfonewService.update(devicedyninfonewEntity,wrappernew);

                List<DevicedyninfonewEntity> initlist=new InitDataListener().getListnew();
                for (DevicedyninfonewEntity unit:initlist){
                    if(devid.equals(unit.getDevid())){
                        unit.setDevstate("2");
                        unit.setCountum(0);

                    }
                }
                return R.ok();
            }else{

                DevicedyninfoEntity devicedyninfoEntity=new DevicedyninfoEntity();
                devicedyninfoEntity.setDevid(devstate.getDevid());
                devicedyninfoEntity.setEncservicestate(devstate.getEncservicestate());
                devicedyninfoEntity.setAlgrescontion(devstate.getAlgrescontion());
                devicedyninfoEntity.setKeyversion(devstate.getKeyversion());
                devicedyninfoEntity.setKernelprogcheck(devstate.getKernelprogcheck());
                devicedyninfoEntity.setRngCheck(devstate.getRngCheck());
                devicedyninfoEntity.setDevipstate(devstate.getDevipstate());
                devicedyninfoEntity.setKeyupdatestate(devstate.getKeyupdatestate());
                devicedyninfoEntity.setTunnelconnectivity(devstate.getTunnelconnectivity());
                devicedyninfoEntity.setKeyrescondition(devstate.getKeyrescondition());
                devicedyninfoEntity.setDevstate(devstate.getDevstate());
                Date time=new Date();
                Timestamp timestamp=new Timestamp(time.getTime());
                devicedyninfoEntity.setLastmodifytime(timestamp);
                devicedyninfoEntity.setNotice("待毙杀");

                devicedyninfoService.insert(devicedyninfoEntity);

                DevicedyninfonewEntity devicedyninfonewEntity=new DevicedyninfonewEntity();
                devicedyninfonewEntity.setNotice("待毙杀");
                DevicedyninfonewEntity devicenewEntity=new DevicedyninfonewEntity();
                devicenewEntity.setDevid(devid);
                EntityWrapper<DevicedyninfonewEntity> wrappernew=new EntityWrapper<>();
                wrappernew.setEntity(devicenewEntity);
                devicedyninfonewEntity.setLastmodifytime(timestamp);
                devicedyninfonewService.update(devicedyninfonewEntity,wrappernew);
                return R.ok();
            }

        }
    }

    @RequestMapping("/devdynlist")
    public R devdynlist(){
        //判断当前监管中心的上报策略
        Map<String,Object> map=new HashMap<>();
        map.put("is_current",Integer.valueOf(1));
        List<SysDeptEntity> list=sysDeptService.selectByMap(map);
        int strategy=list.get(0).getSendStrategy();
        List<DevicedyninfoEntity> menuList= devicedyninfoService.selectList(null);
        if(strategy==0){
            for(DevicedyninfoEntity deviceinfoEntity:menuList){
                deviceinfoEntity.setAlgrescontion(null);
                deviceinfoEntity.setKeyrescondition(null);
                deviceinfoEntity.setRngCheck(null);
                deviceinfoEntity.setKernelprogcheck(null);
            }
        }
        return R.ok().put("page", menuList);
    }
}
