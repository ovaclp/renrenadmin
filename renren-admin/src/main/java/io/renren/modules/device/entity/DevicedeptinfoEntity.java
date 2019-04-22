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
 * @date 2018-11-09 21:32:24
 */
@TableName("devicedeptinfo")
public class DevicedeptinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 设备使用单位编号
	 */
	private String devdepartment;
	/**
	 * 设备使用单位名称
	 */
	private String devdeptname;

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
	 * 设置：设备使用单位名称
	 */
	public void setDevdeptname(String devdeptname) {
		this.devdeptname = devdeptname;
	}
	/**
	 * 获取：设备使用单位名称
	 */
	public String getDevdeptname() {
		return devdeptname;
	}
}
