package dev.dmayr.chatapplication.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.databinding.ActivityLoginBinding
import dev.dmayr.chatapplication.presentation.ui.salas.RoomsActivity
import dev.dmayr.chatapplication.utils.EncryptionHelper
import dev.dmayr.chatapplication.utils.SharedPrefsHelper
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var prefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is already logged ind
        if (prefsHelper.getUserId() != null) {
            navigateToRooms()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            val userName = binding.editUserName.text.toString().trim()
            if (userName.isNotEmpty()) {
                val userId = UUID.randomUUID().toString()

                // Save user info
                prefsHelper.saveUserId(userId)
                prefsHelper.saveUserName(userName)

                // Generate and save encryption key
                val encryptionKey = EncryptionHelper.generateKey()
                prefsHelper.saveEncryptionKey(EncryptionHelper.keyToString(encryptionKey))

                navigateToRooms()
            }
        }
    }

    private fun navigateToRooms() {
        val intent = Intent(this, RoomsActivity::class.java)
        startActivity(intent)
        finish()
    }
}
