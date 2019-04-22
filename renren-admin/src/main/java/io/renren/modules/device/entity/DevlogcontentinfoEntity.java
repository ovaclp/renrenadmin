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
 * @date 2018-11-09 15:00:25
 */
@TableName("devlogcontentinfo")
public class DevlogcontentinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 日志内容ID
	 */
	private String logcontent;
	/**
	 * 日志具体内容
	 */
	private String logmeaning;

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
	 * 设置：日志内容ID
	 */
	public void setLogcontent(String logcontent) {
		this.logcontent = logcontent;
	}
	/**
	 * 获取：日志内容ID
	 */
	public String getLogcontent() {
		return logcontent;
	}
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
}
