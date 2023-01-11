package com.example.quiztime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quiztime.databinding.ActivityForgotPassBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {
    lateinit var resetPassBinding: ActivityForgotPassBinding
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetPassBinding = ActivityForgotPassBinding.inflate(layoutInflater)
        val view = resetPassBinding.root

        setContentView(view)

        resetPassBinding.btnForgetPass.setOnClickListener {
            val email = resetPassBinding.etEmailResetPass.text.toString()
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    Toast.makeText(applicationContext,"Password reset email has been sent to your email address",Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}