package com.bwalshe.describedsky.data

import  work.socialhub.kbsky.domain.Service as BskyService


interface AppContainer {
    //val context: Context
    val blueSkyRepository: BlueSkyRepository
    //val modelDownloadRepository: ModelDownloadRepository
}

class DefaultContainer: AppContainer {
    override val blueSkyRepository = NetworkBlueSkyRepository(BskyService.BSKY_SOCIAL.uri)
}