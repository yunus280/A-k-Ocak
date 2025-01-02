package com.example.acikocak;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import com.example.acikocak.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and set up ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Yeni "Kamyon Seç" alanları
        binding.btnKamyonGonder.setOnClickListener(v -> {
            String kamyonNo = binding.etKamyonSec.getText().toString();
            if (kamyonNo.isEmpty()) {
                Toast.makeText(this, "Lütfen bir kamyon numarası girin!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Kamyon Numarası Gönderildi: " + kamyonNo, Toast.LENGTH_SHORT).show();
            }
        });

        // Mevcut butonlara tıklama işlevleri
        binding.btnVardiyaSec.setOnClickListener(v -> showVardiyaDialog());
        binding.btnMarkupSec.setOnClickListener(v -> showMarkupDialog());
        binding.btnKantarciSec.setOnClickListener(v -> showKantarciDialog());
        binding.btnCevherSec.setOnClickListener(v -> showCevherDialog());
        binding.btnUretimeBasla.setOnClickListener(v -> startProduction());
        binding.btnRaporlariGoruntule.setOnClickListener(v -> showRaporOptions());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    // Örnek metodlar (boş bırakılmıştır)
    private void showVardiyaDialog() {
        // Vardiya seçimi ile ilgili işlemler
    }

    private void showMarkupDialog() {
        // Markup seçimi ile ilgili işlemler
    }

    private void showKantarciDialog() {
        // Kantarcı seçimi ile ilgili işlemler
    }

    private void showCevherDialog() {
        // Cevher seçimi ile ilgili işlemler
    }

    private void startProduction() {
        // Üretime başlama işlemleri
    }

    private void showRaporOptions() {
        // Rapor görüntüleme işlemleri
    }
}