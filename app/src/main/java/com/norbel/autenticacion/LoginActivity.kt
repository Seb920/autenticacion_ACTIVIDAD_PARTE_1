package com.norbel.autenticacion

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.norbel.autenticacion.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    private var semail: String = ""
    private var spassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            performLoginOrRegistration()
        }
    }

    private fun performLoginOrRegistration() {
        semail    = binding.username.text.toString().trim()
        spassword = binding.password.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
            binding.username.error = "Formato inválido de email"
            binding.loading.visibility = View.GONE
        } else if (TextUtils.isEmpty(spassword) || spassword.length < 6) {
            binding.password.error = "El password debe tener al menos 6 caracteres"
            binding.loading.visibility = View.GONE
        } else {
            loginUser()
        }
    }

    private fun loginUser() {
        mAuth.signInWithEmailAndPassword(semail, spassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.loading.visibility = View.GONE
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    registerUser()
                }
            }
    }

    private fun registerUser() {
        mAuth.createUserWithEmailAndPassword(semail, spassword)
            .addOnCompleteListener(this) { task ->
                binding.loading.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Error: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}