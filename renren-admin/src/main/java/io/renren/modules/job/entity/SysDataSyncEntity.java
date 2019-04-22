package io.renren.modules.job.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2019-01-16 19:01:34
 */
@TableName("sys_data_sync")
public class SysDataSyncEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 同步类型
	 */
	private String syncType;
	/**
	 * 最近同步时间
	 */
	private Date lastsynctime;

	/**
	 * 设置：
	 */
	public void setRecnumber(Integer recnumber) {
		this.recnumber = recnumber;
	}
	/**
	 * 获取：
	 */
	public Integer getRecnumber() {
		return recnumber;
	}
	/**
	 * 设置：同步类型
	 */
	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}
	/**
	 * 获取：同步类型
	 */
	public String getSyncType() {
		return syncType;
	}
	/**
	 * 设置：最近同步时间
	 */
	public void setLastsynctime(Date lastsynctime) {
		this.lastsynctime = lastsynctime;
	}
	/**
	 * 获取：最近同步时间
	 */
	public Date getLastsynctime() {
		return lastsynctime;
	}
}
