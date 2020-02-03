package com.codingwell.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.IllegalViewOperationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class WifiManagerModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;
    private WifiManager wifi;

    WifiManagerModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
        wifi = (WifiManager)reactContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public String getName() {
        return "WifiManager";
    }

    @ReactMethod
    public void log(String message) {
        Log.d("WifiManager",message);
    }

    @ReactMethod
    public void checkPermission() {

        List<String> permissionsList = new ArrayList<String>();

        Context context = reactContext.getApplicationContext();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.CHANGE_WIFI_STATE);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(reactContext.getCurrentActivity(), permissionsList.toArray(new String[permissionsList.size()]),
                    1);
        }
    }

    @ReactMethod
    public void scanWifi() {
        wifi.startScan();
    }

    @ReactMethod
    public void listWifi(Promise promise) {
        try {
            List<ScanResult> results = wifi.getScanResults();
            WritableArray wifiArray = Arguments.createArray();

            for (ScanResult result: results) {
                WritableMap wifiObject = Arguments.createMap();
                if(!result.SSID.equals("")){
                        wifiObject.putString("SSID", result.SSID);
                        wifiObject.putString("BSSID", result.BSSID);
                        wifiObject.putString("capabilities", result.capabilities);
                        wifiObject.putInt("frequency", result.frequency);
                        wifiObject.putInt("level", result.level);
                        wifiObject.putDouble("timestamp", result.timestamp);
                        //Other fields not added
                        wifiObject.putString("operatorFriendlyName", result.operatorFriendlyName.toString());
                        wifiObject.putString("venueName", result.venueName.toString());
                        wifiObject.putInt("centerFreq0", result.centerFreq0);
                        wifiObject.putInt("centerFreq1", result.centerFreq1);
                        wifiObject.putInt("channelWidth", result.channelWidth);
                    wifiArray.pushMap(wifiObject);
                }
            }
            promise.resolve(wifiArray);
        } catch (IllegalViewOperationException e) {
            promise.reject(e);
        }
    }
}
