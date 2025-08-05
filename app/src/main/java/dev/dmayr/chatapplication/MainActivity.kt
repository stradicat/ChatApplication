package dev.dmayr.chatapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.presentation.ui.login.LoginActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Directly redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finish MainActivity so user can't go back to it
    }
}
