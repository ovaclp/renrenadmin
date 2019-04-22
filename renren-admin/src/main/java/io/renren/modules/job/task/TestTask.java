package io.renren.modules.job.task;

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

import com.alibaba.fastjson.JSON;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.R;
import io.renren.modules.device.entity.DevicedyninfoEntity;
import io.renren.modules.device.entity.DeviceinfoEntity;
import io.renren.modules.device.service.DevicedyninfoService;
import io.renren.modules.device.service.DeviceinfoService;
import io.renren.modules.job.formwork.SendSynchroData;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.2.0 2016-11-28
 */
@Component("testTask")
public class TestTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SendSynchroData sendSynchroData;

	
	public void test(String params){
		/*logger.info("我是带参数的test方法，正在被执行，参数为：" + params);
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		System.out.println("------------定时任务1------------------------");
		
		//SysUserEntity user = sysUserService.selectById(1L);
		//System.out.println(ToStringBuilder.reflectionToString(user));
		
	}
	
	
	public void test2(){
		System.out.println("------------------数据同步开始----------------------");
		sendSynchroData.sendSyncData();
		//监控中心1静态信息
		/*R r=restTemplate.postForObject("http://222.28.142.146:8080/renren-admin/device/deviceinfo/devlist",null,R.class);
		//List<DeviceinfoEntity> list=restTemplate.postForObject("http://192.168.2.243:8080/renren-admin/device/deviceinfo/devinfolist",null,List.class);
		List<HashMap<String,Object>> deviceinfoEntityList=(List<HashMap<String,Object>>)r.get("page");
		List<DeviceinfoEntity> list=new ArrayList<>();
		//执行HTTP请求，将返回的结构使用ResultVO类格式化
		//ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, ResultVO.class);

		for(HashMap<String,Object> entity:deviceinfoEntityList){
			DeviceinfoEntity deviceinfoEntity=new DeviceinfoEntity();
			deviceinfoEntity.setRecnumber((int)entity.get("recnumber"));
			deviceinfoEntity.setDeviceid((String)entity.get("deviceid"));
			deviceinfoEntity.setDeviceversion((int)entity.get("deviceversion"));
			deviceinfoEntity.setAsymalgability0((int)entity.get("asymalgability0"));
			deviceinfoEntity.setAsymalgability1((int)entity.get("asymalgability1"));
			deviceinfoEntity.setHashalgability((int)entity.get("hashalgability"));
			deviceinfoEntity.setDeviceipv4((String)entity.get("deviceipv4"));
			deviceinfoEntity.setDevicename((String)entity.get("devicename"));
			deviceinfoEntity.setIssuername((String)entity.get("issuername"));
			deviceinfoEntity.setDevicetype((String)entity.get("devicetype"));
			deviceinfoEntity.setDevname((String)entity.get("devname"));
			deviceinfoEntity.setDevcertificate((String)entity.get("devcertificate"));
			deviceinfoEntity.setAdminorid((String) entity.get("adminorid"));
			deviceinfoEntity.setAdminorphone((String)entity.get("adminorphone"));
			deviceinfoEntity.setSysnumber((String)entity.get("sysnumber"));
			deviceinfoEntity.setDevdepartment((String)entity.get("devdepartment"));
			//deviceinfoEntity.setBuffersize((int)entity.get("buffersize"));
			deviceinfoEntity.setRegdate(DateUtils.stringToDate((String)entity.get("regdate"),DateUtils.DATE_TIME_PATTERN));
			list.add(deviceinfoEntity);
		}
		deviceinfoService.insertOrUpdateBatch(list);



		//监管中心2静态状态
		R r1=restTemplate.postForObject("http://222.28.142.25:8080/renren-admin/device/deviceinfo/devlist",null,R.class);
		//List<DeviceinfoEntity> list=restTemplate.postForObject("http://192.168.2.243:8080/renren-admin/device/deviceinfo/devinfolist",null,List.class);
		List<HashMap<String,Object>> deviceinfoEntityList1=(List<HashMap<String,Object>>)r1.get("page");
		List<DeviceinfoEntity> list1=new ArrayList<>();
		//执行HTTP请求，将返回的结构使用ResultVO类格式化
		//ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, ResultVO.class);

		for(HashMap<String,Object> entity:deviceinfoEntityList1){
			DeviceinfoEntity deviceinfoEntity=new DeviceinfoEntity();
			deviceinfoEntity.setRecnumber((int)entity.get("recnumber"));
			deviceinfoEntity.setDeviceid((String)entity.get("deviceid"));
			deviceinfoEntity.setDeviceversion((int)entity.get("deviceversion"));
			deviceinfoEntity.setAsymalgability0((int)entity.get("asymalgability0"));
			deviceinfoEntity.setAsymalgability1((int)entity.get("asymalgability1"));
			deviceinfoEntity.setHashalgability((int)entity.get("hashalgability"));
			deviceinfoEntity.setDeviceipv4((String)entity.get("deviceipv4"));
			deviceinfoEntity.setDevicename((String)entity.get("devicename"));
			deviceinfoEntity.setIssuername((String)entity.get("issuername"));
			deviceinfoEntity.setDevicetype((String)entity.get("devicetype"));
			deviceinfoEntity.setDevname((String)entity.get("devname"));
			deviceinfoEntity.setDevcertificate((String)entity.get("devcertificate"));
			deviceinfoEntity.setAdminorid((String) entity.get("adminorid"));
			deviceinfoEntity.setAdminorphone((String)entity.get("adminorphone"));
			deviceinfoEntity.setSysnumber((String)entity.get("sysnumber"));
			deviceinfoEntity.setDevdepartment((String)entity.get("devdepartment"));
			//deviceinfoEntity.setBuffersize((int)entity.get("buffersize"));
			deviceinfoEntity.setRegdate(DateUtils.stringToDate((String)entity.get("regdate"),DateUtils.DATE_TIME_PATTERN));
			list1.add(deviceinfoEntity);
		}
		deviceinfoService.insertOrUpdateBatch(list1);



		///监控中心1动态信息
		R r2=restTemplate.postForObject("http://222.28.142.146:8080/renren-admin/device/devicedyninfo/devdynlist",null,R.class);
		//List<DeviceinfoEntity> list=restTemplate.postForObject("http://192.168.2.243:8080/renren-admin/device/deviceinfo/devinfolist",null,List.class);
		List<HashMap<String,Object>> devicedyninfoEntityList=(List<HashMap<String,Object>>)r2.get("page");
		List<DevicedyninfoEntity> devicedyninfoList=new ArrayList<>();
		for(HashMap<String,Object> entity:devicedyninfoEntityList){

			DevicedyninfoEntity devicedyninfoEntity=new DevicedyninfoEntity();

			devicedyninfoEntity.setRecnumber((int)entity.get("recnumber"));
			devicedyninfoEntity.setDevid((String)entity.get("devid"));
			devicedyninfoEntity.setEncservicestate((String)entity.get("encservicestate"));
			devicedyninfoEntity.setKeyversion((String)entity.get("keyversion"));
			devicedyninfoEntity.setKeyupdatestate((String)entity.get("keyupdatestate"));
			devicedyninfoEntity.setTunnelconnectivity((String)entity.get("tunnelconnectivity"));
			devicedyninfoEntity.setDevipstate((String)entity.get("devipstate"));
			devicedyninfoEntity.setAlgrescontion((Integer) entity.get("algrescontion"));
			//int b=(int) entity.get("keyrescondition");
			//Long bb=(long)b;
			//devicedyninfoEntity.setKeyrescondition(bb);
			devicedyninfoEntity.setDevstate((String)entity.get("devstate"));
			devicedyninfoEntity.setRngCheck((String)entity.get("rngCheck"));
			devicedyninfoEntity.setKernelprogcheck((String)entity.get("kernelprogcheck"));

			devicedyninfoList.add(devicedyninfoEntity);
		}

		devicedyninfoService.insertOrUpdateBatch(devicedyninfoList);


		///监控中心2状态信息
		R r3=restTemplate.postForObject("http://222.28.142.25:8080/renren-admin/device/devicedyninfo/devdynlist",null,R.class);
		//List<DeviceinfoEntity> list=restTemplate.postForObject("http://192.168.2.243:8080/renren-admin/device/deviceinfo/devinfolist",null,List.class);
		List<HashMap<String,Object>> devicedyninfoEntityList3=(List<HashMap<String,Object>>)r3.get("page");
		List<DevicedyninfoEntity> devicedyninfoList4=new ArrayList<>();
		for(HashMap<String,Object> entity:devicedyninfoEntityList3){

			DevicedyninfoEntity devicedyninfoEntity=new DevicedyninfoEntity();

			devicedyninfoEntity.setRecnumber((int)entity.get("recnumber"));
			devicedyninfoEntity.setDevid((String)entity.get("devid"));
			devicedyninfoEntity.setEncservicestate((String)entity.get("encservicestate"));
			devicedyninfoEntity.setKeyversion((String)entity.get("keyversion"));
			devicedyninfoEntity.setKeyupdatestate((String)entity.get("keyupdatestate"));
			devicedyninfoEntity.setTunnelconnectivity((String)entity.get("tunnelconnectivity"));
			devicedyninfoEntity.setDevipstate((String)entity.get("devipstate"));
			devicedyninfoEntity.setAlgrescontion((Integer) entity.get("algrescontion"));
			//int b=(int) entity.get("keyrescondition");
			//Long bb=(long)b;
			//devicedyninfoEntity.setKeyrescondition(bb);
			devicedyninfoEntity.setDevstate((String)entity.get("devstate"));
			devicedyninfoEntity.setRngCheck((String)entity.get("rngCheck"));
			devicedyninfoEntity.setKernelprogcheck((String)entity.get("kernelprogcheck"));

			devicedyninfoList4.add(devicedyninfoEntity);
		}

		devicedyninfoService.insertOrUpdateBatch(devicedyninfoList4);
		*/
	}
}
