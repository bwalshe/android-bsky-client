package com.bwalshe.describedsky.data

import android.util.Log
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineRequest
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.auth.AuthProvider
import work.socialhub.kbsky.auth.BearerTokenAuthProvider
import work.socialhub.kbsky.model.app.bsky.embed.EmbedExternal
import work.socialhub.kbsky.model.app.bsky.embed.EmbedImages
import work.socialhub.kbsky.model.app.bsky.embed.EmbedUnion
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsFeedViewPost


const val TAG = "BlueSkyRepository"

interface BlueSkyRepository {
    fun createSession(identity: String, password: String)
    fun getTimeline(): List<BlueSkyPost>
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

    override fun getTimeline(): List<BlueSkyPost> {
        if (sessionActive)
            return bskyInstance.feed()
                .getTimeline(FeedGetTimelineRequest(auth!!)).data.feed.map { convertPost(it) }
        throw IllegalStateException("Cannot get timeline there is no session")
    }

    private fun convertPost(rawPost: FeedDefsFeedViewPost): BlueSkyPost {
        val rawAuthor = rawPost.post.author!!
        val postContent = rawPost.post.record?.asFeedPost!!

        val author = AuthorInfo(
            did = rawAuthor.did,
            handle = rawAuthor.handle,
            name = rawAuthor.displayName,
            avatarLink = rawAuthor.avatar
        )

        return BlueSkyPost(
            author = author,
            text = postContent.text,
            embedding = convertEmbedding(author.did, postContent.embed)
        )
    }

    private fun convertEmbedding(authorDid: String, rawEmbedding: EmbedUnion?): BlueSkyEmbedding? {
        if (rawEmbedding == null) {
            return null
        }
        Log.d(TAG, "Converting an embedding of type ${rawEmbedding.type}")

        when (rawEmbedding.type) {
            EmbedExternal.TYPE -> {
                val rawExternal = rawEmbedding.asExternal?.external!!
                val thumbnailBlobRef = rawExternal.thumb?.ref?.link
                val thumbnailLink = if (thumbnailBlobRef != null) {
                    resolveBlob(authorDid, rawExternal.thumb?.ref?.link!!)
                } else {
                    null
                }
                Log.d(TAG, "Blob ref $thumbnailBlobRef resolved to $thumbnailLink")

                return BlueSkyEmbedding.External(
                    link = rawExternal.uri,
                    thumbnailLink = thumbnailLink,
                    description = rawExternal.description
                )
            }

            EmbedImages.TYPE -> {
                val rawImages = rawEmbedding.asImages?.images ?: listOf()
                val images = rawImages.map { image ->
                    BlueSkyImage(
                        link = resolveBlob(authorDid, image.image?.ref?.link!!),
                        altText = image.alt
                    )
                }
                return BlueSkyEmbedding.Images(images)
            }

            else -> {
                return BlueSkyEmbedding.UnknownType(rawEmbedding.type)
            }
        }
    }

    private fun resolveBlob(owner: String, blobLink: String): String {
        return "$baseUri/xrpc/com.atproto.sync.getBlob?did=$owner&cid=$blobLink"
    }
}