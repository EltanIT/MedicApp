package com.example.medicapp.main_screens.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SelectedAnalysis implements Parcelable {

    private Analysis analysis;
    private boolean check;


    public SelectedAnalysis(Analysis analysis, boolean check) {
        this.analysis = analysis;
        this.check = check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public boolean isCheck() {
        return check;
    }

    protected SelectedAnalysis(Parcel in) {
        analysis = in.readParcelable(Analysis.class.getClassLoader());
        check = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(analysis, flags);
        dest.writeByte((byte) (check ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SelectedAnalysis> CREATOR = new Creator<SelectedAnalysis>() {
        @Override
        public SelectedAnalysis createFromParcel(Parcel in) {
            return new SelectedAnalysis(in);
        }

        @Override
        public SelectedAnalysis[] newArray(int size) {
            return new SelectedAnalysis[size];
        }
    };
}
