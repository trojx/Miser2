package com.jx.miser2;

/**
 * Created by li on 16-9-10.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class BeanSMS implements Parcelable {

    public String mMsg;//信息内容
    public String mTime;
    public String mName;
    public String mImgUrl;

    public BeanSMS(){

    }

    public BeanSMS(Parcel source){
        this.mMsg = source.readString();
        this.mTime = source.readString();
        this.mName = source.readString();
        this.mImgUrl = source.readString();
    }
    public BeanSMS(String name, String time, String msg, String img){
        this.mMsg=msg;
        this.mTime=time;
        this.mName=name;
        this.mImgUrl=img;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMsg);
        parcel.writeString(mTime);
        parcel.writeString(mName);
        parcel.writeString(mImgUrl);

    }

    public static final Creator<BeanSMS> CREATOR = new Creator<BeanSMS>(){

        @Override
        public BeanSMS createFromParcel(Parcel source) {
            return new BeanSMS(source);
        }

        @Override
        public BeanSMS[] newArray(int size) {
            return new BeanSMS[size];
        }

    };

}
