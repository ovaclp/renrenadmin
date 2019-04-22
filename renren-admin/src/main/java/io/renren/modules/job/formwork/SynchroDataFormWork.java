package io.renren.modules.job.formwork;

import java.io.Serializable;

public class SynchroDataFormWork implements Serializable {

    private static final long serialVersionUID = 7196752398368093575L;

    private String deviceDynSync;
    private String deviceInfoSync;
    private String centerInfoSync;

    public String getDeviceDynSync() {
        return deviceDynSync;
    }

    public void setDeviceDynSync(String deviceDynSync) {
        this.deviceDynSync = deviceDynSync;
    }

    public String getDeviceInfoSync() {
        return deviceInfoSync;
    }

    public void setDeviceInfoSync(String deviceInfoSync) {
        this.deviceInfoSync = deviceInfoSync;
    }

    public String getCenterInfoSync() {
        return centerInfoSync;
    }

    public void setCenterInfoSync(String centerInfoSync) {
        this.centerInfoSync = centerInfoSync;
    }

}
