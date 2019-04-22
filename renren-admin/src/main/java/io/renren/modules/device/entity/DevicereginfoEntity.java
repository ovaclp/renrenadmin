package io.renren.modules.device.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 密码设备注册表
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-08 17:05:55
 */
@TableName("devicereginfo")
public class DevicereginfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 设备唯一标识
	 */
	private String devid;
	/**
	 * 设备生产厂商名称
	 */
	private String issuername;
	/**
	 * 设备型号
	 */
	private String devname;
	/**
	 * 设备编号
	 */
	private String devserial;
	/**
	 * 设备证书
	 */
	private String devcertificate;
	/**
	 * 设备注册管理员身份证编号
	 */
	private String adminorid;
	/**
	 * 设备管理员联系电话
	 */
	private String adminorphone;
	/**
	 * 监管中心编号
	 */
	private String sysnumber;
	/**
	 * 设备使用单位编号
	 */
	private String devdepartment;
	/**
	 * 注册日期
	 */
	private Date regdate;

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
	 * 设置：设备唯一标识
	 */
	public void setDevid(String devid) {
		this.devid = devid;
	}
	/**
	 * 获取：设备唯一标识
	 */
	public String getDevid() {
		return devid;
	}
	/**
	 * 设置：设备生产厂商名称
	 */
	public void setIssuername(String issuername) {
		this.issuername = issuername;
	}
	/**
	 * 获取：设备生产厂商名称
	 */
	public String getIssuername() {
		return issuername;
	}
	/**
	 * 设置：设备型号
	 */
	public void setDevname(String devname) {
		this.devname = devname;
	}
	/**
	 * 获取：设备型号
	 */
	public String getDevname() {
		return devname;
	}
	/**
	 * 设置：设备编号
	 */
	public void setDevserial(String devserial) {
		this.devserial = devserial;
	}
	/**
	 * 获取：设备编号
	 */
	public String getDevserial() {
		return devserial;
	}
	/**
	 * 设置：设备证书
	 */
	public void setDevcertificate(String devcertificate) {
		this.devcertificate = devcertificate;
	}
	/**
	 * 获取：设备证书
	 */
	public String getDevcertificate() {
		return devcertificate;
	}
	/**
	 * 设置：设备注册管理员身份证编号
	 */
	public void setAdminorid(String adminorid) {
		this.adminorid = adminorid;
	}
	/**
	 * 获取：设备注册管理员身份证编号
	 */
	public String getAdminorid() {
		return adminorid;
	}
	/**
	 * 设置：设备管理员联系电话
	 */
	public void setAdminorphone(String adminorphone) {
		this.adminorphone = adminorphone;
	}
	/**
	 * 获取：设备管理员联系电话
	 */
	public String getAdminorphone() {
		return adminorphone;
	}
	/**
	 * 设置：监管中心编号
	 */
	public void setSysnumber(String sysnumber) {
		this.sysnumber = sysnumber;
	}
	/**
	 * 获取：监管中心编号
	 */
	public String getSysnumber() {
		return sysnumber;
	}
	/**
	 * 设置：设备使用单位编号
	 */
	public void setDevdepartment(String devdepartment) {
		this.devdepartment = devdepartment;
	}
	/**
	 * 获取：设备使用单位编号
	 */
	public String getDevdepartment() {
		return devdepartment;
	}
	/**
	 * 设置：注册日期
	 */
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	/**
	 * 获取：注册日期
	 */
	public Date getRegdate() {
		return regdate;
	}
}
