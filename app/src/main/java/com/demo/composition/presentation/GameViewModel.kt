package com.demo.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.composition.R
import com.demo.composition.data.GameRepositoryImpl
import com.demo.composition.domain.entity.GameResult
import com.demo.composition.domain.entity.GameSettings
import com.demo.composition.domain.entity.Level
import com.demo.composition.domain.entity.Question
import com.demo.composition.domain.usecases.GenerateQuestionUseCase
import com.demo.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private val repository = GameRepositoryImpl

    val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentRightAnswers = MutableLiveData<Int>()
    val percentRightAnswers: LiveData<Int>
        get() = _percentRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
        updateProgress()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswer
        )
        _enoughCount.value = countOfRightAnswers >= gameSettings.minCountOfRightAnswer
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestions == null){
            return 0
        }
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    private fun formatTime(p0: Long): String {
        val seconds = p0 / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * seconds)
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS
        ) {
            override fun onTick(p0: Long) {
                _formattedTime.postValue(formatTime(p0))
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}