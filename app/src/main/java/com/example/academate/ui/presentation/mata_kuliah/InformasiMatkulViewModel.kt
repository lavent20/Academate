package com.example.academate.ui.presentation.mata_kuliah

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academate.data.repository.MataKuliahRepository
import com.example.academate.model.MataKuliah
import com.example.academate.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class InformasiMatkulViewModel @Inject constructor(
    private val mataKuliahRepository: MataKuliahRepository
): ViewModel() {

    private val _matkul = mutableStateOf(MataKuliah())
    val matkul: State<MataKuliah> = _matkul

    private val _isLoading= mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isShowDialog= mutableStateOf(false)
    val isShowDialog: State<Boolean> = _isShowDialog

    fun getMatkulDetail(id: String) =
        viewModelScope.launch {
            mataKuliahRepository.getMatkulById(id).collect {
                when(it) {
                    is Resource.Error -> setLoading(true)
                    is Resource.Loading -> setLoading(true)
                    is Resource.Success -> {
                        _matkul.value= it.data!!.item!!
                        setLoading(false)
                    }
                }
            }
        }

    fun setLoading(state: Boolean) {
        _isLoading.value= state
    }

    fun setDialog(state: Boolean) {
        _isShowDialog.value= state
    }
}