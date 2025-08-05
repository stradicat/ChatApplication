package dev.dmayr.chatapplication.presentation.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.databinding.ActivityProfileBinding

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

        // For MVP, load a dummy user profile or fetch the current user's profile
        profileViewModel.loadUserProfile("current_user_id") // Replace with actual user ID
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
                binding.profileEmailEditText.setText(it.profileImageUrl) // Assuming profileImageUrl is used for email for simplicity in MVP
                // Load profile image using a library like Coil or Glide if profileImageUrl is a URL
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
