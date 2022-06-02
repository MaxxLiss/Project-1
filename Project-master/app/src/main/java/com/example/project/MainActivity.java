package com.example.project;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.fragments.FragmentSettings;
import com.example.project.fragments.FragmentSongList;
import com.example.project.fragments.SendInfoFromFragment;
import com.example.project.workers.ScanningBluetoothWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements SendInfoFromFragment {

    public static final String MY_TAG = "MY_TAG";
    private static final String NAME = "MusicAround";
    public static final int BLUETOOTH_CONNECT_REQUEST_CODE = 143;
    public static final int BLUETOOTH_SCAN_REQUEST_CODE = 143;

    private MenuItem to_settings;
    private MenuItem bluetooth_state;
    private MenuItem to_main_screen;

    ////
    private TextView test;
    ////

    private SwipeRefreshLayout refresh_layout;

    private WorkManager workManager;

    private FragmentManager fm;
    private FragmentSongList fragmentSongList;
    private FragmentSettings fragmentSettings;

    private BluetoothAdapter bluetoothAdapter;
    private final MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    //Intent discoverableBluetoothIntent;
    //private final int BLUETOOTH_CONNECT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter stateChangedFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver, foundFilter);
        registerReceiver(receiver, stateChangedFilter);

        //discoverableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        //discoverableBluetoothIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 10 * 60);

        ////
        test = findViewById(R.id.test);
        ////

        refresh_layout = findViewById(R.id.refresh_layout);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragmentSongList = new FragmentSongList();
        fragmentSettings = new FragmentSettings();
        ft.replace(R.id.main_screen, fragmentSongList);
        ft.commit();

        workManager = WorkManager.getInstance(MainActivity.this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //startActivityForResult(discoverableBluetoothIntent, BLUETOOTH_CONNECT_CODE);

                if (checkBluetoothState()) {

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        if (bluetoothAdapter.startDiscovery()) {
                            Log.i(MY_TAG, "Discovery start");
                        }
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, BLUETOOTH_SCAN_REQUEST_CODE);
                    }

                    fragmentSongList.updateSongList(receiver.getFindUserList());

                } else {

                    Toast.makeText(MainActivity.this, "Включите bluetooth", Toast.LENGTH_SHORT).show();

                }
                refresh_layout.setRefreshing(false);
            }
        });
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private List<FindUser> findUserList;

        public List<FindUser> getFindUserList() {
            return findUserList;
        }

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            findUserList = new ArrayList<>();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    findUserList.add(new FindUser(device.getName(), device.getAddress()));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, BLUETOOTH_CONNECT_REQUEST_CODE);
                }

                Log.i(MY_TAG, "BluetoothDevice found");
            }

        }
    }

    private class BluetoothServerConnection extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        private BluetoothServerConnection() {
            BluetoothServerSocket tmp = null;
            try {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID.randomUUID());
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, BLUETOOTH_CONNECT_REQUEST_CODE);
                }
            } catch (IOException e) {
                Log.e(MY_TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        @Override
        public void run() {


        }
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
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, BLUETOOTH_CONNECT_REQUEST_CODE);
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

    @Override
    public void sendNumber(int number) {

        test.setText(String.valueOf(number));

    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(receiver);

        super.onDestroy();
    }
}