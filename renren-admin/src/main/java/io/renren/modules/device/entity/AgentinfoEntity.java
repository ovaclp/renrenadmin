package io.renren.modules.device.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-12-04 09:33:27
 */
@TableName("agentinfo")
public class AgentinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 管理代理唯一标识
	 */
	private String agentid;
	/**
	 * 管理代理的IP地址
	 */
	private String agentip;
	/**
	 * 管理代理服务器端口号
	 */
	private Integer agentport;
	/**
	 * 所属监控中心唯一标识
	 */
	private String centerid;

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
	 * 设置：管理代理唯一标识
	 */
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	/**
	 * 获取：管理代理唯一标识
	 */
	public String getAgentid() {
		return agentid;
	}
	/**
	 * 设置：管理代理的IP地址
	 */
	public void setAgentip(String agentip) {
		this.agentip = agentip;
	}
	/**
	 * 获取：管理代理的IP地址
	 */
	public String getAgentip() {
		return agentip;
	}
	/**
	 * 设置：管理代理服务器端口号
	 */
	public void setAgentport(Integer agentport) {
		this.agentport = agentport;
	}
	/**
	 * 获取：管理代理服务器端口号
	 */
	public Integer getAgentport() {
		return agentport;
	}
	/**
	 * 设置：所属监控中心唯一标识
	 */
	public void setCenterid(String centerid) {
		this.centerid = centerid;
	}
	/**
	 * 获取：所属监控中心唯一标识
	 */
	public String getCenterid() {
		return centerid;
	}

	@Override
	public String toString() {
		return "AgentinfoEntity{" +
				"agentid='" + agentid + '\'' +
				", agentip='" + agentip + '\'' +
				", agentport=" + agentport +
				", centerid='" + centerid + '\'' +
				'}';
	}
}
