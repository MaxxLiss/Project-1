package com.example.project;

import androidx.appcompat.widget.AppCompatButton;

public class FindUser {

    private String deviceName;
    private String songName;
    private String address;

    public FindUser(String deviceName, String address) {
        this.deviceName = deviceName;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getSongName() {
        return songName;
    }
}
