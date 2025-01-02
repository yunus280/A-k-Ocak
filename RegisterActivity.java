package com.example.acikocak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etRegisterUsername, etRegisterPassword;
    RadioGroup rgUserType;
    Button btnRegisterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        rgUserType = findViewById(R.id.rgUserType);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);

        // Kayıt Ol Butonu
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etRegisterUsername.getText().toString();
                String password = etRegisterPassword.getText().toString();
                int selectedRoleId = rgUserType.getCheckedRadioButtonId();

                // Kullanıcı tipi seçildi mi?
                if (selectedRoleId == -1) {
                    Toast.makeText(RegisterActivity.this, "Lütfen bir kullanıcı tipi seçin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedRole = findViewById(selectedRoleId);
                String role = selectedRole.getText().toString();

                // Basit doğrulama
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                } else {
                    // Kullanıcı verilerini kaydetme (örneğin SQLite veya Firebase)
                    Toast.makeText(RegisterActivity.this, "Kayıt başarılı! Giriş ekranına yönlendiriliyorsunuz.", Toast.LENGTH_SHORT).show();

                    // Giriş ekranına geri dön
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}