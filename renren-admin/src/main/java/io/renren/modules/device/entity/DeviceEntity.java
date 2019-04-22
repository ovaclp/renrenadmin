package io.renren.modules.device.entity;

import java.io.Serializable;


/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
public class DeviceEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	//部门ID
	private String deptId;
	//上级部门ID，一级部门为0
	private String parentId;
	//部门名称
	private String name;
	//设备名称
	private String deviceName;
	//设备型号（9）
	private String devName;
	//设备唯一标识
	private String DevID;
	//设备状态
	private String Devstate;
	/**
	 * 毙杀通知
	 */
	private String notice;

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getDevID() {
		return DevID;
	}

	public void setDevID(String devID) {
		DevID = devID;
	}

	public String getDevstate() {
		return Devstate;
	}

	public void setDevstate(String devstate) {
		Devstate = devstate;
	}
}
