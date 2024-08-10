package com.rivaphy.suitmediatestapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rivaphy.suitmediatestapp.api.ApiService
import com.rivaphy.suitmediatestapp.response.DataItem
import kotlinx.coroutines.launch

class ThirdScreenViewModel : ViewModel() {

    private val _data = MutableLiveData<List<DataItem>>()
    val data: LiveData<List<DataItem>> get() = _data

    private val _isLastPage = MutableLiveData<Boolean>()
    val isLastPage: LiveData<Boolean> get() = _isLastPage

    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> get() = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var currentPage = 1
    private val dataSet = mutableSetOf<DataItem>()
    private val processedPages = mutableSetOf<Int>()

    fun loadData(service: ApiService) {
        if (_isLoading.value == true || processedPages.contains(currentPage)) return

        _isLoading.value = true

        viewModelScope.launch {
            try {
                _isError.value = null
                val response = service.getUsers(currentPage, 10)
                val newItems = response.data?.filterNotNull()?.filter { dataSet.add(it) }

                if (newItems.isNullOrEmpty()) {
                    _isLastPage.value = true
                } else {
                    val currentData = _data.value.orEmpty()
                    _data.value = currentData + newItems
                    processedPages.add(currentPage)
                    currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _isError.value = e.message
            } finally {
                _isLoading.value = false


            }
        }
    }

    fun resetPage() {
        currentPage = 1
        _isLastPage.value = false
        dataSet.clear()
        processedPages.clear()
        _data.value = emptyList()
        _isLoading.value = false
    }
}

