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
 * @date 2018-10-29 15:05:39
 */
@TableName("deviceinfo")
public class DeviceinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 设备唯一标识
	 */
	private String deviceid;
	/**
	 * 	系统版本号
	 */
	private Integer deviceversion;
	/**
	 * 管理协议版本号
	 */
	private Integer standardversion;
	/**
	 * 系统支持的非对称算法
	 */
	private Integer asymalgability0;
	/**
	 * 系统支持的非对称算法
	 */
	private Integer asymalgability1;
	/**
	 * 系统支持的杂凑算法
	 */
	private Integer hashalgability;
	/**
	 * 系统支持的对称算法
	 */
	private Integer symalgability;
	/**
	 * 	设备IP地址
	 */
	private String deviceipv4;
	/**
	 * 设备名称
	 */
	private String devicename;
	/**
	 * 设备编号/序列号
	 */
	private String deviceserial;
	/**
	 * 设备生产厂商名称
	 */
	private String issuername;
	/**
	 * 设备型号
	 */
	private String devicetype;
	/**
	 * 设备型号(9)
	 */
	private String devname;
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
	 * 支持的最大文件存储空间
	 */
	private Integer buffersize;
	/**
	 * 注册日期
	 */
	private Date regdate;
//	/**
//	 * 总运行时间
//	 */
//	private Integer totalruntime;
	/**
	 * 管理代理表的记录号
	 */
	private Integer agentnum;
	/**
	 * 最近修改时间
	 */
	private Date lastmodifytime;

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
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	/**
	 * 获取：设备唯一标识
	 */
	public String getDeviceid() {
		return deviceid;
	}
	/**
	 * 设置：	系统版本号
	 */
	public void setDeviceversion(Integer deviceversion) {
		this.deviceversion = deviceversion;
	}
	/**
	 * 获取：	系统版本号
	 */
	public Integer getDeviceversion() {
		return deviceversion;
	}
	/**
	 * 设置：管理协议版本号
	 */
	public void setStandardversion(Integer standardversion) {
		this.standardversion = standardversion;
	}
	/**
	 * 获取：管理协议版本号
	 */
	public Integer getStandardversion() {
		return standardversion;
	}
	/**
	 * 设置：系统支持的非对称算法
	 */
	public void setAsymalgability0(Integer asymalgability0) {
		this.asymalgability0 = asymalgability0;
	}
	/**
	 * 获取：系统支持的非对称算法
	 */
	public Integer getAsymalgability0() {
		return asymalgability0;
	}
	/**
	 * 设置：系统支持的非对称算法
	 */
	public void setAsymalgability1(Integer asymalgability1) {
		this.asymalgability1 = asymalgability1;
	}
	/**
	 * 获取：系统支持的非对称算法
	 */
	public Integer getAsymalgability1() {
		return asymalgability1;
	}
	/**
	 * 设置：系统支持的杂凑算法
	 */
	public void setHashalgability(Integer hashalgability) {
		this.hashalgability = hashalgability;
	}
	/**
	 * 获取：系统支持的杂凑算法
	 */
	public Integer getHashalgability() {
		return hashalgability;
	}
	/**
	 * 设置：系统支持的对称算法
	 */
	public void setSymalgability(Integer symalgability) {
		this.symalgability = symalgability;
	}
	/**
	 * 获取：系统支持的对称算法
	 */
	public Integer getSymalgability() {
		return symalgability;
	}
	/**
	 * 设置：	设备IP地址
	 */
	public void setDeviceipv4(String deviceipv4) {
		this.deviceipv4 = deviceipv4;
	}
	/**
	 * 获取：	设备IP地址
	 */
	public String getDeviceipv4() {
		return deviceipv4;
	}
	/**
	 * 设置：设备名称
	 */
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	/**
	 * 获取：设备名称
	 */
	public String getDevicename() {
		return devicename;
	}
	/**
	 * 设置：设备编号/序列号
	 */
	public void setDeviceserial(String deviceserial) {
		this.deviceserial = deviceserial;
	}
	/**
	 * 获取：设备编号/序列号
	 */
	public String getDeviceserial() {
		return deviceserial;
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
	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	/**
	 * 获取：设备型号
	 */
	public String getDevicetype() {
		return devicetype;
	}
	/**
	 * 设置：设备型号(9)
	 */
	public void setDevname(String devname) {
		this.devname = devname;
	}
	/**
	 * 获取：设备型号(9)
	 */
	public String getDevname() {
		return devname;
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
	 * 设置：支持的最大文件存储空间
	 */
	public void setBuffersize(Integer buffersize) {
		this.buffersize = buffersize;
	}
	/**
	 * 获取：支持的最大文件存储空间
	 */
	public Integer getBuffersize() {
		return buffersize;
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
//	/**
//	 * 设置：总运行时间
//	 */
//	public void setTotalruntime(Integer totalruntime) {
//		this.totalruntime = totalruntime;
//	}
//	/**
//	 * 获取：总运行时间
//	 */
//	public Integer getTotalruntime() {
//		return totalruntime;
//	}
	/**
	 * 设置：管理代理表的记录号
	 */
	public void setAgentnum(Integer agentnum) {
		this.agentnum = agentnum;
	}
	/**
	 * 获取：管理代理表的记录号
	 */
	public Integer getAgentnum() {
		return agentnum;
	}
	/**
	 * 设置：最近修改时间
	 */
	public void setLastmodifytime(Date lastmodifytime) {
		this.lastmodifytime = lastmodifytime;
	}
	/**
	 * 获取：最近修改时间
	 */
	public Date getLastmodifytime() {
		return lastmodifytime;
	}


	@Override
	public String toString() {
		return "DeviceinfoEntity{" +
				"recnumber=" + recnumber +
				", deviceid='" + deviceid + '\'' +
				", deviceversion=" + deviceversion +
				", standardversion=" + standardversion +
				", asymalgability0=" + asymalgability0 +
				", asymalgability1=" + asymalgability1 +
				", hashalgability=" + hashalgability +
				", symalgability=" + symalgability +
				", deviceipv4='" + deviceipv4 + '\'' +
				", devicename='" + devicename + '\'' +
				", deviceserial='" + deviceserial + '\'' +
				", issuername='" + issuername + '\'' +
				", devicetype='" + devicetype + '\'' +
				", devname='" + devname + '\'' +
				", devcertificate='" + devcertificate + '\'' +
				", adminorid='" + adminorid + '\'' +
				", adminorphone='" + adminorphone + '\'' +
				", sysnumber='" + sysnumber + '\'' +
				", devdepartment='" + devdepartment + '\'' +
				", buffersize=" + buffersize +
				", regdate=" + regdate +
				", agentnum=" + agentnum +
				", lastmodifytime=" + lastmodifytime +
				'}';
	}
}
