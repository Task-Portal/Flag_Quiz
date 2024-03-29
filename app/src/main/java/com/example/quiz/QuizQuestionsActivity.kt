package com.example.quiz

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.quiz.databinding.ActivityQuizQuestionsBinding

/*
Implement a ClickListener on the Activity
and ensure the required views are listening to the click.
*/
class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy {
        ActivityQuizQuestionsBinding.inflate(layoutInflater)
    }
    private var progressBar: ProgressBar? = null
    private var tvProgress: TextView? = null
    private var tvQuestion: TextView? = null
    private var ivFlag: ImageView? = null
    private var tvOptionOne: TextView? = null
    private var tvOptionTwo: TextView? = null
    private var tvOptionThree: TextView? = null
    private var tvOptionFour: TextView? = null
    private var buttonSubmit: Button? = null
    private var currentPosition: Int = 0
    private val questionsList: ArrayList<Question> = arrayListOf()
    private var correctAnswers: Int = 0
    private var userName: String = ""
    private var selectedOptionPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progressBar = binding.progressBar
        tvProgress = binding.tvProgress
        tvQuestion = binding.tvQuestion
        ivFlag = binding.ivFlag
        tvOptionOne = binding.tvOptionOne
        tvOptionTwo = binding.tvOptionTwo
        tvOptionThree = binding.tvOptionThree
        tvOptionFour = binding.tvOptionFour
        buttonSubmit = binding.btnSubmit
        questionsList.addAll(Constants.getQuestions())

        setQuestion()

        tvOptionOne?.setOnClickListener(this)
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)
        buttonSubmit?.setOnClickListener(this)
    }

    private fun defaultOptionsView() {
        val options = listOf(
            tvOptionOne,
            tvOptionTwo,
            tvOptionThree,
            tvOptionFour
        )
        for (option in options) {
            option?.setTextColor(
                Color.parseColor("#7A8089")
            )
            option?.typeface = Typeface.DEFAULT
            option?.background = ContextCompat.getDrawable(
                this@QuizQuestionsActivity,
                R.drawable.default_option_border_bg
            )
        }
    }

    private fun setQuestion() {
        val question: Question = questionsList[currentPosition]
        defaultOptionsView()
        if (currentPosition == questionsList.size) {
            buttonSubmit?.text = "Конец"
        } else {
            buttonSubmit?.text = "Проверить"
        }
        progressBar?.progress = currentPosition
        progressBar?.max = questionsList.size
        val no = currentPosition + 1
        tvProgress?.text = resources.getString(
            R.string.progress_Text, no, progressBar?.max
        )
        tvQuestion?.text = question.question
        ivFlag?.setImageResource(question.image)
        tvOptionOne?.text = question.optionOne
        tvOptionTwo?.text = question.optionTwo
        tvOptionThree?.text = question.optionThree
        tvOptionFour?.text = question.optionFour
    }

    private fun selectedOptionView(
        tv: TextView,
        selectedOptionNum: Int
    ) {
        defaultOptionsView()
        selectedOptionPosition = selectedOptionNum
        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            R.drawable.selected_option_border_bg
        )
    }

    private fun answerView(
        answer: Int, drawableView:
        Int
    ) {
        /*
        This statement sets a Green or Red background to the option TextView if
        the answer is right or wrong respectively
        */
        when (answer) {
            1 -> {
                tvOptionOne?.background =
                    ContextCompat.getDrawable(
                        this@QuizQuestionsActivity,
                        drawableView
                    )
            }

            2 -> {
                tvOptionTwo?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }

            3 -> {
                tvOptionThree?.background =
                    ContextCompat.getDrawable(
                        this@QuizQuestionsActivity,
                        drawableView
                    )
            }

            4 -> {
                tvOptionFour?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            tvOptionOne?.id -> {
                tvOptionOne?.let {
                    selectedOptionView(it, 1)
                }
            }

            tvOptionTwo?.id -> {
                tvOptionTwo?.let {
                    selectedOptionView(it, 2)
                }
            }

            tvOptionThree?.id -> {
                tvOptionThree?.let {
                    selectedOptionView(it, 3)
                }
            }

            tvOptionFour?.id -> {
                tvOptionFour?.let {
                    selectedOptionView(it, 4)
                }
            }

            /*
When the submit button is clicked, the background for the answer Views
and the text is changed to next if there are questions left or changed to
finish if it is the last question. If the text is set to finish, clicking it takes the
user to the Result Activity.
*/

            buttonSubmit?.id -> {
                if (selectedOptionPosition == 0) {
                    currentPosition++
                    when {
                        currentPosition <= questionsList.size -
                                1 -> {
                            setQuestion()
                        }

                        else -> {
                            val intent =
                                Intent(
                                    this@QuizQuestionsActivity,
                                    ResultActivity::class.java
                                )
                            intent.putExtra(
                                Constants.USER_NAME,
                                userName
                            )
                            intent.putExtra(Constants.CORRECT_ANSWERS, correctAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, questionsList.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val question = questionsList[currentPosition]
                    if (question.correctAnswer != selectedOptionPosition) {
                        answerView(
                            selectedOptionPosition,
                            R.drawable.wrong_option_border
                        )
                    } else {
                        correctAnswers++
                    }


                    answerView(
                        question.correctAnswer,
                        R.drawable.correct_option_border
                    )
                    if (currentPosition == questionsList.size - 1) {
                        buttonSubmit?.text = "Конец"
                    } else {
                        buttonSubmit?.text = "Еще один вопрос"
                    }
                    selectedOptionPosition = 0
                }


            }


        }

    }
}