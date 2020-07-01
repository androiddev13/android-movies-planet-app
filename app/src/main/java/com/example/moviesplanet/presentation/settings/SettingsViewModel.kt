package com.example.moviesplanet.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.moviesplanet.domain.GetSettingsUseCase
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val getSettingsUseCase: GetSettingsUseCase) : ViewModel() {
}