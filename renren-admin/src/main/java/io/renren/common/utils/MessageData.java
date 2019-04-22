package io.renren.common.utils;

/**
 * 代理消息数据格式
 */
public class MessageData {
	//版本号 1
	private String  version="01";
	//安全模式 1
	private String security="00";
	//保留 2
	private String hold="0000";
	//消息ID 8
	private String messageid;
	//PDU长度 4
	private String length;
	//目的方ID（监控中心）32
	private String destinyid;
	//发送方ID（代理）32
	private String sendid;
	//操作类型（0xA3）1
	private String oprtype="A3";
	//密码设备管理应用标识（0x00）1
	private String appid="00";
	//类型
	private String type;
	//MM设备ID
	private String devcieid;
	//密码设备监控消息PDU N
	private byte[] message;
	//安全通道会话密钥对消息头和消息PDU计算的16字节的MAC值 16
	private String mac;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public String getHold() {
		return hold;
	}

	public void setHold(String hold) {
		this.hold = hold;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getDestinyid() {
		return destinyid;
	}

	public void setDestinyid(String destinyid) {
		this.destinyid = destinyid;
	}

	public String getSendid() {
		return sendid;
	}

	public void setSendid(String sendid) {
		this.sendid = sendid;
	}

	public String getOprtype() {
		return oprtype;
	}

	public void setOprtype(String oprtype) {
		this.oprtype = oprtype;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDevcieid() {
		return devcieid;
	}

	public void setDevcieid(String devcieid) {
		this.devcieid = devcieid;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
}
