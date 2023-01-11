package com.example.quiztime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quiztime.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResultActivity : AppCompatActivity() {
    lateinit var resultBinding : ActivityResultBinding

    val database = FirebaseDatabase.getInstance("https://quiz-time-6e0eb-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val databaseRef = database.reference.child("scores")
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var correct = ""
    var incorrect = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultBinding = ActivityResultBinding.inflate(layoutInflater)
        val view = resultBinding.root
        setContentView(view)

        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (user != null) {
                    correct = snapshot.child(user.uid).child("correct").value.toString()
                    incorrect = snapshot.child(user.uid).child("Incorrect").value.toString()

                    resultBinding.tvCorrect.text = correct
                    resultBinding.tvIncorrect.text = incorrect
                }

            }

            override fun onCancelled(error: DatabaseError) {
                 Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }
        })

        resultBinding.btnPlayAgain.setOnClickListener {

            val intent = Intent(this@ResultActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        resultBinding.btnExit.setOnClickListener {
           finish()
        }
    }
}