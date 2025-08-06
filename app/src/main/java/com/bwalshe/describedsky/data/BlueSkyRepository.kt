package com.bwalshe.describedsky.data

import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineRequest
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.auth.AuthProvider
import work.socialhub.kbsky.auth.BearerTokenAuthProvider
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsFeedViewPost
import work.socialhub.kbsky.model.share.Blob


interface BlueSkyRepository {
    fun createSession(identity: String, password: String)
    fun getTimeline(): List<FeedDefsFeedViewPost>
    fun resolveBlob(owner:String, blob: Blob): String
    val sessionActive: Boolean

}

class NetworkBlueSkyRepository(private val baseUri: String) : BlueSkyRepository {

    private val bskyInstance = BlueskyFactory
        .instance(baseUri)

    private var auth: AuthProvider? = null

    override val sessionActive: Boolean
        get() = auth != null

    override fun createSession(identity: String, password: String) {
        val response = bskyInstance.server()
            .createSession(
                ServerCreateSessionRequest().also {
                    it.identifier = identity
                    it.password = password
                }
            )
        auth = BearerTokenAuthProvider(response.data.accessJwt, response.data.refreshJwt)
    }

    override fun getTimeline(): List<FeedDefsFeedViewPost> {
        if (sessionActive)
            return bskyInstance.feed().getTimeline(FeedGetTimelineRequest(auth!!)).data.feed
        throw IllegalStateException("Cannot get timeline there is no session")
    }

    override fun resolveBlob(owner: String, blob: Blob): String {
       return "$baseUri/xrpc/com.atproto.sync.getBlob?did=$owner&cid=${blob.ref?.link}"
    }
}