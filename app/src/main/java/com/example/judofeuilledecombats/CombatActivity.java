package com.example.judofeuilledecombats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.util.Log;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CombatActivity extends AppCompatActivity {

    private int numberBIppon = 0;
    private int numberBWaza = 0;
    private int numberBShido = 0;
    private int numberRIppon = 0;
    private int numberRWaza = 0;
    private int numberRShido = 0;
    private long timeLeftMillis = 3000;
    private final long[] pattern = {0, 500, 500, 500, 500, 500, 500};
    public boolean finishTimer = false;
    private Vibrator vib;
    private CountDownTimer countDownTimer;
    private Button buttonPauseTimer;
    private Button buttonStartTimer;
    private String vainqueur;
    private TextView IpponB;
    private TextView WazaB;
    private TextView ShidoB;
    private TextView IpponR;
    private TextView WazaR;
    private TextView ShidoR;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        IpponB = (TextView) findViewById(R.id.B_ippon);
        WazaB = (TextView) findViewById(R.id.B_waza);
        ShidoB = (TextView) findViewById(R.id.B_shido);
        IpponR = (TextView) findViewById(R.id.R_ippon);
        WazaR = (TextView) findViewById(R.id.R_waza);
        ShidoR = (TextView) findViewById(R.id.R_shido);
        IpponB.setText(String.valueOf(numberBIppon));
        WazaB.setText(String.valueOf(numberBWaza));
        ShidoB.setText(String.valueOf(numberBShido));
        IpponR.setText(String.valueOf(numberRIppon));
        WazaR.setText(String.valueOf(numberRWaza));
        ShidoR.setText(String.valueOf(numberRShido));

        timer = (TextView) findViewById(R.id.Timer);
        updateTimer();
        buttonStartTimer = findViewById(R.id.startTimer);
        buttonStartTimer.setOnClickListener(v -> startTimer());

        buttonPauseTimer = findViewById(R.id.pauseTimer);
        buttonPauseTimer.setOnClickListener(v -> stopTimer());

        Button buttonBIpponMinus = findViewById(R.id.B_ippon_minus);
        buttonBIpponMinus.setOnClickListener(v -> {
            numberBIppon = 0;
            IpponB.setText(String.valueOf(numberBIppon));
        });

        Button buttonBWazaMinus = findViewById(R.id.B_waza_minus);
        buttonBWazaMinus.setOnClickListener(v -> {
            numberBWaza = numberBWaza > 0 ? numberBWaza - 1 : 0;
            WazaB.setText(String.valueOf(numberBWaza));
        });

        Button buttonBShidoMinus = findViewById(R.id.B_shido_minus);
        buttonBShidoMinus.setOnClickListener(v -> {
            numberBShido = numberBShido > 0 ? numberBShido - 1 : 0;
            ShidoB.setText(String.valueOf(numberBShido));
        });

        Button buttonBIpponPlus = findViewById(R.id.B_ippon_plus);
        buttonBIpponPlus.setOnClickListener(v -> {
            numberBIppon = 1;
            IpponB.setText(String.valueOf(numberBIppon));
        });

        Button buttonBWazaPlus = findViewById(R.id.B_waza_plus);
        buttonBWazaPlus.setOnClickListener(v -> {
            numberBWaza++;
            WazaB.setText(String.valueOf(numberBWaza));
        });

        Button buttonBShidoPlus = findViewById(R.id.B_shido_plus);
        buttonBShidoPlus.setOnClickListener(v -> {
            numberBShido++;
            ShidoB.setText(String.valueOf(numberBShido));
        });

        Button buttonRIpponMinus = findViewById(R.id.R_ippon_minus);
        buttonRIpponMinus.setOnClickListener(v -> {
            numberRIppon = 0;
            IpponR.setText(String.valueOf(numberRIppon));
        });

        Button buttonRWazaMinus = findViewById(R.id.R_waza_minus);
        buttonRWazaMinus.setOnClickListener(v -> {
            numberRWaza = numberRWaza > 0 ? numberRWaza - 1 : 0;
            WazaR.setText(String.valueOf(numberRWaza));
        });

        Button buttonRShidoMinus = findViewById(R.id.R_shido_minus);
        buttonRShidoMinus.setOnClickListener(v -> {
            numberRShido = numberRShido > 0 ? numberRShido - 1 : 0;
            ShidoR.setText(String.valueOf(numberRShido));
        });

        Button buttonRIpponPlus = findViewById(R.id.R_ippon_plus);
        buttonRIpponPlus.setOnClickListener(v -> {
            numberRIppon = 1;
            IpponR.setText(String.valueOf(numberRIppon));
        });
        Button buttonRWazaPlus = findViewById(R.id.R_waza_plus);
        buttonRWazaPlus.setOnClickListener(v -> {
            numberRWaza++;
            WazaR.setText(String.valueOf(numberRWaza));
        });

        Button buttonRShidoPlus = findViewById(R.id.R_shido_plus);
        buttonRShidoPlus.setOnClickListener(v -> {
            numberRShido++;
            ShidoR.setText(String.valueOf(numberRShido));
        });

        Button buttonEndCombat = findViewById(R.id.EndCombat);
        buttonEndCombat.setOnClickListener(v -> checkEndCombat());
    }

    private void startTimer() {
        Log.v("Start", String.valueOf(timeLeftMillis));
        if (!finishTimer) {
            countDownTimer = new CountDownTimer(timeLeftMillis, 1) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftMillis = millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    finishTimer = true;
                    vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vib.hasVibrator()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(VibrationEffect.createWaveform(pattern, -1));
                        } else {
                            //deprecated in API 26
                            vib.vibrate(pattern, -1);
                        }
                    }

                }
            }.start();
            buttonStartTimer.setAlpha(0.25f);
            buttonPauseTimer.setAlpha(1f);
            buttonStartTimer.setClickable(false);
            buttonPauseTimer.setClickable(true);
        }
    }


    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            buttonStartTimer.setAlpha(1f);
            buttonPauseTimer.setAlpha(0.25f);
            buttonStartTimer.setClickable(true);
            buttonPauseTimer.setClickable(false);
        }

    }

    private void updateTimer() {
        int minutes = (int) timeLeftMillis / 60000;
        int seconds = (int) timeLeftMillis % 60000 / 1000;
        String timeLeft = minutes + ":";
        if (seconds < 10) {
            timeLeft += "0";
        }
        timeLeft += seconds;
        timer.setText(timeLeft);
    }

    private void checkEndCombat() {
        if (numberBIppon == 1 && numberRIppon == 1) {
            Toast.makeText(getApplicationContext(), "Ippon Blanc et Rouge, attention", Toast.LENGTH_LONG).show();
        } else if (numberBIppon == 1) {
            vainqueur = "B";
            endCombat();
        } else if (numberRIppon == 1) {
            vainqueur = "R";
            endCombat();
        } else if (numberBWaza == numberRWaza) {
            vainqueur = askWinner();
            if (vainqueur.equals("B") || vainqueur.equals("R")) {
                endCombat();
            }
        } else if (numberBWaza > numberBIppon) {
            vainqueur = "B";
            endCombat();
        } else if (numberRWaza > numberBWaza) {
            vainqueur = "R";
            endCombat();
        }
    }

    private String askWinner() {
        return "o";
    }

    private void endCombat() {

        if (vib != null) {
            vib.cancel();
        }
        Intent intent = new Intent();
        intent.putExtra("numberBIppon", String.valueOf(numberBIppon));
        intent.putExtra("numberBWaza", String.valueOf(numberBWaza));
        intent.putExtra("numberBShido", String.valueOf(numberBShido));
        intent.putExtra("numberRIppon", String.valueOf(numberRIppon));
        intent.putExtra("numberRWaza", String.valueOf(numberRWaza));
        intent.putExtra("numberRShido", String.valueOf(numberRShido));
        intent.putExtra("vainqueur", vainqueur);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onBackPressed() {

    }
}