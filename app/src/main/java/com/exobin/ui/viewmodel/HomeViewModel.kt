package com.exobin.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exobin.data.repository.HomeRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _garbagePercentage = MutableLiveData<Int>()
    val garbagePercentage: LiveData<Int> = _garbagePercentage

    private val _trashCount = MutableLiveData<Int>()
    val trashCount: LiveData<Int> = _trashCount

    init {
        homeRepository.observeChanges { percentage ->
            Log.d("HomeViewModel", "Received new garbage percentage value from repository: $percentage")
            _garbagePercentage.postValue(percentage)
        }
        homeRepository.observeTrashCount { count ->
            Log.d("HomeViewModel", "Received new trash count value from repository: $count")
            _trashCount.postValue(count)
        }
    }
}

