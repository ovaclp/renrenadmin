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

import io.renren.modules.device.entity.DevtypeinfoEntity;
import io.renren.modules.device.service.DevtypeinfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-11-07 15:59:57
 */
@RestController
@RequestMapping("device/devtypeinfo")
public class DevtypeinfoController {
    @Autowired
    private DevtypeinfoService devtypeinfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("device:devtypeinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = devtypeinfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{recnumber}")
    @RequiresPermissions("device:devtypeinfo:info")
    public R info(@PathVariable("recnumber") Integer recnumber){
        DevtypeinfoEntity devtypeinfo = devtypeinfoService.selectById(recnumber);

        return R.ok().put("devtypeinfo", devtypeinfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:devtypeinfo:save")
    public R save(@RequestBody DevtypeinfoEntity devtypeinfo){
        devtypeinfoService.insert(devtypeinfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:devtypeinfo:update")
    public R update(@RequestBody DevtypeinfoEntity devtypeinfo){
        ValidatorUtils.validateEntity(devtypeinfo);
        devtypeinfoService.updateAllColumnById(devtypeinfo);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:devtypeinfo:delete")
    public R delete(@RequestBody Integer[] recnumbers){
        devtypeinfoService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }

}
