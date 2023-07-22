package com.example.chat_app.utilities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.LayoutRes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class FunctionGlobal {
    public static void openDialog
            (Context context, @LayoutRes int layoutResID, View.OnClickListener event,int idButtonNegative,int idButtonPositive) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(layoutResID);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        Button buttonNegative = dialog.findViewById(idButtonNegative);
        Button buttonPositive = dialog.findViewById(idButtonPositive);

        buttonNegative.setOnClickListener(event);
        buttonPositive.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    public static String checkTheDateToShow(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOfWeekString = "";
        Calendar beforeSunday = Calendar.getInstance();
        int dayOfWeek = beforeSunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY ?
                beforeSunday.get(Calendar.DAY_OF_WEEK) : 8;
        beforeSunday.add(Calendar.DAY_OF_MONTH, -dayOfWeek + 1);
        beforeSunday.set(Calendar.HOUR, 23);
        beforeSunday.set(Calendar.MINUTE, 59);
        beforeSunday.set(Calendar.SECOND, 59);
        if (calendar.after(beforeSunday)) {
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    dayOfWeekString = "CN";
                    break;
                case Calendar.MONDAY:
                    dayOfWeekString = "T2";
                    break;
                case Calendar.TUESDAY:
                    dayOfWeekString = "T3";
                    break;
                case Calendar.WEDNESDAY:
                    dayOfWeekString = "T4";
                    break;
                case Calendar.THURSDAY:
                    dayOfWeekString = "T5";
                    break;
                case Calendar.FRIDAY:
                    dayOfWeekString = "T6";
                    break;
                case Calendar.SATURDAY:
                    dayOfWeekString = "T7";
                    break;
            }
        } else {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM");
            dayOfWeekString = dateFormat.format(date);
        }
        return dayOfWeekString;
    }

    public static boolean isConnectedNetwork(Context context) {
        if (context == null)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null)
                return false;
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities == null)
                return false;
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
}
