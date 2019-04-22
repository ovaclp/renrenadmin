package io.renren.modules.device.entity;

public class DeviceidIpv4DevstateEntity {


    /**
     * 设备唯一标识
     */
    private String devid;

    /**
     * 	设备IP地址
     */

    //private String deviceipv4;

    /**
     * 设备状态
     */
    private String devstate;

    /**
     *计数器
     */

    private int countflag;
    private int row_number;

    public int getRow_number() {
        return row_number;
    }

    public void setRow_number(int row_number) {
        this.row_number = row_number;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    /*public String getDeviceipv4() {
        return deviceipv4;
    }

    public void setDeviceipv4(String deviceipv4) {
        this.deviceipv4 = deviceipv4;
    }*/

    public String getDevstate() {
        return devstate;
    }

    public void setDevstate(String devstate) {
        this.devstate = devstate;
    }

    public int getCount() {
        return countflag;
    }

    public void setCount(int count) {
        this.countflag = count;
    }

    @Override
    public String toString() {
        return "DeviceidIpv4DevstateEntity{" +
                "devid='" + devid + '\'' +
                ", devstate='" + devstate + '\'' +
                ", countflag=" + countflag +
                ", row_number=" + row_number +
                '}';
    }
}
