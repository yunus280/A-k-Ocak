package com.example.acikocak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // EditText ve Buttonları tanımlama
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Giriş Yap Butonu
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Basit doğrulama
                String role = validateUser(username, password);
                if (role != null) {
                    Toast.makeText(LoginActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                    navigateToRoleSelection(role);
                } else {
                    Toast.makeText(LoginActivity.this, "Kullanıcı adı veya şifre yanlış!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Kayıt Ol Butonu
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Kullanıcı doğrulama metodu
    private String validateUser(String username, String password) {
        // Kullanıcı rolüne göre giriş kontrolü
        if (username.equals("stoper") && password.equals("1234")) {
            return "Stoper";
        } else if (username.equals("kantar") && password.equals("5678")) {
            return "Kantar";
        } else if (username.equals("yonetici") && password.equals("admin")) {
            return "Yönetici";
        } else {
            return null;
        }
    }

    // Rol seçimine göre aktiviteye yönlendirme
    private void navigateToRoleSelection(String role) {
        Intent intent;
        switch (role) {
            case "Stoper":
                intent = new Intent(LoginActivity.this, StoperActivity.class);
                break;
            case "Kantar":
                intent = new Intent(LoginActivity.this, KantarActivity.class);
                break;
            case "Yönetici":
                intent = new Intent(LoginActivity.this, YoneticiActivity.class);
                break;
            default:
                Toast.makeText(this, "Geçersiz rol seçimi!", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
    }
}