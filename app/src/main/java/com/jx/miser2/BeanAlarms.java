package com.jx.miser2;

/**
 * Created by li on 16-9-9.
 */
import android.os.Parcel;
import android.os.Parcelable;


public class BeanAlarms implements Parcelable {
    public String masterIconUrl;
    public String masterName;
    public String sharesname;
    public String time;
    public String wdjg;
    public String type;
    public String number;

    public BeanAlarms(){}
    public BeanAlarms(Parcel source){
        this.masterIconUrl = source.readString();
        this.masterName = source.readString();
        this.sharesname = source.readString();
        this.time = source.readString();
        this.wdjg = source.readString();
        this.type = source.readString();
        this.number = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(masterIconUrl);
        parcel.writeString(masterName);
        parcel.writeString(sharesname);
        parcel.writeString(time);
        parcel.writeString(wdjg);
        parcel.writeString(type);
        parcel.writeString(number);
    }

    public static final Creator<BeanAlarms> CREATOR = new Creator<BeanAlarms>(){

        @Override
        public BeanAlarms createFromParcel(Parcel source) {
            return new BeanAlarms(source);
        }

        @Override
        public BeanAlarms[] newArray(int size) {
            return new BeanAlarms[size];
        }

    };

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof BeanAlarms)){
            return false;
        }
        BeanAlarms ab = (BeanAlarms) obj;

        return (this.sharesname.equals(ab.sharesname)) &&
                (this.type.equals(ab.type)) &&
                 (this.masterName.equals(ab.masterName));

    }
}