package com.example.utsandroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.security.MessageDigest

private const val PREFS_NAME = "user_credentials_prefs"
private const val KEY_USERNAME = "username"
private const val KEY_EMAIL = "email"
private const val KEY_PASSWORD_HASH = "password_hash"

class Register : AppCompatActivity() {

    private lateinit var editTextUsernameRegister: EditText
    private lateinit var editTextEmailRegister: EditText
    private lateinit var editTextPasswordRegister: EditText
    private lateinit var editTextConfirmPasswordRegister: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Inisialisasi Views
        editTextUsernameRegister = findViewById(R.id.register_edit1)
        editTextEmailRegister = findViewById(R.id.register_edit2)
        editTextPasswordRegister = findViewById(R.id.register_edit3)
        editTextConfirmPasswordRegister = findViewById(R.id.register_edit4)

        val txtLogin: TextView = findViewById(R.id.register_txt3)
        val btnRegister: Button = findViewById(R.id.register_btn1)
        val backButton: ImageView = findViewById(R.id.register_image2)

        txtLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            performRegistration()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, SplashArt::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun performRegistration() {
        val username = editTextUsernameRegister.text.toString().trim()
        val email = editTextEmailRegister.text.toString().trim()
        val password = editTextPasswordRegister.text.toString()
        val confirmPassword = editTextConfirmPasswordRegister.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password dan konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        // --- Simpan Kredensial Pengguna ---
        val passwordHash = hashString(password, "SHA-256")

        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PASSWORD_HASH, passwordHash)
        editor.apply() // Simpan secara asynchronous

        Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()

        // Pindah ke halaman Login setelah registrasi berhasil
        val intent = Intent(this, Login::class.java) // Ganti LoginActivity dengan nama Activity Login Anda

        startActivity(intent)
        finish() // Tutup halaman register agar tidak bisa kembali dengan tombol back
    }

    private fun hashString(input: String, algorithm: String): String {
        return MessageDigest.getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

}