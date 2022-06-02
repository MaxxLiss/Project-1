package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.fragments.FragmentSettings;
import com.example.project.fragments.FragmentSongList;
import com.example.project.fragments.SendInfoFromFragment;
import com.example.project.workers.ScanningBluetoothWorker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SendInfoFromFragment {

    public static final String MY_TAG = "MY_TAG";
    public static final int SET_NEW_FIND_USER_LIST = 0;

    private MenuItem to_settings;
    private MenuItem bluetooth_state;
    private MenuItem to_main_screen;

    ////
    private TextView test;
    ////

    private SwipeRefreshLayout refresh_layout;

    private WorkManager workManager;

    private FragmentSongList fragmentSongList;
    private FragmentSettings fragmentSettings;
    private final FragmentManager fm = getSupportFragmentManager();

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////
        test = findViewById(R.id.test);
        ////

        refresh_layout = findViewById(R.id.refresh_layout);

//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                switch (msg.what) {
//                    case SET_NEW_FIND_USER_LIST:
//                        ArrayList<FindUser> findUserList = (ArrayList<FindUser>) msg.obj;
//                        if (findUserList != null) {
//                            fragmentSongList.updateSongList(findUserList);
//                        } else {
//                            Toast.makeText(MainActivity.this, "Попробуйте обновить снова", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                }
//            }
//        };

        ////

        ArrayList<FindUser> findUserList = new ArrayList<FindUser>();
        for (int i = 0; i < 100; i++) {
            findUserList.add(i, new FindUser("aaa", "bbb"));
        }
        ////

        FragmentTransaction ft = fm.beginTransaction();
        ////
        fragmentSongList = new FragmentSongList(findUserList);
        ////
        fragmentSettings = new FragmentSettings();
        ft.add(R.id.main_screen, fragmentSongList);
        ft.commit();

        workManager = WorkManager.getInstance(MainActivity.this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkBluetoothState();

                ArrayList<FindUser> findUserList = new ArrayList<FindUser>();
                for (int i = 0; i < 100; i++) {
                    findUserList.add(i, new FindUser("bbb", "aaa"));
                }

                fragmentSongList.updateSongList(findUserList);

                refresh_layout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.bluetooth_state:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if (!bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.enable();
                    } else {
                        bluetoothAdapter.disable();
                    }
                }
                checkBluetoothState();
                break;

            case R.id.to_settings:
                to_settings.setVisible(false);
                to_main_screen.setVisible(true);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_screen, fragmentSettings);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.to_main_screen:
                to_main_screen.setVisible(false);
                to_settings.setVisible(true);
                ft = fm.beginTransaction();
                ft.replace(R.id.main_screen, fragmentSongList);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        to_settings = menu.findItem(R.id.to_settings);
        bluetooth_state = menu.findItem(R.id.bluetooth_state);
        to_main_screen = menu.findItem(R.id.to_main_screen);

        checkBluetoothState();

        return super.onCreateOptionsMenu(menu);
    }

    private boolean checkBluetoothState() {
        if (bluetoothAdapter.isEnabled()) {
            bluetooth_state.setIcon(R.drawable.disable_bluetooth);
        } else {
            bluetooth_state.setIcon(R.drawable.enable_bluetooth);
        }
        return bluetoothAdapter.isEnabled();
    }

//    void bindService() {
//
//        Log.i(MY_TAG, "bindService()");
//        //Log.i(MY_TAG, "isBluetoothServiceBound " + isBluetoothServiceBound);
//
//        if (bluetoothServiceConnection == null) {
//            bluetoothServiceConnection = new ServiceConnection() {
//                @Override
//                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                    isBluetoothServiceBound = true;
//                    ScanningBluetoothService.MyServiceBinder myServiceBinder = (ScanningBluetoothService.MyServiceBinder) iBinder;
//                    scanningBluetoothService = myServiceBinder.getService();
//                }
//
//                @Override
//                public void onServiceDisconnected(ComponentName componentName) {
//                    isBluetoothServiceBound = false;
//                }
//            };
//        }
//        bindService(bluetoothServiceIntent, bluetoothServiceConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    void unbindService() {
//
//        Log.i(MY_TAG, "unbindService()");
//        //Log.i(MY_TAG, "isBluetoothServiceBound " + isBluetoothServiceBound);
//
//        if (isBluetoothServiceBound) {
//            unbindService(bluetoothServiceConnection);
//            isBluetoothServiceBound = false;
//        }
//
//        //Log.i(MY_TAG, "bluetoothServiceConnection is null " + (bluetoothServiceConnection == null));
//    }

    @Override
    public void sendNumber(int number) {

        test.setText(String.valueOf(number));

    }

    @Override
    protected void onStart() {
        Toast.makeText(MainActivity.this, "Попробуйте обновить экран", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

}