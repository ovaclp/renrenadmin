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

package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 部门管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@TableName("sys_dept")
public class SysDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 本级监控中心编号
	 */
	private String deptId;
	/**
	 * 使用单位名称
	 */
	private String deptUser;
	/**
	 * 上级监控中心编号
	 */
	private String parentId;
	/**
	 * 上级监控中心IP地址
	 */
	private String parentIp;
	/**
	 * 上级监控中心唯一标识
	 */
	private String parcenterId;
	/**
	 * 本级监控中心名称
	 */
	private String name;
	/**
	 * 本级监控中心唯一标识
	 */
	private String centerId;
	/**
	 * 本级监控中心的IP地址
	 */
	private String ipAddr;
	/**
	 * 上级监控中心名称
	 */
	@TableField(exist=false)
	private String parentName;
	/**
	 * 排序
	 */
	private Integer orderNum;

	/**
	 * 是否删除  -1：已删除  0：正常
	 */
	@TableLogic
	private Integer delFlag;
	/**
	 * 是否本级监管系统
	 */
	private Integer isCurrent;
	/**
	 * 代理向监控中心报数据的端口号
	 */
	private Integer agentPort;
	/**
	 * 上报策略0:最小集合；1:全部集合
	 */
	private Integer sendStrategy;
	/**
	 * 毙杀策略：0：自动;1：通知
	 */
	private Integer killStrategy;
	/**
	 * 所属域编号
	 */
	private String deptDomainnum;
	/**
	 * 最近更新时间
	 */
	private Date lastModifyTime;
	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;
	@TableField(exist=false)
	private List<?> list;

	/**
	 * 设置：所属域编号
	 */
	public void setDeptDomainnum(String deptDomainnum) {
		this.deptDomainnum = deptDomainnum;
	}
	/**
	 * 获取：所属域编号
	 */
	public String getDeptDomainnum() {
		return deptDomainnum;
	}
	/**
	 * 设置：记录编号
	 */
	public void setRecnumber(Integer recnumber) {
		this.recnumber = recnumber;
	}
	/**
	 * 获取：记录编号
	 */
	public Integer getRecnumber() {
		return recnumber;
	}
	/**
	 * 设置：本级监控中心编号
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：本级监控中心编号
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * 设置：使用单位名称
	 */
	public void setDeptUser(String deptUser) {
		this.deptUser = deptUser;
	}
	/**
	 * 获取：使用单位名称
	 */
	public String getDeptUser() {
		return deptUser;
	}
	/**
	 * 设置：上级监控中心编号
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：上级监控中心编号
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * 设置：上级监控中心IP地址
	 */
	public void setParentIp(String parentIp) {
		this.parentIp = parentIp;
	}
	/**
	 * 获取：上级监控中心IP地址
	 */
	public String getParentIp() {
		return parentIp;
	}
	/**
	 * 设置：上级监控中心唯一标识
	 */
	public void setParcenterId(String parcenterId) {
		this.parcenterId = parcenterId;
	}
	/**
	 * 获取：上级监控中心唯一标识
	 */
	public String getParcenterId() {
		return parcenterId;
	}
	/**
	 * 设置：本级监控中心名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：本级监控中心名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：本级监控中心唯一标识
	 */
	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}
	/**
	 * 获取：本级监控中心唯一标识
	 */
	public String getCenterId() {
		return centerId;
	}
	/**
	 * 设置：本级监控中心的IP地址
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	/**
	 * 获取：本级监控中心的IP地址
	 */
	public String getIpAddr() {
		return ipAddr;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 获取：是否删除  -1：已删除  0：正常
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置：是否删除  -1：已删除  0：正常
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：是否本级监管系统
	 */
	public Integer getIsCurrent() {
		return isCurrent;
	}
	/**
	 * 设置：是否本级监管系统
	 */
	public void setIsCurrent(Integer isCurrent) {
		this.isCurrent = isCurrent;
	}

	/**
	 * 获取：代理向监控中心报数据的端口号
	 */
	public Integer getAgentPort() {
		return agentPort;
	}
	/**
	 * 设置：代理向监控中心报数据的端口号
	 */
	public void setAgentPort(Integer agentPort) {
		this.agentPort = agentPort;
	}
	/**
	 * 获取：上报策略0:最小集合；1:全部集合
	 */
	public Integer getSendStrategy() {
		return sendStrategy;
	}
	/**
	 * 设置：上报策略0:最小集合；1:全部集合
	 */
	public void setSendStrategy(Integer sendStrategy) {
		this.sendStrategy = sendStrategy;
	}
	/**
	 * 设置：毙杀策略：0：自动;1：通知
	 */
	public void setKillStrategy(Integer killStrategy) {
		this.killStrategy = killStrategy;
	}
	/**
	 * 获取：毙杀策略：0：自动;1：通知
	 */
	public Integer getKillStrategy() {
		return killStrategy;
	}
	/**
	 * 设置：最近更新时间
	 */
	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	/**
	 * 获取：最近更新时间
	 */
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "SysDeptEntity{" +
				"recnumber=" + recnumber +
				", deptId='" + deptId + '\'' +
				", deptUser='" + deptUser + '\'' +
				", parentId='" + parentId + '\'' +
				", parentIp='" + parentIp + '\'' +
				", parcenterId='" + parcenterId + '\'' +
				", name='" + name + '\'' +
				", centerId='" + centerId + '\'' +
				", ipAddr='" + ipAddr + '\'' +
				", parentName='" + parentName + '\'' +
				", orderNum=" + orderNum +
				", delFlag=" + delFlag +
				", isCurrent=" + isCurrent +
				", agentPort=" + agentPort +
				", sendStrategy=" + sendStrategy +
				", killStrategy=" + killStrategy +
				", deptDomainnum='" + deptDomainnum + '\'' +
				", lastModifyTime=" + lastModifyTime +
				", open=" + open +
				", list=" + list +
				'}';
	}
}
