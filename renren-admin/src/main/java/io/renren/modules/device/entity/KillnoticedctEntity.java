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
 * @date 2018-12-04 17:35:14
 */
@TableName("killnoticedct")
public class KillnoticedctEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 警告消息编码
	 */
	private String notice;
	/**
	 * 警告消息含义
	 */
	private String noticemeans;

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
	 * 设置：警告消息编码
	 */
	public void setNotice(String notice) {
		this.notice = notice;
	}
	/**
	 * 获取：警告消息编码
	 */
	public String getNotice() {
		return notice;
	}
	/**
	 * 设置：警告消息含义
	 */
	public void setNoticemeans(String noticemeans) {
		this.noticemeans = noticemeans;
	}
	/**
	 * 获取：警告消息含义
	 */
	public String getNoticemeans() {
		return noticemeans;
	}
}
