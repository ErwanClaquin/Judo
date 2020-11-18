package com.example.judofeuilledecombats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class CombatActivity extends AppCompatActivity {

    private final long[] pattern = {0, 500, 500, 500, 500, 500, 500};
    public boolean finishTimer = false;
    private int numberBIppon = 0;
    private int numberBWaza = 0;
    private int numberBShido = 0;
    private int numberRIppon = 0;
    private int numberRWaza = 0;
    private int numberRShido = 0;
    private long timeLeftMillis = 3000;
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
        IpponB = findViewById(R.id.B_ippon);
        WazaB = findViewById(R.id.B_waza);
        ShidoB = findViewById(R.id.B_shido);
        IpponR = findViewById(R.id.R_ippon);
        WazaR = findViewById(R.id.R_waza);
        ShidoR = findViewById(R.id.R_shido);
        IpponB.setText(String.valueOf(numberBIppon));
        WazaB.setText(String.valueOf(numberBWaza));
        ShidoB.setText(String.valueOf(numberBShido));
        IpponR.setText(String.valueOf(numberRIppon));
        WazaR.setText(String.valueOf(numberRWaza));
        ShidoR.setText(String.valueOf(numberRShido));

        timer = findViewById(R.id.Timer);
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

        Intent intent = getIntent();
        String CombattantR = intent.getStringExtra("CombattantR");
        String CombattantB = intent.getStringExtra("CombattantB");
        String CombattantRNext = intent.getStringExtra("CombattantRNext");
        String CombattantBNext = intent.getStringExtra("CombattantBNext");

        TextView combR = findViewById(R.id.CombattantR);
        TextView combB = findViewById(R.id.CombattantB);
        combB.setText(CombattantB);
        combR.setText(CombattantR);

        if (CombattantBNext == null) {
            LinearLayout prepare = findViewById(R.id.prepare);
            prepare.setVisibility(View.INVISIBLE);
        } else {
            TextView futcombR = findViewById(R.id.FuturcombattantRed);
            TextView futcombB = findViewById(R.id.FuturcombattantBlanc);
            futcombB.setText(CombattantBNext);
            futcombR.setText(CombattantRNext);
        }

    }

    private void startTimer() {
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
                    buttonStartTimer.setAlpha(1f);
                    buttonPauseTimer.setAlpha(0.25f);
                    buttonStartTimer.setClickable(true);
                    buttonPauseTimer.setClickable(false);
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
            Toast.makeText(getApplicationContext(), "Ippon Blanc ET Rouge, attention", Toast.LENGTH_LONG).show();
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

    @SuppressLint("SetTextI18n")
    private String askWinner() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Attention");
        alert.setMessage("Impossible de désigner un vainqueur");

        LinearLayout linear = new LinearLayout(this);

        linear.setOrientation(LinearLayout.VERTICAL);
        TextView text = new TextView(this);
        text.setText("0:00");
        text.setPadding(10, 10, 10, 10);

        SeekBar seek = new SeekBar(this);
        seek.setMax(60);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minutes = (progress * 5) / 60;
                int seconds = (progress * 5) % 60;
                String timeLeft = minutes + ":";
                if (seconds < 10) {
                    timeLeft += "0";
                }
                timeLeft += seconds;
                text.setText(timeLeft);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        linear.addView(seek);
        linear.addView(text);

        alert.setView(linear);

        alert.setPositiveButton("Rajouter du temps", (dialog, which) -> {
            String time = (String) text.getText();
            String[] timeSplit = time.split(":");
            int minutes = Integer.parseInt(timeSplit[0]);
            int seconds = Integer.parseInt(timeSplit[1]);
            if (minutes == 0 && seconds == 0) {
                Toast.makeText(getApplicationContext(), "Aucun temps", Toast.LENGTH_SHORT).show();
            } else {
                timeLeftMillis = (minutes * 60 + seconds) * 1000;
                String timeLeft = minutes + ":";
                if (seconds < 10) {
                    timeLeft += "0";
                }
                timeLeft += seconds;
                timer.setText(timeLeft);
            }
            finishTimer = false;
            dialog.cancel();
        });

        alert.setNegativeButton("Combattant Rouge gagne", (dialog, which) -> {
            vainqueur = "R";
            endCombat();
            dialog.cancel();
        });

        alert.setNeutralButton("Combattant Blanc gagne", (dialog, which) -> {
            vainqueur = "B";
            endCombat();
            dialog.cancel();
        });
        alert.show();
        return "";
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