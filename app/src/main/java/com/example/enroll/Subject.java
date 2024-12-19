package com.example.enroll;

import android.os.Parcel;
import android.os.Parcelable;

public class Subject implements Parcelable {
    private String name;
    private int credit;

    public Subject() {
    }

    public Subject(String name, int credit) {
        this.name = name;
        this.credit = credit;
    }

    protected Subject(Parcel in) {
        name = in.readString();
        credit = in.readInt();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getCredit() {
        return credit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(credit);
    }
}
