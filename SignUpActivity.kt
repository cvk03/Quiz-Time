package com.example.quiztime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quiztime.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var signupBinding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signupBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)

        signupBinding.btnSignUp.setOnClickListener {
            val email = signupBinding.etEmailSignup.text.toString()
            val pass = signupBinding.etPassSignup.text.toString()

            signupWithFirebase(email,pass)

        }
    }

    fun signupWithFirebase(email:String, pass:String)
    {
        signupBinding.pbSignup.visibility = View.VISIBLE
        signupBinding.btnSignUp.isClickable = false

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener { task->
            if(task.isSuccessful)
            {
                Toast.makeText(applicationContext,"Your account has been created successfully!!!",Toast.LENGTH_SHORT).show()
                finish()
                signupBinding.pbSignup.visibility = View.INVISIBLE
                signupBinding.btnSignUp.isClickable = true

            }else{

                Toast.makeText(applicationContext, task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()


            }
        }


    }
}