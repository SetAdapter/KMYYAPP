package com.kmwlyy.patient.weight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.patient.R;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickDialogUtil implements OnDateChangedListener {
    private DatePicker datePicker;
    private String dateTime;
    private final String BirthDay;
    private final Activity activity;

    public DatePickDialogUtil(Activity activity, String BirthDay) {
        this.activity = activity;
        this.BirthDay = BirthDay;
    }

    @SuppressLint("SimpleDateFormat")
    public AlertDialog dateTimePickDialog(final Context context, final TextView inputDate, final JSONObject json) {
        @SuppressLint("InflateParams") RelativeLayout dateTimeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.date_picker, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        datePicker.setMaxDate(new Date().getTime());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(BirthDay));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            datePicker.init(year, month, day, this);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AlertDialog ad = new AlertDialog.Builder(activity)
                .setView(dateTimeLayout)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        inputDate.setText(dateTime);
                        Toast.makeText(context, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(com.winson.ui.R.string.cancel, null).show();
        onDateChanged(null, 0, 0, 0);
        return ad;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        dateTime = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
}