package com.kuldeepsocialmedia.indianic.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.kuldeepsocialmedia.indianic.Authentication.Otp
import com.kuldeepsocialmedia.indianic.MainActivity
import com.kuldeepsocialmedia.indianic.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mAuth = FirebaseAuth.getInstance()

        splash.alpha = 0f

        splash.animate().setDuration(2000).alpha(1f)
            .withEndAction{

                if (mAuth.currentUser != null) {

                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
                else
                {
                    val i = Intent(this, Otp::class.java)
                    startActivity(i)
                    finish()
                }
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            }

    }
}