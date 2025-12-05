package com.example.academate.ui.presentation.login_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _mentorname = MutableStateFlow("")
    val mentorname: StateFlow<String> = _mentorname.asStateFlow()

    private val _mentorDetails = MutableStateFlow<Map<String, Any>>(emptyMap())
    val mentorDetails: StateFlow<Map<String, Any>> = _mentorDetails.asStateFlow()

    // Tambahan untuk menyimpan lokasi pengguna
    private val _location = MutableStateFlow("Lokasi Belum Diatur")
    val location: StateFlow<String> = _location.asStateFlow()

    // Profile image URI (tersimpan selama app lifecycle)
    private val _profileImageUri = MutableStateFlow<String?>(null)
    val profileImageUri: StateFlow<String?> = _profileImageUri.asStateFlow()

    fun setUsername(value: String) {
        _username.value = value
    }

    fun setMentorname(value: String) {
        _mentorname.value = value
    }

    fun addMentorDetail(key: String, value: Any) {
        val currentDetails = _mentorDetails.value.toMutableMap()
        currentDetails[key] = value
        _mentorDetails.value = currentDetails
    }

    // Fungsi untuk mengatur lokasi
    fun setLocation(value: String) {
        _location.value = value
    }

    // Fungsi untuk set profile image
    fun setProfileImageUri(uri: String?) {
        _profileImageUri.value = uri
    }
}