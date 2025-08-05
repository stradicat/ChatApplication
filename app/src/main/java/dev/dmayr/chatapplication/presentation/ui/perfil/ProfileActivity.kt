package dev.dmayr.chatapplication.presentation.ui.perfil


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.databinding.ActivityProfileBinding
import dev.dmayr.chatapplication.utils.SharedPrefsHelper
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    @Inject
    lateinit var prefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserInfo()
        setupClickListeners()
    }

    private fun loadUserInfo() {
        binding.textUserId.text = "ID: ${prefsHelper.getUserId()}"
        binding.textUserName.text = "Name: ${prefsHelper.getUserName()}"
        binding.textEncryptionStatus.text = if (prefsHelper.getEncryptionKey() != null) {
            "Encryption: Enabled"
        } else {
            "Encryption: Disabled"
        }
    }

    private fun setupClickListeners() {
        binding.buttonLogout.setOnClickListener {
            // Clear all preferences
            prefsHelper.saveUserId("")
            prefsHelper.saveUserName("")
            prefsHelper.saveEncryptionKey("")

            finish()
        }
    }
}
