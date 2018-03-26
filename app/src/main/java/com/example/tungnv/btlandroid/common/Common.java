package com.example.tungnv.btlandroid.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.tungnv.btlandroid.model.User;

/**
 * Created by Tungnv on 3/22/2018.
 */

public class Common {
    public static User curentUser;
    public static final String DELETE = "Xóa";
    public static final String USER_KEY = "Tài khoản";
    public static final String PWD_KEY = "Mật khẩu";

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager !=null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i=0; i<info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
