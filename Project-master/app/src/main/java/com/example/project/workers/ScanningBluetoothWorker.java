package com.example.project.workers;

import static com.example.project.MainActivity.MY_TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.project.FindUser;
import com.example.project.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScanningBluetoothWorker extends Worker {

    private boolean scanningOn = false;
    private ArrayList<FindUser> findUserList;

    public ScanningBluetoothWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        scanningOn = getInputData().getBoolean("Long Scanning", false);

        scanningOn = true;
        scanning();

        HashMap<String, FindUser> dataMap = new HashMap<String, FindUser>();

        for (int i = 0; i < findUserList.size(); i++) {
            dataMap.put("FindUser" + i, findUserList.get(i));
        }

        Data output = new Data.Builder()
                .put("aaa", findUserList)
                //.putAll((Map<String, Object>) dataMap)
                .putBoolean("Result in null", findUserList.size() == 0)
                .build();

        return ListenableWorker.Result.success();
    }

    private void scanning() {
        do {
            Log.i(MY_TAG, "Service is working in thread " + Thread.currentThread().getId());
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                Log.e("Service Sleeping", String.valueOf(e.toString()));
            }
            findUserList = new ArrayList<FindUser>();
            for (int i = 0; i < 100; i++) {
                findUserList.add(new FindUser(String.valueOf(new Random().nextInt()), String.valueOf(new Random().nextInt())));
            }
        } while (scanningOn);
    }

}
