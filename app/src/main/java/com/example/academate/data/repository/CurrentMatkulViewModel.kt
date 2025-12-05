package com.example.academate.data.repository

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrentMatkulViewModel : ViewModel() {

    // membuat current matkul yang dipilih
    private val _currentMatkul = MutableStateFlow(1)
    val currentMatkul: StateFlow<Int> = _currentMatkul.asStateFlow()

    private val _currentNamaMatkul = MutableStateFlow("")
    val currentNamaMatkul: StateFlow<String> = _currentNamaMatkul.asStateFlow()

    fun setCurrentMatkul(value: Int) {
        _currentMatkul.value = value
    }

    fun setCurrentNamaMatkul(value: String) {
        _currentNamaMatkul.value = value
    }

}