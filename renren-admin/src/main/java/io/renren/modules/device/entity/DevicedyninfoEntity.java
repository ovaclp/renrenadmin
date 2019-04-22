package io.renren.modules.device.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableField;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author cutegl
 * @email cutegl@mail.ustc.edu.cn
 * @date 2018-10-29 15:05:39
 */
@TableName("devicedyninfo")
public class DevicedyninfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 索引号
	 */
	@TableId
	private Integer recnumber;
	/**
	 * 设备唯一标识
	 */
	private String devid;
	/**
	 * 密码服务状态
	 */
	private String encservicestate;
	/**
	 * 密钥失步监视
	 */
	private String keyversion;
	/**
	 * 密钥更新状态
	 */
	private String keyupdatestate;
	/**
	 * 隧道联通性
	 */
	private String tunnelconnectivity;
	/**
	 * IP地址是否变化
	 */
	private String devipstate;
	/**
	 * 密钥算法使用率
	 */
	private Integer algrescontion;
	/**
	 * 密钥资源利用率
	 */
	private Long keyrescondition;
	/**
	 * 设备状态
	 */
	private String devstate;
	/**
	 * 随机数发生器校验状态
	 */
	private String rngCheck;
	/**
	 * 关键程序校验
	 */
	private String kernelprogcheck;
	/**
	 * 警告消息编码
	 */
	private String notice;
	/**
	 * 最近修改时间
	 */
	private Date lastmodifytime;

	@TableField(exist = false)
	public  int row_number;

	public int getRow_number() {
		return row_number;
	}

	public void setRow_number(int row_number) {
		this.row_number = row_number;
	}

	/**
	 * 设置：索引号
	 */
	public void setRecnumber(Integer recnumber) {
		this.recnumber = recnumber;
	}
	/**
	 * 获取：索引号
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
	 * 设置：密码服务状态
	 */
	public void setEncservicestate(String encservicestate) {
		this.encservicestate = encservicestate;
	}
	/**
	 * 获取：密码服务状态
	 */
	public String getEncservicestate() {
		return encservicestate;
	}
	/**
	 * 设置：密钥版本号/密钥更新日期
	 */
	public void setKeyversion(String keyversion) {
		this.keyversion = keyversion;
	}
	/**
	 * 获取：密钥版本号/密钥更新日期
	 */
	public String getKeyversion() {
		return keyversion;
	}
	/**
	 * 设置：密钥更新状态
	 */
	public void setKeyupdatestate(String keyupdatestate) {
		this.keyupdatestate = keyupdatestate;
	}
	/**
	 * 获取：密钥更新状态
	 */
	public String getKeyupdatestate() {
		return keyupdatestate;
	}
	/**
	 * 设置：隧道联通性
	 */
	public void setTunnelconnectivity(String tunnelconnectivity) {
		this.tunnelconnectivity = tunnelconnectivity;
	}
	/**
	 * 获取：隧道联通性
	 */
	public String getTunnelconnectivity() {
		return tunnelconnectivity;
	}
	/**
	 * 设置：IP地址是否变化
	 */
	public void setDevipstate(String devipstate) {
		this.devipstate = devipstate;
	}
	/**
	 * 获取：IP地址是否变化
	 */
	public String getDevipstate() {
		return devipstate;
	}
	/**
	 * 设置：密钥算法使用率
	 */
	public void setAlgrescontion(Integer algrescontion) {
		this.algrescontion = algrescontion;
	}
	/**
	 * 获取：密钥算法使用率
	 */
	public Integer getAlgrescontion() {
		return algrescontion;
	}
	/**
	 * 设置：密钥资源利用率
	 */
	public void setKeyrescondition(Long keyrescondition) {
		this.keyrescondition = keyrescondition;
	}
	/**
	 * 获取：密钥资源利用率
	 */
	public Long getKeyrescondition() {
		return keyrescondition;
	}
	/**
	 * 设置：设备状态
	 */
	public void setDevstate(String devstate) {
		this.devstate = devstate;
	}
	/**
	 * 获取：设备状态
	 */
	public String getDevstate() {
		return devstate;
	}
	/**
	 * 设置：随机数发生器校验状态
	 */
	public void setRngCheck(String rngCheck) {
		this.rngCheck = rngCheck;
	}
	/**
	 * 获取：随机数发生器校验状态
	 */
	public String getRngCheck() {
		return rngCheck;
	}
	/**
	 * 设置：关键程序校验
	 */
	public void setKernelprogcheck(String kernelprogcheck) {
		this.kernelprogcheck = kernelprogcheck;
	}
	/**
	 * 获取：关键程序校验
	 */
	public String getKernelprogcheck() {
		return kernelprogcheck;
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
		return "DevicedyninfoEntity{" +
				"recnumber=" + recnumber +
				", devid='" + devid + '\'' +
				", encservicestate='" + encservicestate + '\'' +
				", keyversion='" + keyversion + '\'' +
				", keyupdatestate='" + keyupdatestate + '\'' +
				", tunnelconnectivity='" + tunnelconnectivity + '\'' +
				", devipstate='" + devipstate + '\'' +
				", algrescontion=" + algrescontion +
				", keyrescondition=" + keyrescondition +
				", devstate='" + devstate + '\'' +
				", rngCheck='" + rngCheck + '\'' +
				", kernelprogcheck='" + kernelprogcheck + '\'' +
				", notice='" + notice + '\'' +
				", lastmodifytime=" + lastmodifytime +
				", row_number=" + row_number +
				'}';
	}
}
