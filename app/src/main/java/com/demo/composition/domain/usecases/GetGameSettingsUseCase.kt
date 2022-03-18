package com.demo.composition.domain.usecases

import com.demo.composition.domain.entity.GameSettings
import com.demo.composition.domain.entity.Level
import com.demo.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings{
        return repository.getGameSettings(level)
    }
}