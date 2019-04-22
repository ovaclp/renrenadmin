package io.renren.modules.device.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.device.entity.DevicedyninfonewEntity;
import io.renren.modules.device.service.DevicedyninfonewService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-26 12:40:15
 */
@RestController
@RequestMapping("device/devicedyninfonew")
public class DevicedyninfonewController {
    @Autowired
    private DevicedyninfonewService devicedyninfonewService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("device:devicedyninfonew:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = devicedyninfonewService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{recnumber}")
    //@RequiresPermissions("device:devicedyninfonew:info")
    public R info(@PathVariable("recnumber") Integer recnumber) {
        DevicedyninfonewEntity devicedyninfonew = devicedyninfonewService.selectById(recnumber);

        return R.ok().put("devicedyninfonew", devicedyninfonew);
    }

    @RequestMapping("/devinfo/{devid}")
    public R devinfo(@PathVariable("devid") String devid) {
        Map<String, Object> map = new HashMap<>();
        map.put("DevID", devid);
        List<DevicedyninfonewEntity> list = devicedyninfonewService.selectByMap(map);
        if (list.size() == 1) {
            return R.ok().put("devicedyninfonew", list.get(0));
        } else {
            return R.error();
        }
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("device:devicedyninfonew:save")
    public R save(@RequestBody DevicedyninfonewEntity devicedyninfonew) {
        devicedyninfonewService.insert(devicedyninfonew);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("device:devicedyninfonew:update")
    public R update(@RequestBody DevicedyninfonewEntity devicedyninfonew) {
        ValidatorUtils.validateEntity(devicedyninfonew);
        devicedyninfonewService.updateAllColumnById(devicedyninfonew);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("device:devicedyninfonew:delete")
    public R delete(@RequestBody Integer[] recnumbers) {
        devicedyninfonewService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }


    /**
     * 监控表
     */
    @RequestMapping("/tablelist/{sysNum}")
    public R tablelist(@PathVariable("sysNum") String sysNum) {
        List<DevicedyninfonewEntity> list = null;
        if (sysNum.equals("000")) {
            list = devicedyninfonewService.selectList(null);
        } else {
            list = devicedyninfonewService.queryDevidsBySysnum(sysNum);
        }

        return R.ok().put("devidList", list);
    }

}
