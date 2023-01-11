package com.example.quiztime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.quiztime.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.btnStartQuiz.setOnClickListener {
           val intent = Intent(this,QuizActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onCreateOptionsMenu(the_menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,the_menu)
       return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logOut) {
            FirebaseAuth.getInstance().signOut()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(this,gso)
            googleSignInClient.signOut().addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    Toast.makeText(applicationContext,"Successfully Signed Out",Toast.LENGTH_SHORT).show()

                }
            }


            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        return true

    }



    
}