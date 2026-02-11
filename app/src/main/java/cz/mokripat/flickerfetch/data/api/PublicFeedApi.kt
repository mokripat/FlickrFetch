package cz.mokripat.flickerfetch.data.api

import cz.mokripat.flickerfetch.data.api.dto.PublicFeedDto
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PublicFeedApi {

    @GET("services/feeds/photos_public.gne")
    suspend fun getPublicFeed(
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("tags") tags: String? = null,
    ): PublicFeedDto
}