package com.example.utsandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

class Login : AppCompatActivity() {

    private lateinit var editTextUsernameLogin: EditText
    private lateinit var editTextPasswordLogin: EditText
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Inisialisasi Views
        editTextUsernameLogin = findViewById(R.id.login_edittxt1)
        editTextPasswordLogin = findViewById(R.id.login_edittxt2)

        val btnLogin: Button = findViewById(R.id.login_btn1)
        val txtRegister: TextView = findViewById(R.id.login_txt3)


        btnLogin.setOnClickListener {
            performLogin()
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }


    }

    private fun performLogin() {
        val inputUsernameOrEmail = editTextUsernameLogin.text.toString().trim()
        val inputPassword = editTextPasswordLogin.text.toString()

        if (inputUsernameOrEmail.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(this, "Username/Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil data yang tersimpan dari SharedPreferences
        val storedUsername = sharedPreferences.getString(KEY_USERNAME, null)
        val storedEmail = sharedPreferences.getString(KEY_EMAIL, null)
        val storedPasswordHash = sharedPreferences.getString(KEY_PASSWORD_HASH, null)

        if (storedUsername == null || storedPasswordHash == null) {
            // Ini berarti belum ada pengguna yang terdaftar atau data hilang
            Toast.makeText(this, "Akun tidak ditemukan. Silakan register terlebih dahulu.", Toast.LENGTH_LONG).show()
            return
        }

        // Hash password yang diinput pengguna dengan algoritma yang SAMA seperti saat registrasi
        val inputPasswordHash = hashString(inputPassword, "SHA-256")

        // Bandingkan username/email dan hash password
        val isUsernameMatch = inputUsernameOrEmail.equals(storedUsername, ignoreCase = true)
        val isEmailMatch = inputUsernameOrEmail.equals(storedEmail, ignoreCase = true)
        val isPasswordMatch = inputPasswordHash == storedPasswordHash

        if ((isUsernameMatch || isEmailMatch) && isPasswordMatch) {
            // Login Berhasil
            Toast.makeText(this, "Login Berhasil! Selamat datang, $storedUsername!", Toast.LENGTH_SHORT).show()

            // Pindah ke halaman Homepage atau halaman utama aplikasi Anda
            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Username/Email atau Password salah.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hashString(input: String, algorithm: String): String {
        return MessageDigest.getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

}