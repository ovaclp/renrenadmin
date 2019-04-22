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

package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.annotation.DataFilter;
import io.renren.common.utils.Constant;
import io.renren.modules.sys.dao.SysDeptDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
	@Autowired
	private SysDeptService sysDeptService;
	@Override
	@DataFilter(subDept = true, user = false)
	public List<SysDeptEntity> queryList(Map<String, Object> params){
		List<SysDeptEntity> deptList =
			this.selectList(new EntityWrapper<SysDeptEntity>()
			.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER)));

		for(SysDeptEntity sysDeptEntity : deptList){
			Map<String, Object> map = new HashMap<>();
			map.put("dept_id", sysDeptEntity.getParentId());
			List<SysDeptEntity> list = sysDeptService.selectByMap(map);
			if (list.size() == 1) {
				sysDeptEntity.setParentName(list.get(0).getName());
			}
		}
		return deptList;
	}

	@Override
	public List<String> queryDetpIdList(String parentId) {
		return baseMapper.queryDetpIdList(parentId);
	}

	@Override
	public List<String> getSubDeptIdList(String deptId){
		//部门及子部门ID列表
		List<String> deptIdList = new ArrayList<>();

		//获取子部门ID
		List<String> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);

		return deptIdList;
	}

	/**
	 * 递归
	 */
	private void getDeptTreeList(List<String> subIdList, List<String> deptIdList){
		for(String deptId : subIdList){
			List<String> list = queryDetpIdList(deptId);
			if(list.size() > 0){
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}


	public  List<SysDeptEntity>  queryOwnSysNumberCenterid(){
		return baseMapper.queryOwnSysNumberCenterid();
	}


	public  List<SysDeptEntity> findSysDeptList(){
		return baseMapper.findSysDeptList();

	}

}
