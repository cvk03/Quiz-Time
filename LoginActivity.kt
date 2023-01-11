package com.example.quiztime

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.quiztime.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        var view = loginBinding.root
        setContentView(view)

        val textOfGoogleBtn = loginBinding.btnGoogleSignIn.getChildAt(0) as TextView
        textOfGoogleBtn.text = "Sign In with Google"
        textOfGoogleBtn.setTextColor(Color.BLACK)
        textOfGoogleBtn.textSize = 18F

        registerActivityForGoogleSignIn()

        //normal sign in
        loginBinding.btnSignIn.setOnClickListener {
            val userEmail = loginBinding.etEmail.text.toString()
            val userPass = loginBinding.etPass.text.toString()
            signInUser(userEmail,userPass)
        }

        //google sign in
        loginBinding.btnGoogleSignIn.setOnClickListener {


        }

        //sign up
        loginBinding.tvSignUp.setOnClickListener {

            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent )

        }

        //forget password
        loginBinding.tvForgetPass.setOnClickListener {
            val intent = Intent(this@LoginActivity,ForgotPassActivity::class.java)
            startActivity(intent)

        }
    }

    fun signInUser(userEmail:String,userPass : String)
    {
     auth.signInWithEmailAndPassword(userEmail,userPass).addOnCompleteListener { task->
         if(task.isSuccessful)
         {
             Toast.makeText(applicationContext,"Welcome to Quiz Time!!!",Toast.LENGTH_SHORT).show()

             val intent = Intent(this@LoginActivity,MainActivity::class.java)
             startActivity(intent)
             finish()

         }else{
             Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()

         }
     }
    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser
        if(user!=null)
        {
            Toast.makeText(applicationContext,"Welcome to Quiz Time!!!",Toast.LENGTH_SHORT).show()

            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signInGoogle()
    {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("955576282858-cntipsek39mr1ssgbv5tjnf898ujchqv.apps.googleusercontent.com")
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        signIn()
    }

    private fun signIn()
    {
        val signInIntent : Intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    private fun registerActivityForGoogleSignIn()
    {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result->
                val resultCode = result.resultCode
                val data = result.data
                if(resultCode == RESULT_OK && data!=null)
                {
                    val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseSignInWithGoogle(task)
                }
            })

    }

    private fun firebaseSignInWithGoogle(task : Task<GoogleSignInAccount>)
    {
        try{
        val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext,"Welcome to Quiz Game!!!",Toast.LENGTH_SHORT).show()
            val intent  = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }catch (e: ApiException)
        {
            Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }
    }
    private fun firebaseGoogleAccount(account : GoogleSignInAccount)
    {
        val authCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task->
            if(task.isSuccessful)
            {
                //val user = auth.currentUser
            }else{

            }
        }
    }


}