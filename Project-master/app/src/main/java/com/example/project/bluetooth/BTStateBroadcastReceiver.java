package com.example.project.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.project.SendInfo;

public class BTStateBroadcastReceiver extends BroadcastReceiver {

    private Context context;

    public BTStateBroadcastReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            ((SendInfo) context).sendBluetoothState(state);

        }

    }
}
