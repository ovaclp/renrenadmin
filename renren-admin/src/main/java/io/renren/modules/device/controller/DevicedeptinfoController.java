package io.renren.modules.device.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.device.entity.DevicedeptinfoEntity;
import io.renren.modules.device.service.DevicedeptinfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-11-09 21:32:24
 */
@RestController
@RequestMapping("device/devicedeptinfo")
public class DevicedeptinfoController {
    @Autowired
    private DevicedeptinfoService devicedeptinfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("device:devicedeptinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = devicedeptinfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{recnumber}")
    @RequiresPermissions("device:devicedeptinfo:info")
    public R info(@PathVariable("recnumber") Integer recnumber){
        DevicedeptinfoEntity devicedeptinfo = devicedeptinfoService.selectById(recnumber);

        return R.ok().put("devicedeptinfo", devicedeptinfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:devicedeptinfo:save")
    public R save(@RequestBody DevicedeptinfoEntity devicedeptinfo){
        devicedeptinfoService.insert(devicedeptinfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:devicedeptinfo:update")
    public R update(@RequestBody DevicedeptinfoEntity devicedeptinfo){
        ValidatorUtils.validateEntity(devicedeptinfo);
        devicedeptinfoService.updateAllColumnById(devicedeptinfo);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:devicedeptinfo:delete")
    public R delete(@RequestBody Integer[] recnumbers){
        devicedeptinfoService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }

}
