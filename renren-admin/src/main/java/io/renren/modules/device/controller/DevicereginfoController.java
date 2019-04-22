package io.renren.modules.device.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.device.entity.DevicereginfoEntity;
import io.renren.modules.device.service.DevicereginfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 密码设备注册表
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-08 17:05:55
 */
@RestController
@RequestMapping("device/devicereginfo")
public class DevicereginfoController {
    @Autowired
    private DevicereginfoService devicereginfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("device:devicereginfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = devicereginfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{recnumber}")
    @RequiresPermissions("device:devicereginfo:info")
    public R info(@PathVariable("recnumber") Integer recnumber){
        DevicereginfoEntity devicereginfo = devicereginfoService.selectById(recnumber);

        return R.ok().put("devicereginfo", devicereginfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:devicereginfo:save")
    public R save(@RequestBody DevicereginfoEntity devicereginfo){
        //生成设备唯一标示，调用管理代理接口进行注册
        devicereginfo.setDevid(UUID.randomUUID().toString().replace("-",""));
        devicereginfo.setRegdate(new Date());
        devicereginfoService.insert(devicereginfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:devicereginfo:update")
    public R update(@RequestBody DevicereginfoEntity devicereginfo){
        devicereginfo.setRegdate(new Date());
        ValidatorUtils.validateEntity(devicereginfo);
        devicereginfoService.updateAllColumnById(devicereginfo);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:devicereginfo:delete")
    public R delete(@RequestBody Integer[] recnumbers){
        devicereginfoService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }

}
