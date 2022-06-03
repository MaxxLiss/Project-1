package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.project.bluetooth.BTStateBroadcastReceiver;
import com.example.project.fragments.FragmentSettings;
import com.example.project.fragments.FragmentSongList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SendInfo {

    public static final String MY_TAG = "MY_TAG";
    private static final String NAME = "MusicAround";
    public static final String FIND_MUSIC_APP = "FindMusicApp";

    private static String deviceName;

    public static final int PERMISSION_REQUEST_BLUETOOTH_CONNECT = 143;
    public static final int PERMISSION_REQUEST_BLUETOOTH_SCAN = 144;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 145;
    public static final int PERMISSION_REQUEST_BLUETOOTH = 146;
    private static final int PERMISSION_REQUEST_BLUETOOTH_ADMIN = 147;

    private MenuItem to_settings;
    private MenuItem bluetooth_state;
    private MenuItem to_main_screen;

    private SwipeRefreshLayout refresh_layout;

    private FragmentManager fm;
    private FragmentSongList fragmentSongList;
    private FragmentSettings fragmentSettings;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothScanner;

    private BTStateBroadcastReceiver btStateBroadcastReceiver;

    private List<FindUser> findUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_CONNECT);

        } else {

            deviceName = bluetoothAdapter.getName();
            bluetoothAdapter.setName(FIND_MUSIC_APP + "*" + bluetoothAdapter.getName());

        }

        btStateBroadcastReceiver = new BTStateBroadcastReceiver(getApplicationContext());

        refresh_layout = findViewById(R.id.refresh_layout);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragmentSongList = new FragmentSongList();
        fragmentSettings = new FragmentSettings(deviceName);
        ft.replace(R.id.main_screen, fragmentSongList);
        ft.commit();

        findUserList = new ArrayList<>();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_CONNECT);
//
//        }
//
//        ScanSettings scanSettings = new ScanSettings.Builder()
//                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES).build();
//
//        bluetoothScanner.startScan(null, scanSettings, scanCallback);


        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.bluetooth_state:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_CONNECT);

                }

                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                    bluetooth_state.setTitle("Выключить bluetooth");
                } else {
                    bluetoothAdapter.disable();
                    bluetooth_state.setTitle("Включить bluetooth");
                }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_BLUETOOTH_CONNECT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_CONNECT);

                    }
                }
                break;

            case PERMISSION_REQUEST_BLUETOOTH_SCAN:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_REQUEST_BLUETOOTH_SCAN);

                    }
                }
                break;

            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);

                    }
                }
                break;

            case PERMISSION_REQUEST_BLUETOOTH:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_REQUEST_BLUETOOTH);

                    }
                }

            case PERMISSION_REQUEST_BLUETOOTH_ADMIN:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_REQUEST_BLUETOOTH_ADMIN);

                    }
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        to_settings = menu.findItem(R.id.to_settings);
        bluetooth_state = menu.findItem(R.id.bluetooth_state);
        to_main_screen = menu.findItem(R.id.to_main_screen);

        if (!bluetoothAdapter.isEnabled()) {
            bluetooth_state.setIcon(R.drawable.enable_bluetooth);
            bluetooth_state.setTitle("Выключить bluetooth");
        } else {
            bluetooth_state.setIcon(R.drawable.disable_bluetooth);
            bluetooth_state.setTitle("Включить bluetooth");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void sendNumber(int number) {

        //test.setText(String.valueOf(number));

    }

    private void requestPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_REQUEST_BLUETOOTH_SCAN);

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_REQUEST_BLUETOOTH);

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_REQUEST_BLUETOOTH_ADMIN);

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_CONNECT);

            }
        }
    }

    @Override
    public void sendNewDeviceName(String deviceName) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_CONNECT);

        } else {

            MainActivity.deviceName = deviceName;
            bluetoothAdapter.setName(FIND_MUSIC_APP + "*" + bluetoothAdapter.getName());

        }

    }

    @Override
    public void sendBluetoothState(int bluetoothState) {

        switch (bluetoothState) {
            case BluetoothAdapter.STATE_ON:
                bluetooth_state.setIcon(R.drawable.enable_bluetooth);
                break;

            case BluetoothAdapter.STATE_OFF:
                bluetooth_state.setIcon(R.drawable.disable_bluetooth);
                break;

            default:
                bluetooth_state.setIcon(R.drawable.changing_bluetooth);
                break;
        }

    }

    @Override
    protected void onDestroy() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_CONNECT);

        } else {

            bluetoothAdapter.setName(deviceName);

        }

        //unregisterReceiver(receiver);

        super.onDestroy();
    }
}