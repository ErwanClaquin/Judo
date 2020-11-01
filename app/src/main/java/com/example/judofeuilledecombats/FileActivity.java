package com.example.judofeuilledecombats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity {
    private boolean firstLauch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        Button buttonStart = findViewById(R.id.buttonStartCombat);
        buttonStart.setOnClickListener(v -> {
            verifStart();
        });
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
                firstLauch = false;
            }
            startActivity(new Intent(FileActivity.this, CombatActivity.class));
        }
    }

    private void resizeTable(TableLayout tableLayout, List<TableRow> tableRowList) {
        //Put in the first
        for (int i = 0; i < tableRowList.size(); i++) {
            Log.v("i : " + i, String.valueOf(tableRowList.size()));

            TableRow row = (TableRow) tableLayout.getChildAt(i + 1);
            EditText editTextNameTo = (EditText) row.getChildAt(0);
            EditText editTextClubTo = (EditText) row.getChildAt(1);
            EditText editTextNameFrom = (EditText) tableRowList.get(i).getChildAt(0);
            EditText editTextClubFrom = (EditText) tableRowList.get(i).getChildAt(1);
            Log.v("NAME", String.valueOf(editTextNameFrom.getText()));
            Log.v("CLUB", String.valueOf(editTextClubFrom.getText()));
            editTextNameTo.setText(editTextNameFrom.getText());
            editTextClubTo.setText(editTextClubFrom.getText());
        }

        //Remove last line
        List<TableRow> tableRowListRemove = new ArrayList<>();
        int len = tableLayout.getChildCount();
        for (int j = tableRowList.size() + 1; j < len; j++) {
            Log.v("j : " + j, String.valueOf(tableRowListRemove.size()));
            tableRowListRemove.add((TableRow) tableLayout.getChildAt(j));
        }
        for (TableRow t : tableRowListRemove) {
            tableLayout.removeView(t);
        }

        //remove useless colomn
        List<TableRow> tableRowColRemove = new ArrayList<>();

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow t = (TableRow) tableLayout.getChildAt(i);
            for (int j = tableRowList.size(); j < tableRowList.size() + tableRowListRemove.size(); j++) {
                t.removeView(t.getChildAt(3 + tableRowList.size()));
            }
        }
    }

    @Override
    public void onBackPressed() {
        //Ask save if anything write
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstLauch) {
            //
        }
    }
}