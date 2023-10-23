package com.example.medicapp.main_screens.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Order {
    private Profile profile;
    private List<SelectedAnalysis> analysisList;

    public Profile getProfile() {
        return profile;
    }
    public List<SelectedAnalysis> getAnalysisList() {
        return analysisList;
    }

    public void setAnalysisList(List<SelectedAnalysis> analysisList) {
        this.analysisList = analysisList;
    }
    public Order(Profile profile, List<SelectedAnalysis> analysis) {
        this.profile = profile;
        this.analysisList = analysis;
    }

    public static class ClientAddress implements Parcelable{
        public String address;
        public int longitude;
        public int width;
        public int height;
        public int flat;
        public int entrance;
        public int floor;
        public String intercom;
        public int buildingType;

        protected ClientAddress(Parcel in) {
            address = in.readString();
            longitude = in.readInt();
            width = in.readInt();
            height = in.readInt();
            flat = in.readInt();
            entrance = in.readInt();
            floor = in.readInt();
            intercom = in.readString();
            buildingType = in.readInt();
        }
        ClientAddress(){

        }


        public int getLongitude() {
            return longitude;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getFlat() {
            return flat;
        }

        public int getEntrance() {
            return entrance;
        }

        public int getFloor() {
            return floor;
        }

        public String getIntercom() {
            return intercom;
        }

        public int getBuildingType() {
            return buildingType;
        }

        public String getAddress() {
            return address;
        }

        public ClientAddress(String address, int longitude, int width, int height, int flat, int entrance, int floor, String intercom, int buildingType) {
            this.address = address;
            this.longitude = longitude;
            this.width = width;
            this.height = height;
            this.flat = flat;
            this.entrance = entrance;
            this.floor = floor;
            this.intercom = intercom;
            this.buildingType = buildingType;
        }


        public static final Creator<ClientAddress> CREATOR = new Creator<ClientAddress>() {
            @Override
            public ClientAddress createFromParcel(Parcel in) {
                return new ClientAddress(in);
            }

            @Override
            public ClientAddress[] newArray(int size) {
                return new ClientAddress[size];
            }
        };
        @Override
        public int describeContents() {
            return 0;
        }
        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(address);
            parcel.writeInt(longitude);
            parcel.writeInt(width);
            parcel.writeInt(height);
            parcel.writeInt(flat);
            parcel.writeInt(entrance);
            parcel.writeInt(floor);
            parcel.writeString(intercom);
            parcel.writeInt(buildingType);
        }
    }

}


