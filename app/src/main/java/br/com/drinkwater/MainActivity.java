package br.com.drinkwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //variáveis usadas para vincular os ids no activity_main.xml
    private Button btnNotify;
    private EditText editMinutes;
    private TimePicker timePicker;

    //variáveis usadas na função notifyClick
    private int hour;
    private int minute;
    private int interval;

    //guardadr pequenos dados
    private SharedPreferences preferences;

    private boolean activated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //vatiáveis vinculadas aos ids
        btnNotify = findViewById(R.id.btn_notify);
        editMinutes = findViewById(R.id.edit_txt_number_interval);
        timePicker = findViewById(R.id.time_picker);

        //mudando o valor do timer para 24 horas
        timePicker.setIs24HourView(true);

        //deve ser guardada no onCreated
        preferences = getSharedPreferences("db", Context.MODE_PRIVATE);

        activated = preferences.getBoolean("activated", false);

        if (activated) {
            btnNotify.setText(R.string.pause);
            int color = ContextCompat.getColor(this, android.R.color.black);
            btnNotify.setBackgroundColor(color);

            int interval = preferences.getInt("interval", 0);
            int hour = preferences.getInt("hour", timePicker.getCurrentHour());
            int minute = preferences.getInt("minute", timePicker.getCurrentMinute());

            editMinutes.setText(String.valueOf(interval));
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }

    }


    //função que escuta o evento de click
    public void notifyClick(View view) {

        String sInterval = editMinutes.getText().toString();

        //Toast informando quando o campo de intervalo está vazio
        if (sInterval.isEmpty()) {
            Toast.makeText(this, R.string.msg_error, Toast.LENGTH_LONG).show();
            return;
        }
        //Pega a hora e o minuto
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        //converte para string
        interval = Integer.parseInt(sInterval);

        if (!activated) {
            //Muda o nome do botão
            btnNotify.setText(R.string.pause);
            //Muda a cor do botão
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.black));

            activated = true;

            //Depois que muda o estado do layout
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", true);
            editor.putInt("interval", interval);
            editor.putInt("minute", minute);
            editor.putInt("hour", hour);
            //aplica a escrita no banco de dados
            editor.apply();

        } else {
            //Muda o nome do botão
            btnNotify.setText(R.string.notify);
            //Muda a cor do botão
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.teal_200));
            activated = false;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", false);
            editor.remove("interval");
            editor.remove("minute");
            editor.remove("hour");
            //aplica a remoção do banco de dados
            editor.apply();
        }


        Log.d("teste", "hora: " + hour + " minuto: " + minute + " intervalo: " + interval);

    }
}