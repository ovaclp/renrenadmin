package io.renren.modules.device.controller;

import java.util.*;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.device.service.DevicedyninfonewService;
import io.renren.modules.device.service.DevlogcontentinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.device.entity.DeviceloginfoEntity;
import io.renren.modules.device.service.DeviceloginfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
@Api(value = "/DeviceloginfoController",tags = "deviceloginfo",description = "关于设备日志信息的操作接口")
@RestController
@RequestMapping("device/deviceloginfo")
public class DeviceloginfoController {
    @Autowired
    private DeviceloginfoService deviceloginfoService;
    @Autowired
    private DevicedyninfonewService devicedyninfonewService;


    /**
     * 列表
     */
    @ApiOperation(value = "设备日志信息列表",notes = "根据params参数来获取设备日志信息列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("device:deviceloginfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = deviceloginfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{recnumber}")
    @RequiresPermissions("device:deviceloginfo:info")
    public R info(@PathVariable("recnumber") Integer recnumber) {
        DeviceloginfoEntity deviceloginfo = deviceloginfoService.selectById(recnumber);

        return R.ok().put("deviceloginfo", deviceloginfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:deviceloginfo:save")
    public R save(@RequestBody DeviceloginfoEntity deviceloginfo) {
        deviceloginfoService.insert(deviceloginfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:deviceloginfo:update")
    public R update(@RequestBody DeviceloginfoEntity deviceloginfo) {
        ValidatorUtils.validateEntity(deviceloginfo);
        deviceloginfoService.updateAllColumnById(deviceloginfo);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:deviceloginfo:delete")
    public R delete(@RequestBody Integer[] recnumbers) {
        deviceloginfoService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }

    /*
     * 查询单个设备数据库记录
     */
    @RequestMapping("/quaryDByDevid")
    public R quaryDByDevid(@RequestBody Map<String, String> params) {
        String deviceid = params.get("devid");
        List<DeviceloginfoEntity> list = deviceloginfoService.queryDByDevid(deviceid);
        //new PageUtils(list,list.size(),10,1);
        return R.ok().put("page", new PageUtils(list, list.size(), 10, 1));
    }

    /*
     * 查询本级监管中心所属的设备devid集合
     */
    @RequestMapping("/devlist")
    public List<DeviceinfoEntity> devlist() {
        //List<String> list= deviceloginfoService.queryDevList();

        return deviceloginfoService.queryDevList();
    }


    /*

     *在某时间段对单个设备的日志查询
     * InputParameter Map
     * return List<DeviceloginfoEntity>
     *
     */
    @ApiOperation(value = "查询设备日志",notes = "查询某时间段内设备唯一标识日志记录列表")
    @ApiImplicitParam(name = "params",value = "唯一标识起始和结束时间")
    @RequestMapping(value = "/loglist")
    @RequiresPermissions("device:deviceloginfo:loglist")
    public R loglist(@RequestBody Map<String, String> params) {
        //System.out.println(params);
        String deviceid = params.get("devid");
        Map<String, Object> maper = new HashMap<>();
        maper.put("DevID", deviceid);
        List<DevicedyninfonewEntity> listdyinfo = devicedyninfonewService.selectByMap(maper);
        if (listdyinfo != null && listdyinfo.size() > 0) {
            DevicedyninfonewEntity devicedyninfonewEntity = listdyinfo.get(0);
            if (devicedyninfonewEntity.getDevstate().equals("1") | devicedyninfonewEntity.getDevstate().equals("2")) {
                return R.error().put("msg", "设备离线或者已毙杀，无法查询日志！");
            }
        }
        List<DeviceloginfoEntity> list = deviceloginfoService.querylogByDate(params);
        if(list!=null&&list.size()>0){
            return R.ok();
        }else{
            return R.error();
        }
    }

}
