package com.example.acikocak;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StoperActivity extends AppCompatActivity {

    private Button btnSelectShift, btnSelectMarkup, btnSelectWeigher, btnSelectOre, btnStartProduction, btnStopProduction, btnSendTruck, btnViewReports;
    private EditText etTruckNumber;

    private String selectedShift = "";
    private String selectedMarkup = "";
    private String selectedWeigher = "";
    private String selectedOre = "";
    private String selectedDestination = "";

    private ArrayList<String> truckNumbers = new ArrayList<>();
    private ArrayList<String> stoperReports = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private DatabaseReference kantarDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoper);

        // Firebase bağlantısı
        kantarDatabaseReference = FirebaseDatabase.getInstance().getReference("Kantar");

        // UI bileşenlerini tanımla
        initUI();

        // Düğme işlevlerini ata
        setupButtonListeners();
    }

    private void initUI() {
        btnSelectShift = findViewById(R.id.btnSelectShift);
        btnSelectMarkup = findViewById(R.id.btnSelectMarkup);
        btnSelectWeigher = findViewById(R.id.btnSelectWeigher);
        btnSelectOre = findViewById(R.id.btnSelectOre);
        btnStartProduction = findViewById(R.id.btnStartProduction);
        btnStopProduction = findViewById(R.id.btnStopProduction);
        btnSendTruck = findViewById(R.id.btnSendTruck);
        btnViewReports = findViewById(R.id.btnViewReports);
        etTruckNumber = findViewById(R.id.etTruckNumber);
    }

    private void setupButtonListeners() {
        btnSelectShift.setOnClickListener(v -> showSingleChoiceDialog("Vardiya Seç", new String[]{"08:00-16:00", "16:00-00:00", "00:00-08:00"}, selected -> {
            selectedShift = selected;
            btnSelectShift.setText("Vardiya: " + selectedShift);
        }));

        btnSelectMarkup.setOnClickListener(v -> showSingleChoiceDialog("Markup Seç", new String[]{"Markup 1", "Markup 2", "Markup 3"}, selected -> {
            selectedMarkup = selected;
            btnSelectMarkup.setText("Markup: " + selectedMarkup);
        }));

        btnSelectWeigher.setOnClickListener(v -> showSingleChoiceDialog("Kantarcı Seç", new String[]{"Ali Osman Karataş", "Ümit Akçay", "Ali Boz", "Adnan Kahveci", "Zekeriya Amil"}, selected -> {
            selectedWeigher = selected;
            btnSelectWeigher.setText("Kantarcı: " + selectedWeigher);
        }));

        btnSelectOre.setOnClickListener(v -> {
            String[] ores = {"Sarı Cevher HG", "Sarı Cevher MG", "Sarı Cevher LG", "Yüksek Sülfür", "Oksitli Cevher", "Siyah Cevher HG", "Siyah Cevher MG", "Siyah Cevher LG"};
            showSingleChoiceDialog("Cevher Seç", ores, selected -> {
                selectedOre = selected;
                btnSelectOre.setText("Cevher: " + selectedOre);
                showSingleChoiceDialog("Nereye Gönderiyorsun?", new String[]{"Rom-1", "Rom-2", "Rom-3", "Rom-4", "Rom-5"}, destination -> selectedDestination = destination);
            });
        });

        btnStartProduction.setOnClickListener(v -> startProduction());
        btnStopProduction.setOnClickListener(v -> stopProduction());
        btnSendTruck.setOnClickListener(v -> sendTruck());
        btnViewReports.setOnClickListener(v -> viewReports());
    }

    private void startProduction() {
        if (isAllSelectionsMade()) {
            String productionDetails = getProductionDetails();
            kantarDatabaseReference.child("1").child("productionDetails").setValue(productionDetails)
                    .addOnSuccessListener(aVoid -> {
                        addReport("Üretime Başladı:\n" + productionDetails);
                        Toast.makeText(this, "Üretim Başlatıldı!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Üretim başlatılamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Lütfen tüm seçimleri yapın!", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopProduction() {
        kantarDatabaseReference.child("1").child("productionDetails").removeValue()
                .addOnSuccessListener(aVoid -> {
                    addReport("Üretim durduruldu.");
                    Toast.makeText(this, "Üretim durduruldu!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Üretim durdurulamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void sendTruck() {
        String truckNumber = etTruckNumber.getText().toString().trim();
        if (truckNumber.isEmpty()) {
            Toast.makeText(this, "Lütfen kamyon numarasını girin!", Toast.LENGTH_SHORT).show();
            return;
        }

        truckNumbers.add(truckNumber);
        kantarDatabaseReference.child("1").child("trucks").setValue(truckNumbers)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Kamyon Gönderildi!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Kamyon gönderilemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        etTruckNumber.setText("");
    }

    private void viewReports() {
        Intent intent = new Intent(this, RaporlarActivity.class);
        startActivity(intent);
    }

    private boolean isAllSelectionsMade() {
        return !selectedShift.isEmpty() && !selectedMarkup.isEmpty() && !selectedWeigher.isEmpty() && !selectedOre.isEmpty() && !selectedDestination.isEmpty();
    }

    private String getProductionDetails() {
        return "Vardiya: " + selectedShift +
                "\nMarkup: " + selectedMarkup +
                "\nKantarcı: " + selectedWeigher +
                "\nCevher: " + selectedOre +
                "\nGönderilen Yer: " + selectedDestination +
                "\nTarih: " + dateFormat.format(new Date());
    }

    private void addReport(String report) {
        String timestamp = dateFormat.format(new Date());
        stoperReports.add(report + "\nTarih: " + timestamp);
    }

    private void showSingleChoiceDialog(String title, String[] options, OnSelectionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(options, (dialog, which) -> listener.onSelected(options[which]));
        builder.show();
    }

    interface OnSelectionListener {
        void onSelected(String selected);
    }
}