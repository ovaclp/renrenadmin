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
 * @date 2018-12-21 15:56:47
 */
@TableName("detpdomainnumdct")
public class DetpdomainnumdctEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 监控中心所属域编号
	 */
	private String deptDomainnum;
	/**
	 * 监控中心所属域
	 */
	private String deptDomain;

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
	 * 设置：监控中心所属域编号
	 */
	public void setDeptDomainnum(String deptDomainnum) {
		this.deptDomainnum = deptDomainnum;
	}
	/**
	 * 获取：监控中心所属域编号
	 */
	public String getDeptDomainnum() {
		return deptDomainnum;
	}
	/**
	 * 设置：监控中心所属域
	 */
	public void setDeptDomain(String deptDomain) {
		this.deptDomain = deptDomain;
	}
	/**
	 * 获取：监控中心所属域
	 */
	public String getDeptDomain() {
		return deptDomain;
	}
}
