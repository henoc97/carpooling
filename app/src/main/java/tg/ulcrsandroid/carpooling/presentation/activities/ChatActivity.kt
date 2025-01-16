package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.databinding.ActivityChatBinding
import tg.ulcrsandroid.carpooling.presentation.adapters.ChatAdapter

class ChatActivity : AppCompatActivity() {

    private lateinit var ui: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ui = ActivityChatBinding.inflate(layoutInflater)
        setContentView(ui.root)
        super.onCreate(savedInstanceState)

//        val utilisateurList
//        val adapter = ChatAdapter()
    }
}