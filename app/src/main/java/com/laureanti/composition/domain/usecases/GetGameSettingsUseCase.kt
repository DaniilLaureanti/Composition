package com.laureanti.composition.domain.usecases

import com.laureanti.composition.domain.entity.GameSettings
import com.laureanti.composition.domain.entity.Level
import com.laureanti.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}