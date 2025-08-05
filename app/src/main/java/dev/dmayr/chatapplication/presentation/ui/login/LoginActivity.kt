package dev.dmayr.chatapplication.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.databinding.ActivityLoginBinding
import dev.dmayr.chatapplication.presentation.ui.viewmodel.LoginViewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            loginViewModel.authenticate(username, password)
        }

        loginViewModel.authenticationResult.observe(this) { result ->
            result.onSuccess { user ->
                Toast.makeText(this, "Welcome, ${user.username}!", Toast.LENGTH_SHORT).show()
                // Navigate to SalasActivity on successful login
                val intent = Intent(this, SalasActivity::class.java)
                startActivity(intent)
                finish() // Close LoginActivity so user can't go back
            }.onFailure { exception ->
                Toast.makeText(
                    this,
                    "Login failed: ${exception.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
