package com.game.appupdate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 应用更新设置
 * */
public class AppUpdateSetting  implements Parcelable
{
    /** 下载方式 */
    private int downloadType = AppUpdateType.Auto;

    /** apk下载URL */
    private String apkUrl = "";

    /** apk保存路径 */
    private String apkPath = "";

    /** 是否强制更新 */
    private Boolean isForce = false;


    /** 下载方式 */
    public int getDownloadType()
    {
        return downloadType;
    }

    public void setDownloadType(int downloadType) {
        this.downloadType = downloadType;
    }

    /** apk下载URL */
    public  String getApkUrl()
    {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    /** apk保存路径 */
    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    /** 是否强制更新 */
    public Boolean getForce() {
        return isForce;
    }

    public void setForce(Boolean force) {
        isForce = force;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(downloadType);
        dest.writeString(apkUrl);
        dest.writeString(apkPath);
        dest.writeValue(isForce);
    }


    public AppUpdateSetting() {
    }


    protected AppUpdateSetting(Parcel in) {

        downloadType = in.readInt();
        apkUrl = in.readString();
        apkPath = in.readString();
        isForce = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<AppUpdateSetting> CREATOR = new Creator<AppUpdateSetting>() {
        @Override
        public AppUpdateSetting createFromParcel(Parcel in) {
            return new AppUpdateSetting(in);
        }

        @Override
        public AppUpdateSetting[] newArray(int size) {
            return new AppUpdateSetting[size];
        }
    };

}
