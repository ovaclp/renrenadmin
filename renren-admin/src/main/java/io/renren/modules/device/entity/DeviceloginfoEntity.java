package io.renren.modules.device.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
@TableName("deviceloginfo")
public class DeviceloginfoEntity implements Serializable {
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
	 * 日志级别
	 */
	private Integer loglevel;
	/**
	 * 日志时间
	 */
	private Timestamp  logtime;
	/**
	 * 日志内容
	 */
	private String logcontent;

	public Timestamp getLogtime() {
		return logtime;
	}

	public void setLogtime(Timestamp logtime) {
		this.logtime = logtime;
	}

	/**
	 * 日志具体内容
	 */
	@TableField(exist=false)
	private String logmeaning;

	/**
	 * 设置：日志具体内容
	 */
	public void setLogmeaning(String logmeaning) {
		this.logmeaning = logmeaning;
	}
	/**
	 * 获取：日志具体内容
	 */
	public String getLogmeaning() {
		return logmeaning;
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
	 * 设置：日志级别
	 */
	public void setLoglevel(Integer loglevel) {
		this.loglevel = loglevel;
	}
	/**
	 * 获取：日志级别
	 */
	public Integer getLoglevel() {
		return loglevel;
	}

	/**
	 * 设置：日志内容
	 */
	public void setLogcontent(String logcontent) {
		this.logcontent = logcontent;
	}
	/**
	 * 获取：日志内容
	 */
	public String getLogcontent() {
		return logcontent;
	}

	@Override
	public String toString() {
		return "DeviceloginfoEntity{" +
				"recnumber=" + recnumber +
				", devid='" + devid + '\'' +
				", loglevel=" + loglevel +
				", logtime=" + logtime +
				", logcontent='" + logcontent + '\'' +
				", logmeaning='" + logmeaning + '\'' +
				'}';
	}
}
