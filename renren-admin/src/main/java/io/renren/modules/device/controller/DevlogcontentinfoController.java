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

import io.renren.modules.device.entity.DevlogcontentinfoEntity;
import io.renren.modules.device.service.DevlogcontentinfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-11-09 15:00:25
 */
@RestController
@RequestMapping("device/devlogcontentinfo")
public class DevlogcontentinfoController {
    @Autowired
    private DevlogcontentinfoService devlogcontentinfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("device:devlogcontentinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = devlogcontentinfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{recnumber}")
    @RequiresPermissions("device:devlogcontentinfo:info")
    public R info(@PathVariable("recnumber") Integer recnumber){
        DevlogcontentinfoEntity devlogcontentinfo = devlogcontentinfoService.selectById(recnumber);

        return R.ok().put("devlogcontentinfo", devlogcontentinfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:devlogcontentinfo:save")
    public R save(@RequestBody DevlogcontentinfoEntity devlogcontentinfo){
        devlogcontentinfoService.insert(devlogcontentinfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:devlogcontentinfo:update")
    public R update(@RequestBody DevlogcontentinfoEntity devlogcontentinfo){
        ValidatorUtils.validateEntity(devlogcontentinfo);
        devlogcontentinfoService.updateAllColumnById(devlogcontentinfo);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:devlogcontentinfo:delete")
    public R delete(@RequestBody Integer[] recnumbers){
        devlogcontentinfoService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }

}
