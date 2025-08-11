package com.bwalshe.describedsky.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bwalshe.describedsky.DescribedSkyApplication
import com.bwalshe.describedsky.data.BlueSkyPost
import com.bwalshe.describedsky.data.BlueSkyRepository
import kotlinx.coroutines.launch


sealed interface TimelineState {
    data object Loading : TimelineState
    data class Ready(var timelinePosts: List<BlueSkyPost>) : TimelineState
}
class TimelineViewModel(
    private val blueSkyRepository: BlueSkyRepository,
) : ViewModel() {

    var timeline: TimelineState by mutableStateOf(TimelineState.Loading)

    fun refreshTimeline() {
        timeline = TimelineState.Loading
        viewModelScope.launch {
            timeline = TimelineState.Ready(blueSkyRepository.getTimeline())
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DescribedSkyApplication)
                TimelineViewModel(application.container.blueSkyRepository)
            }
        }
    }
}