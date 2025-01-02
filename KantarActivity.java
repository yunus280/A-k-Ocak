package com.example.acikocak;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KantarActivity extends AppCompatActivity {

    private Button btnReportView, btnRim4, btnRim6, btnSendTruck;
    private EditText etTruckNumber;
    private TextView tvBox1, tvBox2, tvBox3;

    private ArrayList<String> kantarReports = new ArrayList<>();
    private ArrayList<String> truckNumbers = new ArrayList<>();

    private DatabaseReference kantarDatabaseReference;
    private DatabaseReference stoperDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kantar);

        // Firebase bağlantısı
        kantarDatabaseReference = FirebaseDatabase.getInstance().getReference("Kantar");
        stoperDatabaseReference = FirebaseDatabase.getInstance().getReference("Stoper");

        // UI öğelerini tanımla
        btnReportView = findViewById(R.id.btnReportView);
        btnRim4 = findViewById(R.id.btnRim4);
        btnRim6 = findViewById(R.id.btnRim6);
        btnSendTruck = findViewById(R.id.btnSendTruck);
        etTruckNumber = findViewById(R.id.etTruckNumber);
        tvBox1 = findViewById(R.id.tvBox1);
        tvBox2 = findViewById(R.id.tvBox2);
        tvBox3 = findViewById(R.id.tvBox3);

        // Firebase'den gelen raporları dinle
        listenForReports();

        // Rapor Görüntüle
        btnReportView.setOnClickListener(v -> viewReports());

        // Ara Stok Rim-4 Gönderim
        btnRim4.setOnClickListener(v -> showDestinationDialog("ARA STOK RİM-4 GÖNDERİM"));

        // Ara Stok Rim-6 Gönderim
        btnRim6.setOnClickListener(v -> showDestinationDialog("ARA STOK RİM-6 GÖNDERİM"));

        // Kamyon Gönder
        btnSendTruck.setOnClickListener(v -> sendTruck());
    }

    private void listenForReports() {
        stoperDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = 0;
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String productionDetails = formatProductionDetails(data);
                        if (count == 0) {
                            updateBox(tvBox1, productionDetails, Color.GREEN);
                        } else if (count == 1) {
                            updateBox(tvBox2, productionDetails, Color.GREEN);
                        } else if (count == 2) {
                            updateBox(tvBox3, productionDetails, Color.GREEN);
                        }
                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(KantarActivity.this, "Veriler alınamadı: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendTruck() {
        String truckNumber = etTruckNumber.getText().toString().trim();
        if (truckNumber.isEmpty()) {
            Toast.makeText(this, "Lütfen kamyon numarasını girin!", Toast.LENGTH_SHORT).show();
            return;
        }

        truckNumbers.add(truckNumber);
        kantarDatabaseReference.child("trucks").setValue(truckNumbers);
        etTruckNumber.setText(""); // Giriş alanını temizle
        Toast.makeText(this, "Kamyon Gönderildi!", Toast.LENGTH_SHORT).show();
    }

    private void showDestinationDialog(String processName) {
        String[] destinations = {
                "Rom-1", "Rom-2", "Rom-3", "Rom-4",
                "Rom-5", "Rom-6", "Rom-7", "Rom-8",
                "Bunkerin Sağ", "Bunkerin Sol", "Rom-5 Karşısı"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(processName);
        builder.setItems(destinations, (dialog, which) -> {
            String selectedDestination = destinations[which];
            kantarReports.add(processName + " Gönderildi: " + selectedDestination);
            Toast.makeText(this, processName + " için " + selectedDestination + " seçildi.", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    private void viewReports() {
        StringBuilder reportBuilder = new StringBuilder();
        for (String report : kantarReports) {
            reportBuilder.append(report).append("\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Raporlar");
        builder.setMessage(reportBuilder.toString().isEmpty() ? "Henüz rapor yok!" : reportBuilder.toString());
        builder.setPositiveButton("Tamam", null);
        builder.show();
    }

    private void updateBox(TextView box, String text, int color) {
        box.setText(text);
        box.setBackgroundColor(color);
    }

    private String formatProductionDetails(DataSnapshot data) {
        String shift = data.child("shift").getValue(String.class);
        String markup = data.child("markup").getValue(String.class);
        String weigher = data.child("weigher").getValue(String.class);
        String ore = data.child("ore").getValue(String.class);
        String destination = data.child("destination").getValue(String.class);
        String timestamp = data.child("timestamp").getValue(String.class);

        return "Vardiya: " + shift +
                "\nMarkup: " + markup +
                "\nKantarcı: " + weigher +
                "\nCevher: " + ore +
                "\nGönderilen Yer: " + destination +
                "\nTarih: " + timestamp;
    }
}