package io.renren.modules.device.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.device.entity.AgentinfoEntity;
import io.renren.modules.device.service.AgentinfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-04 09:33:27
 */
@RestController
@Api
@RequestMapping("device/agentinfo")
public class AgentinfoController {
    @Autowired
    private AgentinfoService agentinfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("device:agentinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = agentinfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{recnumber}")
    //@RequiresPermissions("device:agentinfo:info")
    public R info(@PathVariable("recnumber") Integer recnumber){
        AgentinfoEntity agentinfo = agentinfoService.selectById(recnumber);

        return R.ok().put("agentinfo", agentinfo);
    }

    /**
     * 代理信息
     */
    @RequestMapping("/infoAgent/{agentid}")
    public R infoAgent(@PathVariable("agentid") String agentid){
        Map<String, Object> map = new HashMap<>();
        map.put("AgentID", agentid);
        List<AgentinfoEntity> list = agentinfoService.selectByMap(map);
        if (list.size() == 1) {
            return R.ok().put("agentinfo", list.get(0));
        } else {
            return R.error();
        }
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("device:agentinfo:save")
    public R save(@RequestBody AgentinfoEntity agentinfo){
        agentinfoService.insert(agentinfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("device:agentinfo:update")
    public R update(@RequestBody AgentinfoEntity agentinfo){
        ValidatorUtils.validateEntity(agentinfo);
        agentinfoService.updateAllColumnById(agentinfo);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("device:agentinfo:delete")
    public R delete(@RequestBody Integer[] recnumbers){
        agentinfoService.deleteBatchIds(Arrays.asList(recnumbers));

        return R.ok();
    }

}
