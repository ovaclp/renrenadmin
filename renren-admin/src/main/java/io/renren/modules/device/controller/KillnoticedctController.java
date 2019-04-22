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

import io.renren.modules.device.entity.KillnoticedctEntity;
import io.renren.modules.device.service.KillnoticedctService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-04 17:35:14
 */
@RestController
@RequestMapping("device/killnoticedct")
public class KillnoticedctController {
    @Autowired
    private KillnoticedctService killnoticedctService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("device:killnoticedct:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = killnoticedctService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{recnumber}")
    @RequiresPermissions("device:killnoticedct:info")
    public R info(@PathVariable("recnumber") Integer recnumber){
        KillnoticedctEntity killnoticedct = killnoticedctService.selectById(recnumber);

        return R.ok().put("killnoticedct", killnoticedct);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:killnoticedct:save")
    public R save(@RequestBody KillnoticedctEntity killnoticedct){
        killnoticedctService.insert(killnoticedct);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:killnoticedct:update")
    public R update(@RequestBody KillnoticedctEntity killnoticedct){
        ValidatorUtils.validateEntity(killnoticedct);
        killnoticedctService.updateAllColumnById(killnoticedct);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:killnoticedct:delete")
    public R delete(@RequestBody Integer[] recnumbers){
        killnoticedctService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }

}
