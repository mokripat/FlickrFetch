package cz.mokripat.flickerfetch.domain.repository

import cz.mokripat.flickerfetch.domain.model.PublicFeed

interface FeedRepository {

    suspend fun getPublicFeed(): Result<PublicFeed>
}
