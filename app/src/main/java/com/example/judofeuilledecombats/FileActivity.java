package com.example.judofeuilledecombats;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileActivity extends AppCompatActivity {
    private boolean firstLauch = true;
    private final int requestCodeCombat = 1;
    private List<String> listeCombat;
    private Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        buttonStart = findViewById(R.id.buttonStartCombat);
        buttonStart.setOnClickListener(v -> verifStart());
    }

    private void verifStart() {
        TableLayout tableLayout = findViewById(R.id.maintable);
        List<TableRow> tableRowList = new ArrayList<>();
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            EditText editText = (EditText) tableRow.getChildAt(0);
            if (!String.valueOf(editText.getText()).equals("")) {
                tableRowList.add(tableRow);
            }
        }
        if (tableRowList.size() < 3) {
            Toast.makeText(getApplicationContext(), "Pas assez de combattants", Toast.LENGTH_LONG).show();
        } else {
            if (firstLauch) {
                resizeTable(tableLayout, tableRowList);
                attributeListCombat(tableLayout);
                firstLauch = false;
            }
            startActivityForResult(new Intent(FileActivity.this, CombatActivity.class), requestCodeCombat);
        }
    }

    private void resizeTable(@NotNull TableLayout tableLayout, @NotNull List<TableRow> tableRowList) {
        //Put in the first
        for (int i = 0; i < tableRowList.size(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i + 1);
            EditText editTextNameTo = (EditText) row.getChildAt(0);
            EditText editTextClubTo = (EditText) row.getChildAt(1);
            EditText editTextNameFrom = (EditText) tableRowList.get(i).getChildAt(0);
            EditText editTextClubFrom = (EditText) tableRowList.get(i).getChildAt(1);
            editTextNameTo.setText(editTextNameFrom.getText());
            editTextClubTo.setText(editTextClubFrom.getText());
        }

        //Remove last line
        int len = tableLayout.getChildCount();
        for (int j = tableRowList.size() + 1; j < len; j++) {
            tableLayout.removeView(tableLayout.getChildAt(tableRowList.size() + 1));
        }

        //remove useless colomn
        List<TableRow> tableRowColRemove = new ArrayList<>();

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow t = (TableRow) tableLayout.getChildAt(i);
            for (int j = tableRowList.size(); j < 6; j++) {
                t.removeView(t.getChildAt(3 + tableRowList.size()));
            }
        }
    }

    private void attributeListCombat(@NotNull TableLayout tableLayout) {
        switch (tableLayout.getChildCount() - 1) {
            case 3:
                listeCombat = new ArrayList<>(Arrays.asList("12", "23", "13"));
                break;
            case 4:
                listeCombat = new ArrayList<>(Arrays.asList("12", "34", "13", "24", "14", "23"));
                break;
            case 5:
                listeCombat = new ArrayList<>(Arrays.asList("12", "43", "15", "23", "45", "13", "25", "14", "35", "24"));
                break;
            case 6:
                listeCombat = new ArrayList<>(Arrays.asList("12", "34", "26", "15", "46", "23", "16", "45", "13", "25", "36", "14", "35", "24", "56"));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //Ask save if anything write
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeCombat) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final int numberBIppon = Integer.parseInt(data.getStringExtra("numberBIppon"));
                    final int numberBWaza = Integer.parseInt(data.getStringExtra("numberBWaza"));
                    final int numberBShido = Integer.parseInt(data.getStringExtra("numberBShido"));
                    final int numberRIppon = Integer.parseInt(data.getStringExtra("numberRIppon"));
                    final int numberRWaza = Integer.parseInt(data.getStringExtra("numberRWaza"));
                    final int numberRShido = Integer.parseInt(data.getStringExtra("numberRShido"));
                    final String vainqueur = data.getStringExtra("vainqueur");

                    String s = listeCombat.get(0);
                    String ids = "_" + s;
                    String oppids = "_" + s.charAt(1) + s.charAt(0);
                    int resourceId = this.getResources().getIdentifier(ids, "id", this.getPackageName());
                    int resourceIdopp = this.getResources().getIdentifier(oppids, "id", this.getPackageName());

                    //Rouge
                    TableLayout tResult = findViewById(resourceId);
                    TableLayout tResultopp = findViewById(resourceIdopp);
                    TableRow Row1 = (TableRow) tResult.getChildAt(0);
                    EditText VorX = (EditText) Row1.getChildAt(0);
                    EditText points = (EditText) Row1.getChildAt(1);
                    TableRow Row2 = (TableRow) tResult.getChildAt(1);
                    EditText rowPoint = (EditText) Row2.getChildAt(0);

                    //Blanc
                    TableRow Row1opp = (TableRow) tResultopp.getChildAt(0);
                    EditText VorXopp = (EditText) Row1opp.getChildAt(0);
                    EditText pointsopp = (EditText) Row1opp.getChildAt(1);
                    TableRow Row2opp = (TableRow) tResultopp.getChildAt(1);
                    EditText rowPointopp = (EditText) Row2opp.getChildAt(0);
                    //first row first col
                    if (vainqueur.equals("R")) {
                        VorX.setText("V");
                        VorXopp.setText("X");
                    } else {
                        VorX.setText("X");
                        VorXopp.setText("V");
                    }


                    //first row second col
                    if (numberRIppon == 1 || numberRWaza == 2) {
                        final String sr = "10";
                        points.setText(sr);
                    } else if (numberRWaza == 1) {
                        points.setText("7");
                    } else {
                        points.setText("0");
                    }
                    if (numberBIppon == 1 || numberBWaza == 2) {
                        final String sr2 = "10";
                        pointsopp.setText(sr2);
                    } else if (numberBWaza == 1) {
                        pointsopp.setText("7");
                    } else {
                        pointsopp.setText("0");
                    }
                    //second row
                    char[] srow = "xx(x)".toCharArray();
                    if (numberRIppon == 1 || numberRWaza == 2) {
                        srow[0] = '1';
                    } else {
                        srow[0] = '0';

                    }
                    if (numberRWaza == 1) {
                        srow[1] = '1';
                    } else {
                        srow[1] = '0';

                    }
                    srow[3] = ("" + numberRShido).charAt(0);
                    rowPoint.setText(new String(srow));

                    char[] srowopp = "xx(x)".toCharArray();
                    if (numberBIppon == 1 || numberBWaza == 2) {
                        srowopp[0] = '1';
                    } else {
                        srowopp[0] = '0';
                    }
                    if (numberBWaza == 1) {
                        srowopp[1] = '1';
                    } else {
                        srowopp[1] = '0';
                    }
                    srowopp[3] = ("" + numberBShido).charAt(0);
                    rowPointopp.setText(new String(srowopp));

                    listeCombat.remove(0);

                    if (listeCombat.size() == 0) {
                        buttonStart.setVisibility(View.INVISIBLE);
                        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                        constraintLayout.setVisibility(View.INVISIBLE);
                        fillResult();
                    } else {
                        displayCombattant();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error has occur", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void displayCombattant() {
        String s = listeCombat.get(0);
        int rowCombattantRed = s.charAt(0);
        int rowCombattantBlanc = s.charAt(1);
        TableLayout tableLayout = findViewById(R.id.maintable);
        TableRow r1 = (TableRow) tableLayout.getChildAt(rowCombattantRed + 1);
        EditText e1 = (EditText) r1.getChildAt(0);
        TableRow r2 = (TableRow) tableLayout.getChildAt(rowCombattantBlanc + 1);
        EditText e2 = (EditText) r2.getChildAt(0);
        TextView T_B = findViewById(R.id.combattantBlanc);
        TextView T_R = findViewById(R.id.combattantRed);
        T_R.setText(e1.getText());
        T_B.setText(e2.getText());
    }

    private void fillResult() {

    }
}
