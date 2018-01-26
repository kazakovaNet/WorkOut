package com.hfad.workout;


import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchFragment extends Fragment implements View.OnClickListener {
    // Количество секунд на секундомере
    private int seconds = 0;
    // Секундомер работает?
    // Переменная running указывает, работает ли секундомер,
    // а wasRunning — работал ли секундомер перед приостановкой секундомера
    private boolean running;
    private boolean wasRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // Восстановить значения переменных из объекта savedInstanceState типа Bundle
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            if (wasRunning) {
                running = true;
            }
        }
    }

    // Макет фрагмента назначается в методе onCreateView()
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Назначить макет фрагмента и передать макет при вызове метода runTimer()
        View layout = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        // Представление макета передается при вызове метода runTimer()
        runTimer(layout);

        // Всем кнопкам назначаются слушатели
        Button startButton = (Button) layout.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        Button stopButton = (Button) layout.findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        Button resetButton = (Button) layout.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Если фрагмент приостанавливается, сохранить информацию о том,
        // работал ли секундомер на момент приостановки, и остановить отсчет времени
        wasRunning = running;
        running = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Если секундомер работал до приостановки, снова запустить отсчет времени
        if (wasRunning) {
            running = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Сохранить значения переменных в Bundle перед уничтожением активности.
        // Эти значения используются при повороте устройства
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    // Выполняется при нажатии кнопки Start
    public void onClickStart(View view) {
        running = true;
    }

    // Выполняется при нажатии кнопки Stop
    public void onClickStop(View view) {
        running = false;
    }

    // Выполняется при нажатии кнопки Reset
    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }

    // Sets the number of seconds on the timer.
    // Теперь методу runTimer() передается объект View
    private void runTimer(View view) {
        final TextView timeView = (TextView) view.findViewById(R.id.time_view);
        // Код, размещаемый в объекте Handler, может выполняться в фоновом программном потоке
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format("%d:%02d:%02d", hours, minutes, secs);
                // Вывести количество прошедших секунд
                timeView.setText(time);
                if (running) {
                    // Если секундомер работает, увеличить число секунд
                    seconds++;
                }
                // Код Handler выполняется каждую секунду
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // Проверить, на каком представлении щелкнул пользователь
        switch (v.getId()) {
            case R.id.start_button:
                onClickStart(v);
                break;
            case R.id.stop_button:
                onClickStop(v);
                break;
            case R.id.reset_button:
                onClickReset(v);
        }
    }
}
