package com.laureanti.composition.domain.repository

import com.laureanti.composition.domain.entity.GameSettings
import com.laureanti.composition.domain.entity.Level
import com.laureanti.composition.domain.entity.Question

interface GameRepository {
    fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question
    fun getGameSettings(level: Level): GameSettings
}