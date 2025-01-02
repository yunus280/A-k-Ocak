package com.example.acikocak;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RaporlarActivity extends AppCompatActivity {

    private TextView tvReports;
    private ArrayList<String> localReports;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raporlar);

        // UI bileşenlerini başlat
        initializeViews();
        configureTextViewStyle();

        // Firebase referansını başlat
        initializeFirebaseReference();

        // Yerel raporları al
        localReports = getIntent().getStringArrayListExtra("reports");

        if (localReports != null && !localReports.isEmpty()) {
            displayLocalReports(); // Yerel raporları göster
        } else {
            loadFirebaseReports(); // Firebase'den raporları yükle
        }
    }

    private void initializeViews() {
        tvReports = findViewById(R.id.tvReports);
    }

    private void configureTextViewStyle() {
        tvReports.setTextColor(Color.BLACK); // Yazı rengi siyah
        tvReports.setTextSize(16); // Yazı boyutu
        tvReports.setTypeface(null, android.graphics.Typeface.BOLD); // Yazı kalın
        tvReports.setBackgroundColor(Color.parseColor("#DFF6DD")); // Arka plan rengi açık yeşil
    }

    private void initializeFirebaseReference() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Raporlar");
    }

    private void displayLocalReports() {
        StringBuilder reportText = new StringBuilder();
        for (String report : localReports) {
            reportText.append(report).append("\n\n");
        }

        tvReports.setText(reportText.length() == 0 ? "Henüz yerel rapor yok." : reportText.toString());
    }

    private void loadFirebaseReports() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    StringBuilder reports = new StringBuilder();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String details = data.child("details").getValue(String.class);
                        String type = data.child("type").getValue(String.class);
                        String timestamp = data.child("timestamp").getValue(String.class);

                        reports.append("Durum: ").append(type)
                                .append("\n").append(details)
                                .append("\nTarih: ").append(timestamp)
                                .append("\n\n");
                    }

                    tvReports.setText(reports.length() == 0 ? "Henüz kayıtlı bir rapor yok." : reports.toString());
                } else {
                    tvReports.setText("Henüz kayıtlı bir rapor yok.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                tvReports.setText("Raporlar yüklenemedi: " + error.getMessage());
                Toast.makeText(RaporlarActivity.this, "Raporlar yüklenirken hata oluştu.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}