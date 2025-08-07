package com.bwalshe.describedsky.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bwalshe.describedsky.DescribedSkyApplication
import com.bwalshe.describedsky.data.BlueSkyRepository
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsFeedViewPost

class DescribedSkyViewModel(
    private val blueSkyRepository: BlueSkyRepository,
) : ViewModel() {

    var timeline: List<FeedDefsFeedViewPost> by mutableStateOf(listOf())

    fun refreshTimeline() {
        timeline = blueSkyRepository.getTimeline()
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DescribedSkyApplication)
                DescribedSkyViewModel(application.container.blueSkyRepository)
            }
        }
    }
}