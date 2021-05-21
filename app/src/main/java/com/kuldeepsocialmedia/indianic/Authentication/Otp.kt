package com.kuldeepsocialmedia.indianic.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.kuldeepsocialmedia.indianic.MainActivity
import com.kuldeepsocialmedia.indianic.R
import kotlinx.android.synthetic.main.activity_otp.*
import java.util.concurrent.TimeUnit

class Otp : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificationId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        auth = FirebaseAuth.getInstance()
        btnVerifyy.setOnClickListener {


            if (!edtPhoneee.text.toString().isNullOrEmpty() || edtPhoneee.text!!.length > 10) {
                verifyMethod()
            } else {
                toast("Please check entered phone number!")

            }
        }

        btnAuthh.setOnClickListener {

            if (!edtPhoneee.text.toString()
                    .isNullOrEmpty() || edtPhoneee.text!!.length > 10 || !edtOtppp.text.toString()
                    .isNullOrEmpty()
            ) {
                authenticateFun()
            } else {
                toast("Please check entered phone number and otp!")

            }
        }

    }

    private fun verificationCallbacks()
    {

        mCallbacks=  object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                signIn(credential)

            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(verification: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verification, p1)

                verificationId = verification.toString()
            }
        }
    }

    private fun verifyMethod() {

        val phone = "+91"+edtPhoneee.text.toString()

        verificationCallbacks()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

            phone,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }

    private fun signIn(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener {

                if (it.isSuccessful)
                {
                    toast("Sign In SucceFully")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

            }

    }

    private fun authenticateFun() {

        val otp = edtOtppp.text.toString()

        val credForOtp: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, otp)

        signIn(credForOtp)


    }

    private fun toast(msg: String)
    {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

}