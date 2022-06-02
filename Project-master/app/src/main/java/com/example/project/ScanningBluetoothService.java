package com.example.project;

import static com.example.project.MainActivity.MY_TAG;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class ScanningBluetoothService extends Service {

    private BluetoothAdapter bluetooth;

    private IBinder binder = new MyServiceBinder();

    private ArrayList<FindUser> findUserList;

    private boolean scanningOn = false;
    private boolean isQuery = false;

    private Thread scanningThread;

    class MyServiceBinder extends Binder {
        public ScanningBluetoothService getService() {
            return ScanningBluetoothService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        bluetooth = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.i(MY_TAG, "onBind");

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        Log.i(MY_TAG, "onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        scanningOn = true;
        scanningThread = new Thread(new Runnable() {
            @Override
            public void run() {
                scanning();
            }
        });
        scanningThread.start();

        return START_REDELIVER_INTENT;
    }

    private void scanning() {
        synchronized (this) {
            while (scanningOn) {
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
                //Log.i(MY_TAG, "Scanning end");
                if (isQuery) {
                    try {
                        //Log.i(MY_TAG, "Scanning thread wait");
                        wait();
                    } catch (InterruptedException e) {
                        Log.e("Service thread", String.valueOf(e.toString()));
                    }
                    //Log.i(MY_TAG, "Scanning thread stop wait");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(MY_TAG, "Service onDestroy");

        stopScanning();
    }

    private void stopScanning() {
        scanningOn = false;
    }

    public ArrayList<FindUser> getFindUserList() {

        ArrayList<FindUser> findUserList;
        isQuery = true;

        Log.i(MY_TAG, "Query is waiting in thread: " + Thread.currentThread().getId());

        synchronized (this) {

            //Log.i(MY_TAG, "Query have synchronized");

            findUserList = this.findUserList;

            notifyAll();
        }

        isQuery = false;

        return findUserList;
    }

}