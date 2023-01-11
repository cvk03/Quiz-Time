package com.example.quiztime

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.GREEN
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.ArraySet
import android.util.Log
import android.util.Range
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.quiztime.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.DriverManager.println
import java.util.*

class QuizActivity : AppCompatActivity() {

    lateinit var quizBinding : ActivityQuizBinding

    val database = FirebaseDatabase.getInstance("https://quiz-time-6e0eb-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val databaseReference = database.reference.child("questions")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 0

    var userAnswer = ""
    var userCorrect = 0
    var userIncorrect =0

    lateinit var timer : CountDownTimer
    private val totalTime = 30000L
    var timerContinue = false
    var leftTime = totalTime

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef = database.reference

    @RequiresApi(Build.VERSION_CODES.M)
    val questions = ArraySet<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root

        setContentView(view)

        do{
            val random = Random()
            val number = random.nextInt(9)
            Log.d("number",number.toString())
            if(number!=0){
                questions.add(number)
            }

        }while(questions.size<5)

        Log.d("numberOfQuestions",questions.toString())

        gameLogic()


        quizBinding.btnFinish.setOnClickListener { sendScore() }

        quizBinding.btnNext.setOnClickListener { resetTimer()
            gameLogic()}
        quizBinding.tvA.setOnClickListener { userAnswer = "a"
            pauseTimer()
            if(correctAnswer == userAnswer)
            {
                quizBinding.tvA.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.tvCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.tvA.setBackgroundColor(Color.RED)
                userIncorrect++
                quizBinding.tvIncorrect.text = userIncorrect.toString()
                findAnswer()
            }
             disableClickingOfOptions()

        }
        quizBinding.tvB.setOnClickListener { userAnswer = "b"
            pauseTimer()
            if(correctAnswer == userAnswer)
            {
                quizBinding.tvB.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.tvCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.tvB.setBackgroundColor(Color.RED)
                userIncorrect++
                quizBinding.tvIncorrect.text = userIncorrect.toString()
                findAnswer()
            }
            disableClickingOfOptions()
        }
        quizBinding.tvC.setOnClickListener { userAnswer = "c"
            pauseTimer()
            if(correctAnswer == userAnswer)
            {
                quizBinding.tvC.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.tvCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.tvC.setBackgroundColor(Color.RED)
                userIncorrect++
                quizBinding.tvIncorrect.text = userIncorrect.toString()
                findAnswer()

            }
            disableClickingOfOptions()
        }
        quizBinding.tvD.setOnClickListener { userAnswer = "d"
            pauseTimer()
            if(correctAnswer == userAnswer)
            {
                quizBinding.tvD.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.tvCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.tvD.setBackgroundColor(Color.RED)
                userIncorrect++
                quizBinding.tvIncorrect.text = userIncorrect.toString()
                findAnswer()

            }
            disableClickingOfOptions()
        }
    }

    private fun gameLogic()
    {
        restoreOptions()
        //quizBinding.tvHint.text = "called gameLogic "+questionNumber.toString()
        databaseReference.addValueEventListener(object : ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onDataChange(snapshot: DataSnapshot) {
                //quizBinding.tvHint.text = "inside datasnapshot "+questionNumber.toString()
                questionCount = snapshot.childrenCount.toInt()

                if(questionNumber<questions.size)
                {
                    question = snapshot.child(questions.valueAt(questionNumber).toString()).child("q").value.toString()
                    answerA = snapshot.child(questions.valueAt(questionNumber).toString()).child("a").value.toString()
                    answerB = snapshot.child(questions.valueAt(questionNumber).toString()).child("b").value.toString()
                    answerC = snapshot.child(questions.valueAt(questionNumber).toString()).child("c").value.toString()
                    answerD = snapshot.child(questions.valueAt(questionNumber).toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questions.valueAt(questionNumber).toString()).child("answer").value.toString()

                    quizBinding.tvQuestion.text = question
                    quizBinding.tvA.text = answerA
                    quizBinding.tvB.text = answerB
                    quizBinding.tvC.text = answerC
                    quizBinding.tvD.text = answerD

                    quizBinding.pbQuiz.visibility = View.INVISIBLE
                    quizBinding.llInfo.visibility = View.VISIBLE
                    quizBinding.llQuestion.visibility = View.VISIBLE
                    quizBinding.llBtns.visibility = View.VISIBLE
                    startTimer()
                }
                else{
                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Time")
                        .setMessage("Congratulations!!!\nYou have completed the quiz. Do you want to see the results?")
                        .setCancelable(false)
                        .setPositiveButton("See result"){dialogInterface,position->
                           sendScore()
                        }
                        .setNegativeButton("Play Again"){dialogMessage,position->
                            val intent = Intent(this@QuizActivity,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    dialogMessage.create().show()
                }
                questionNumber++
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun findAnswer()
    {
        when(correctAnswer)
        {
            "a"->quizBinding.tvA.setBackgroundColor(Color.GREEN)
            "b"->quizBinding.tvB.setBackgroundColor(Color.GREEN)
            "c"->quizBinding.tvC.setBackgroundColor(Color.GREEN)
            "d"->quizBinding.tvD.setBackgroundColor(Color.GREEN)

        }
    }

    fun disableClickingOfOptions()
    {
        quizBinding.tvA.isClickable = false
        quizBinding.tvB.isClickable = false
        quizBinding.tvC.isClickable = false
        quizBinding.tvD.isClickable = false

    }

    fun restoreOptions()
    {
        quizBinding.tvA.setBackgroundResource(R.drawable.options_shape)
        quizBinding.tvB.setBackgroundResource(R.drawable.options_shape)
        quizBinding.tvC.setBackgroundResource(R.drawable.options_shape)
        quizBinding.tvD.setBackgroundResource(R.drawable.options_shape)
        quizBinding.tvA.isClickable = true
        quizBinding.tvB.isClickable = true
        quizBinding.tvC.isClickable = true
        quizBinding.tvD.isClickable = true

    }

    private fun startTimer()
    {
        timer = object : CountDownTimer(leftTime,1000){
            override fun onTick(millisUntilFinished: Long) {

                leftTime = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                disableClickingOfOptions()
                resetTimer()
                updateCountDownText()
                quizBinding.tvQuestion.text= "Sorry, Time is up! Continue with the next question."
                timerContinue =false
            }

        }.start()
        timerContinue =true
    }

    fun updateCountDownText()
    {
        val remainingTime : Int = (leftTime/1000).toInt()
        quizBinding.tvTime.text = remainingTime.toString()
    }

    fun pauseTimer()
    {
         timer.cancel()
        timerContinue = false
    }

    fun resetTimer()
    {
        pauseTimer()
        leftTime = totalTime
        updateCountDownText()

    }

    fun sendScore()
    {
        val userUID = user?.uid
        if (userUID != null) {
            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreRef.child("scores").child(userUID).child("Incorrect").setValue(userIncorrect).addOnSuccessListener {
                Toast.makeText(applicationContext,"Scores sent to database successfully",Toast.LENGTH_SHORT).show()

                val intent = Intent(this@QuizActivity,ResultActivity::class.java)
                startActivity(intent)
                finish()

            }
        }

    }

}


