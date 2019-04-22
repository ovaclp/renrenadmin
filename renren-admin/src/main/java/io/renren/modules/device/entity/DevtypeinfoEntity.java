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
 * @date 2018-11-07 15:59:57
 */
@TableName("devtypeinfo")
public class DevtypeinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 设备型号
	 */
	private String devname;
	/**
	 * 设备名称
	 */
	private String devicename;

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
}
