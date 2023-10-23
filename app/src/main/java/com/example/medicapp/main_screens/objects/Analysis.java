package com.example.medicapp.main_screens.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Analysis implements Parcelable {

    private int id;
    private String title;
    private String description;
    private String processDescription;
    private String deadline;
    private String material;
    private int price;
    private String currency;

    public Analysis(String title, String description, String processDescription, String deadline, String material, int price, String currency) {
        this.title = title;
        this.description = description;
        this.processDescription = processDescription;
        this.deadline = deadline;
        this.material = material;
        this.price = price;
        this.currency = currency;
    }
    public Analysis(){

    }

    @Override
    public boolean equals(@Nullable Object obj) {
//        Analysis analysis = (Analysis) obj;
//        assert analysis != null;
//        return Objects.equals(title, analysis.getTitle());

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Analysis that = (Analysis) obj;
        return title.equals(that.getTitle());
//&& price == analysis.getPrice() && Objects.equals(deadline, analysis.getDeadline()
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcessDescription() {
        return processDescription;
    }

    public void setProcessDescription(String processDescription) {
        this.processDescription = processDescription;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(processDescription);
        parcel.writeString(deadline);
        parcel.writeString(material);
        parcel.writeInt(price);
        parcel.writeInt(Integer.parseInt(currency));
    }
    protected Analysis(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        processDescription = in.readString();
        deadline = in.readString();
        material = in.readString();
        price = in.readInt();
        currency = String.valueOf(in.readInt());
    }
    public static final Creator<Analysis> CREATOR = new Creator<Analysis>() {
        @Override
        public Analysis createFromParcel(Parcel in) {
            return new Analysis(in);
        }

        @Override
        public Analysis[] newArray(int size) {
            return new Analysis[size];
        }
    };

}
