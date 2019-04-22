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

import io.renren.modules.device.entity.DetpdomainnumdctEntity;
import io.renren.modules.device.service.DetpdomainnumdctService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-21 15:56:47
 */
@RestController
@RequestMapping("device/detpdomainnumdct")
public class DetpdomainnumdctController {
    @Autowired
    private DetpdomainnumdctService detpdomainnumdctService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("device:detpdomainnumdct:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = detpdomainnumdctService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{recnumber}")
    @RequiresPermissions("device:detpdomainnumdct:info")
    public R info(@PathVariable("recnumber") Integer recnumber){
        DetpdomainnumdctEntity detpdomainnumdct = detpdomainnumdctService.selectById(recnumber);

        return R.ok().put("detpdomainnumdct", detpdomainnumdct);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:detpdomainnumdct:save")
    public R save(@RequestBody DetpdomainnumdctEntity detpdomainnumdct){
        detpdomainnumdctService.insert(detpdomainnumdct);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:detpdomainnumdct:update")
    public R update(@RequestBody DetpdomainnumdctEntity detpdomainnumdct){
        ValidatorUtils.validateEntity(detpdomainnumdct);
        detpdomainnumdctService.updateAllColumnById(detpdomainnumdct);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:detpdomainnumdct:delete")
    public R delete(@RequestBody Integer[] recnumbers){
        detpdomainnumdctService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }

}
