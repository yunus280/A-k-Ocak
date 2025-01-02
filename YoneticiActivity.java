package com.example.acikocak;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class YoneticiActivity extends AppCompatActivity {

    Button btnAddMarkup, btnViewMarkup, btnUpdateMarkup;
    ArrayList<String> markupData = new ArrayList<>(); // Markup verilerini saklamak için liste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yonetici);

        btnAddMarkup = findViewById(R.id.btnAddMarkup);
        btnViewMarkup = findViewById(R.id.btnViewMarkup);
        btnUpdateMarkup = findViewById(R.id.btnUpdateMarkup);

        // Markup Verisi Yükle
        btnAddMarkup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMarkupDialog();
            }
        });

        // Markup Verilerini Görüntüle
        btnViewMarkup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markupData.isEmpty()) {
                    Toast.makeText(YoneticiActivity.this, "Henüz markup verisi eklenmedi.", Toast.LENGTH_SHORT).show();
                } else {
                    showMarkupListDialog("Markup Verileri", markupData);
                }
            }
        });

        // Markup Verilerini Güncelle
        btnUpdateMarkup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markupData.isEmpty()) {
                    Toast.makeText(YoneticiActivity.this, "Güncellenecek veri yok.", Toast.LENGTH_SHORT).show();
                } else {
                    showUpdateMarkupDialog();
                }
            }
        });
    }

    // Markup Verisi Yükleme Dialogu
    private void showAddMarkupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yeni Markup Verisi Ekle");

        // Giriş alanı ekle
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Markup verisini girin...");
        builder.setView(input);

        // Butonlar
        builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String markup = input.getText().toString().trim();
                if (!markup.isEmpty()) {
                    markupData.add(markup);
                    Toast.makeText(YoneticiActivity.this, "Markup verisi eklendi.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(YoneticiActivity.this, "Boş veri eklenemez.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("İptal", null);

        builder.show();
    }

    // Markup Verilerini Görüntüleme
    private void showMarkupListDialog(String title, ArrayList<String> data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // Listeyi birleştir
        StringBuilder formattedData = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            formattedData.append(i + 1).append(". ").append(data.get(i)).append("\n");
        }

        builder.setMessage(formattedData.toString());
        builder.setPositiveButton("Tamam", null);
        builder.show();
    }

    // Markup Verilerini Güncelleme
    private void showUpdateMarkupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Güncellenecek Veriyi Seç");

        // Mevcut verileri bir diziye dönüştür
        String[] items = markupData.toArray(new String[0]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Güncellenecek veriyi al
                String selectedItem = markupData.get(which);
                showEditMarkupDialog(which, selectedItem);
            }
        });

        builder.show();
    }

    // Güncelleme Dialogu
    private void showEditMarkupDialog(final int index, String currentValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Markup Verisini Güncelle");

        // Giriş alanı ekle
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setText(currentValue);
        builder.setView(input);

        // Butonlar
        builder.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedMarkup = input.getText().toString().trim();
                if (!updatedMarkup.isEmpty()) {
                    markupData.set(index, updatedMarkup);
                    Toast.makeText(YoneticiActivity.this, "Markup verisi güncellendi.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(YoneticiActivity.this, "Boş veri ile güncelleme yapılamaz.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("İptal", null);

        builder.show();
    }
}