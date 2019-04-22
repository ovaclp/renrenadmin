/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.sys.controller;

import io.renren.common.utils.Constant;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;


/**
 * 部门管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:dept:list")
    public List<SysDeptEntity> list() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

        return deptList;
    }
    /**
     * 树形列表
     */
    @RequestMapping("/treelist")
    public List<SysDeptEntity> treelist() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
        return deptList;
    }


    /**
     * 当前中心的行记录
     */
    @RequestMapping("/rowcurrent")
    //@RequiresPermissions("sys:dept:list")
    public R rowcurrent() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
        for (SysDeptEntity sysDeptEntity : deptList) {
            if (sysDeptEntity.getIsCurrent() == 1) {

                return R.ok().put("sysDeptEntity", sysDeptEntity);
            }
        }
        return R.error("获取当前中心信息失败！");
    }

    /**
     * 选择部门(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:dept:select")
    public R select() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

        //添加一级部门
        if (getUserId() == Constant.SUPER_ADMIN) {
            SysDeptEntity root = new SysDeptEntity();
            root.setDeptId("0");
            root.setName("一级部门");
            root.setParentId("-1");
            root.setOpen(true);
            deptList.add(root);
        }

        return R.ok().put("deptList", deptList);
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @RequestMapping("/info")
   // @RequiresPermissions("sys:dept:list")
    public R info() {
        String deptId = "0";
        if (getUserId() != Constant.SUPER_ADMIN) {
            List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
            String parentId = null;
            for (SysDeptEntity sysDeptEntity : deptList) {
                if (parentId == null) {
                    parentId = sysDeptEntity.getParentId();
                    continue;
                }

                if (parentId.compareTo(sysDeptEntity.getParentId()) > 0) {
                    parentId = sysDeptEntity.getParentId();
                }
            }
            deptId = parentId;
        }

        return R.ok().put("deptId", deptId);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public R info(@PathVariable("deptId") Long deptId) {
        SysDeptEntity dept = sysDeptService.selectById(deptId);

        return R.ok().put("dept", dept);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:dept:save")
    public R save(@RequestBody SysDeptEntity dept) {
        //dept.setIsCurrent(0);
        sysDeptService.insert(dept);

        return R.ok();
    }


    /**
     * 上级监控中心增加下级监控信息，并且返回自己的id，ip
     */
    @RequestMapping("/registerReturn")
    public R registerReturn(@RequestBody SysDeptEntity dept) {
        //取出本级监控监控中心的32位id，ip和监控中心sysnumber
        SysDeptEntity partdept = sysDeptService.queryOwnSysNumberCenterid().get(0);
        //获取下级监控中心的数据,插入数据库形成新的记录
        dept.setParentIp(partdept.getIpAddr());
        dept.setParentId(partdept.getDeptId());
        dept.setParcenterId(partdept.getCenterId());
        dept.setIsCurrent(0);

        Date time = new Date();
        Timestamp timestamp = new Timestamp(time.getTime());
        dept.setLastModifyTime(timestamp);
        sysDeptService.insert(dept);
        return R.ok().put("deptinfo", partdept);
    }

    /**
     * 下级向上级监控中心注册
     */
    @RequestMapping("/register")
    public R register(@RequestBody SysDeptEntity dept) {
        List<SysDeptEntity> deptList = sysDeptService.queryOwnSysNumberCenterid();
        if (deptList.size() > 0) {
            return R.error("本级监控中心已注册");
        }
        String parentip = dept.getParentIp();
        if(StringUtils.isEmpty(parentip)){
            dept.setIsCurrent(1);
            Date time = new Date();
            Timestamp timestamp = new Timestamp(time.getTime());
            dept.setLastModifyTime(timestamp);
            dept.setParentId("0");
            sysDeptService.insert(dept);
            return R.ok();
        }else{
            R r = restTemplate.postForObject("http://" + parentip + ":8080/renren-admin/sys/dept/registerReturn", dept, R.class);
            LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>) r.get("deptinfo");
            dept.setParcenterId((String) linkedHashMap.get("centerId"));
            dept.setParentId((String) linkedHashMap.get("deptId"));
            dept.setParentIp((String) linkedHashMap.get("ipAddr"));
            dept.setIsCurrent(1);
            Date time = new Date();
            Timestamp timestamp = new Timestamp(time.getTime());
            dept.setLastModifyTime(timestamp);
            sysDeptService.insert(dept);
            return R.ok();
        }


    }


    /**
     * 本级系统查询
     */
    @RequestMapping("/infoCurrent")
    public R infoCurrent() {
        Map<String, Object> map = new HashMap<>();
        map.put("is_current", 1);
        List<SysDeptEntity> dept = sysDeptService.selectByMap(map);
        if (dept.size() != 0)
            return R.ok().put("dept", dept.get(0));
        else
            return R.ok().put("dept", new SysDeptEntity());
    }

//	/**
//	 * 注册
//	 */
//	@RequestMapping("/register")
//	public R register(@RequestBody SysDeptEntity dept){
//		dept.setIsCurrent(1);
//		sysDeptService.insert(dept);
//
//		return R.ok();
//	}

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:dept:update")
    public R update(@RequestBody SysDeptEntity dept) {
        sysDeptService.updateById(dept);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    public R delete(String deptId) {
        //判断是否有子部门
        List<String> deptList = sysDeptService.queryDetpIdList(deptId);
        if (deptList.size() > 0) {
            return R.error("请先删除子部门");
        }

        sysDeptService.deleteById(deptId);

        return R.ok();
    }

}
