package com.bwalshe.describedsky.data


data class AuthorInfo (
    val did: String,
    val handle: String,
    val name: String?,
    val avatarLink: String?
)

interface BlueSkyEmbedding {
    data class External (
        val link: String?,
        val description: String?,
        val thumbnailLink: String?
    ) : BlueSkyEmbedding

    data class Images (
        val images: List<BlueSkyImage>,
    ) : BlueSkyEmbedding

    data class UnknownType (
        val type: String
    ) : BlueSkyEmbedding
}

data class BlueSkyImage (
    val link: String,
    val altText: String?
)

data class BlueSkyPost (
    val author: AuthorInfo,
    val text: String? = null,
    val embedding: BlueSkyEmbedding? = null
)
