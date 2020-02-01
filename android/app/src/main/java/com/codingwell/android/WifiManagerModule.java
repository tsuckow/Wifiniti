package com.codingwell.android;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

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
                        //wifiObject.put("operatorFriendlyName", result.operatorFriendlyName);
                        //wifiObject.put("venueName", result.venueName);
                        //wifiObject.put("centerFreq0", result.centerFreq0);
                        //wifiObject.put("centerFreq1", result.centerFreq1);
                        //wifiObject.put("channelWidth", result.channelWidth);

                    wifiArray.pushMap(wifiObject);
                }
            }
            promise.resolve(wifiArray);
        } catch (IllegalViewOperationException e) {
            promise.reject(e);
        }
    }
}
