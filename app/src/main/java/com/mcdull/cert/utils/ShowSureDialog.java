package com.mcdull.cert.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.mcdull.cert.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mcdull on 15/8/3.
 */
public class ShowSureDialog {

    private Context context;
    private AlertDialog alertDialog;
    private String title;
    private CallBack callBack;

    public ShowSureDialog(Context context, String title, CallBack callBack) {
        this.context = context;
        this.title = title;
        this.callBack = callBack;
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.sure_dialog, null);

        TextView tvTooltipTitle = (TextView) view.findViewById(R.id.tv_tooltip_title);

        tvTooltipTitle.setText(title);

        view.findViewById(R.id.bt_yes).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBack.CallBack();
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.bt_no).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
    }

    public void dismiss() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    public interface CallBack {
        public void CallBack();
    }
}
