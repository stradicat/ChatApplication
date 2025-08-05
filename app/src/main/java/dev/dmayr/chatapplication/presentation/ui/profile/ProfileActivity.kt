package dev.dmayr.chatapplication.presentation.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.databinding.ActivityProfileBinding
import dev.dmayr.chatapplication.presentation.ui.viewmodel.ProfileViewModel

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()

        profileViewModel.loadUserProfile("current_user_id")
    }

    private fun setupClickListeners() {
        binding.editProfileButton.setOnClickListener {
            toggleEditMode(true)
        }

        binding.saveProfileButton.setOnClickListener {
            val updatedEmail = binding.profileEmailEditText.text.toString()
            // In a real app, you'd get the current user object and update its email
            // For MVP, we'll just simulate an update
            profileViewModel.updateUserProfile(updatedEmail)
            toggleEditMode(false)
        }
    }

    private fun observeViewModel() {
        profileViewModel.userProfile.observe(this) { user ->
            user?.let {
                binding.profileNameTextView.text = it.username
                binding.profileEmailEditText.setText(it.profileImageUrl)
            }
        }

        profileViewModel.profileUpdateStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleEditMode(enable: Boolean) {
        isEditing = enable
        binding.profileEmailEditText.isEnabled = enable
        binding.editProfileButton.visibility = if (enable) View.GONE else View.VISIBLE
        binding.saveProfileButton.visibility = if (enable) View.VISIBLE else View.GONE
    }
}
