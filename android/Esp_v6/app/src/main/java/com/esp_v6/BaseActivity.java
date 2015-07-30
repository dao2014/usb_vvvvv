package com.esp_v6;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.esp_v6.util.SharedFileUtils;
import com.esp_v6.util.WifiUtils;

/**
 * Created by Administrator on 2015/7/28.
 */
public abstract class BaseActivity extends Fragment {
    protected WifiUtils mWifiUtils;
    protected SharedFileUtils sharedFileUtils;
    /** 初始化视图 **/
    protected abstract void initViews();

    /** 初始化事件 **/
    protected abstract void initEvents();
    /** 含有标题和内容的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle(title)
                .setMessage(
                        message)
                .show();
        return alertDialog;
    }

    /** 含有标题、内容、两个按钮的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message, String positiveText, DialogInterface.OnClickListener onPositiveClickListener, String negativeText, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle(title)
                .setMessage(
                        message)
                .setPositiveButton(
                        positiveText,
                        onPositiveClickListener)
                .setNegativeButton(
                        negativeText,
                        onNegativeClickListener)
                .show();
        return alertDialog;
    }

    /** 含有标题、内容、图标、两个按钮的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message, int icon, String positiveText, DialogInterface.OnClickListener onPositiveClickListener, String negativeText, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle(title)
                .setMessage(
                        message)
                .setIcon(icon)
                .setPositiveButton(
                        positiveText,
                        onPositiveClickListener)
                .setNegativeButton(
                        negativeText,
                        onNegativeClickListener)
                .show();
        return alertDialog;
    }
}
