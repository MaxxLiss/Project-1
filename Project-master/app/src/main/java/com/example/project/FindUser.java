package com.example.project;

import androidx.appcompat.widget.AppCompatButton;

public class FindUser {

    private String deviceName;
    private String songName;
    private String address;

    public FindUser(String deviceName, String songName) {
        this.deviceName = deviceName;
        this.songName = songName;
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
