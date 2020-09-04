package com.example.reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private Button time, date, save;
    private TextView selectedTime, selectedDate;
    EditText title;
    String titleStr;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog mTimePicker;
    int selectedDay, selectedMonth, selectedYear;
    long selectedHourMillis, selectedMinuteMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();


        title = (EditText) findViewById(R.id.titleEditText);

        date = (Button) findViewById(R.id.date);
        time = (Button) findViewById(R.id.time);
        save = (Button) findViewById(R.id.save);
        selectedDate = (TextView) findViewById(R.id.selectedDate);

        selectedTime = (TextView) findViewById(R.id.selectedTime);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                titleStr = title.getText().toString();

                final Calendar mCurrentDate = Calendar.getInstance();
                int mYear = mCurrentDate.get(Calendar.YEAR);
                int mMonth = mCurrentDate.get(Calendar.MONTH);
                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                Log.i("Receiver", "Broadcast received: " + titleStr);

                Toast.makeText(getApplicationContext(), "Reminder Set!", Toast.LENGTH_SHORT).show();
                if (mYear == selectedYear && mMonth + 1 == selectedMonth && mDay == selectedDay) {
                    Intent in = new Intent(MainActivity.this, ReminderBroadcast.class);
                    in.putExtra("id", titleStr);
                    PendingIntent pendingIntent =
                            PendingIntent.getBroadcast(MainActivity.this, 0, in, 0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    long triggerAtMillis = selectedHourMillis + selectedMinuteMillis;

                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            }
        });

    }

    private void handleDateButton() {

        final Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
                        selectedDate.setText("Reminder date & time " + day + "/"
                                + (month + 1) + "/" + year);
                        selectedDay = day;
                        selectedMonth = month + 1;
                        selectedYear = year;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void handleTimeButton() {

        Calendar mCurrentTime = Calendar.getInstance();
        int mHour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int mMinute = mCurrentTime.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (minute == 0) {
                    selectedTime.setText(hour + ":00");
                } else if (minute < 10) {
                    selectedTime.setText(hour + ":0" + minute);
                } else {
                    selectedTime.setText(hour + ":" + minute);
                }
                selectedHourMillis = hour * 3600 * 1000;
                selectedMinuteMillis = minute * 60 * 1000;
            }
        }, mHour, mMinute, true);
        mTimePicker.show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyMe", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
