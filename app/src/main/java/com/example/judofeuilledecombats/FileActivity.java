package com.example.judofeuilledecombats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileActivity extends AppCompatActivity {
    private final int requestCodeCombat = 1;
    private boolean firstLauch = true;
    private List<String> listeCombat;
    private Button buttonStart;
    private Button buttonRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        buttonStart = findViewById(R.id.buttonStartCombat);
        buttonStart.setOnClickListener(v -> verifStart());
        buttonRefresh = findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(v -> fillResult());

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
                LinearLayout l = findViewById(R.id.prepareCombattants);
                l.setVisibility(View.VISIBLE);
                firstLauch = false;
            }
            String s = listeCombat.get(0);
            int rowCombattantRed = Character.getNumericValue(s.charAt(0));
            int rowCombattantBlanc = Character.getNumericValue(s.charAt(1));
            String e1 = getCombattants(rowCombattantRed);
            String e2 = getCombattants(rowCombattantBlanc);
            Intent i = new Intent(FileActivity.this, CombatActivity.class);
            i.putExtra("CombattantR", e1);
            i.putExtra("CombattantB", e2);

            if (listeCombat.size() > 1) {
                String snext = listeCombat.get(1);
                int rowCombattantRedNext = Character.getNumericValue(snext.charAt(0));
                int rowCombattantBlancNext = Character.getNumericValue(snext.charAt(1));
                String e1next = getCombattants(rowCombattantRedNext);
                String e2next = getCombattants(rowCombattantBlancNext);
                i.putExtra("CombattantRNext", e1next);
                i.putExtra("CombattantBNext", e2next);
            }
            startActivityForResult(i, requestCodeCombat);

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
                listeCombat = new ArrayList<>(Arrays.asList("12", "34", "15", "23", "45", "13", "25", "14", "35", "24"));
                break;
            case 6:
                listeCombat = new ArrayList<>(Arrays.asList("12", "34", "26", "15", "46", "23", "16", "45", "13", "25", "36", "14", "35", "24", "56"));
                break;
            default:
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
                    if (Objects.requireNonNull(vainqueur).equals("R")) {
                        VorX.setText("V");
                        VorX.setTextColor(Color.parseColor("#00FF00"));
                        VorXopp.setText("X");
                        VorXopp.setTextColor(Color.parseColor("#FF0000"));

                        //first row second col
                        if (numberRIppon == 1 || numberRWaza == 2) {
                            final String sr = "10";
                            points.setText(sr);
                        } else if (numberRWaza == 1) {
                            points.setText("7");
                        } else {
                            points.setText("0");
                        }

                    } else {
                        VorX.setText("X");
                        VorX.setTextColor(Color.parseColor("#FF0000"));
                        VorXopp.setText("V");
                        VorXopp.setTextColor(Color.parseColor("#00FF00"));

                        //first row second col
                        if (numberBIppon == 1 || numberBWaza == 2) {
                            final String sr2 = "10";
                            pointsopp.setText(sr2);
                        } else if (numberBWaza == 1) {
                            pointsopp.setText("7");
                        } else {
                            pointsopp.setText("0");
                        }
                    }

                    //second row
                    char[] srow = "xx(x)".toCharArray();
                    srow[0] = numberRIppon == 1 || numberRWaza == 2 ? '1' : '0';
                    srow[1] = numberRWaza == 1 ? '1' : '0';
                    srow[3] = ("" + numberRShido).charAt(0);
                    rowPoint.setText(new String(srow));

                    char[] srowopp = "xx(x)".toCharArray();
                    srowopp[0] = numberBIppon == 1 || numberBWaza == 2 ? '1' : '0';
                    srowopp[1] = numberBWaza == 1 ? '1' : '0';
                    srowopp[3] = ("" + numberBShido).charAt(0);
                    rowPointopp.setText(new String(srowopp));

                    listeCombat.remove(0);
                    if (listeCombat.size() == 0) {
                        buttonStart.setVisibility(View.INVISIBLE);
                        buttonRefresh.setVisibility(View.VISIBLE);
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
        int rowCombattantRed = Character.getNumericValue(s.charAt(0));
        int rowCombattantBlanc = Character.getNumericValue(s.charAt(1));
        String e1 = getCombattants(rowCombattantRed);
        String e2 = getCombattants(rowCombattantBlanc);
        TextView T_B = findViewById(R.id.FuturcombattantBlanc);
        TextView T_R = findViewById(R.id.FuturcombattantRed);
        T_R.setText(e1);
        T_B.setText(e2);
    }

    private String getCombattants(int rowCombattant) {
        TableLayout tableLayout = findViewById(R.id.maintable);
        TableRow r1 = (TableRow) tableLayout.getChildAt(rowCombattant);
        TextView e1 = (TextView) r1.getChildAt(0);
        return "" + e1.getText();
    }

    private void fillResult() {
        TableLayout tableLayout = findViewById(R.id.maintable);
        int len = tableLayout.getChildCount();
        int[][] places = new int[len - 1][4]; //[[index, victoires, points, place], ...]
        for (int j = 1; j < len; j++) {
            int points = 0;
            int victoires = 0;
            TableRow tableRow = (TableRow) tableLayout.getChildAt(j);
            for (int i = 1; i < len; i++) {
                if (i != j) {
                    TableLayout TableLayoutPoints = (TableLayout) tableRow.getChildAt(2 + i);
                    TableRow tableRowPoint = (TableRow) TableLayoutPoints.getChildAt(0);
                    EditText textPoint = (EditText) tableRowPoint.getChildAt(1);
                    EditText textVictoire = (EditText) tableRowPoint.getChildAt(0);
                    String pointString = String.valueOf(textPoint.getText());
                    if (!pointString.equals("")) {
                        points += Integer.parseInt(pointString);
                    }
                    String victoiresString = String.valueOf(textVictoire.getText());
                    if (victoiresString.equals("V")) {
                        textVictoire.setTextColor(Color.parseColor("#00FF00"));
                        victoires++;
                    } else {
                        textVictoire.setTextColor(Color.parseColor("#FF0000"));
                    }
                }

            }
            TextView VictTot = (TextView) tableRow.getChildAt(2 + len);
            String svict = String.valueOf(victoires);
            VictTot.setText(svict);

            TextView pointTot = (TextView) tableRow.getChildAt(3 + len);
            String spoint = String.valueOf(points);
            pointTot.setText(spoint);

            places[j - 1] = new int[]{j, victoires, points, 0};
        }
        sortPlace(places);
        for (int k = 0; k < len - 1; k++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(places[k][0]);
            TextView textPlace = (TextView) tableRow.getChildAt(4 + len);
            textPlace.setText(String.valueOf(places[k][3]));
        }
    }

    private void sortPlace(int[][] places) {
        int[] temp;
        for (int i = 0; i < places.length - 1; i++) {
            for (int j = i + 1; j < places.length; j++) {
                if (places[i][1] < places[j][1]) {
                    temp = places[j];
                    places[j] = places[i];
                    places[i] = temp;
                } else if (places[i][1] == places[j][1]) {
                    if (places[i][2] < places[j][2]) {
                        temp = places[j];
                        places[j] = places[i];
                        places[i] = temp;
                    }

                }
            }
        }
        attributeRank(places);
    }

    private void attributeRank(int[][] places) {
        // MUST BE SORTED !
        int place = 1;
        places[0][3] = place;
        for (int i = 1; i < places.length; i++) {
            if (places[i][1] < places[i - 1][1]) {
                place = i + 1;
            } else {
                if (places[i][2] < places[i - 1][2]) {
                    place = i + 1;
                }
            }
            places[i][3] = place;
        }

    }
}
