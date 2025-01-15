package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.EmailPasswordAuthStrategy

class signUpActivity : AppCompatActivity(){
    private lateinit var authContext: AuthContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Par défaut, stratégie Email/Password
        val emailPasswordAuthStrategy = EmailPasswordAuthStrategy()
        authContext = AuthContext()
        authContext.updateStrategy(emailPasswordAuthStrategy)

        val signUpButton: Button = findViewById(R.id.sin)
        // authContext.sInscrire("test9785@example.com", "password123", "John Doe")
    }



}