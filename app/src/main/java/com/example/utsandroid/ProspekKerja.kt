package com.example.utsandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProspekKerja : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_prospek_kerja)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton: LinearLayout = findViewById(R.id.prospek_layout)

        backButton.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Set listener untuk item yang dipilih di BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, Homepage::class.java)
                    startActivity(intent)
                    true // Kembalikan true untuk menandakan event telah ditangani
                }

                R.id.nav_fasilitas -> {
                    // Pindah ke Fasilitas2Activity
                    val intent = Intent(this, Fasilitas2::class.java)
                    startActivity(intent)

                    true // Kembalikan true untuk menandakan event telah ditangani
                }

                R.id.nav_prospek -> {
                    val intent = Intent(this, ProspekKerja::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_akun -> {
                    val intent = Intent(this, InformasiProfil::class.java)
                    startActivity(intent)
                    true
                }
                // Tambahkan case lain untuk item menu lainnya
                else -> false // Kembalikan false jika event tidak ditangani oleh blok ini
            }
        }


    }
}