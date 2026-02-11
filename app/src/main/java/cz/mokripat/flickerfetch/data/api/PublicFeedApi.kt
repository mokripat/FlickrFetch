package cz.mokripat.flickerfetch.data.api

import cz.mokripat.flickerfetch.data.api.dto.PublicFeedDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the Flickr Public Feed API.
 */
internal interface PublicFeedApi {

    /**
     * Fetches the public photos feed from Flickr.
     *
     * @param format Response format, defaults to "json".
     * @param noJsonCallback Disable JSON callback wraper, defaults to 1 (true).
     * @param tags Comma-separated list of tags to filter photos.
     * @return [PublicFeedDto] containing the feed data.
     */
    @GET("services/feeds/photos_public.gne")
    suspend fun getPublicFeed(
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("tags") tags: String? = null,
    ): PublicFeedDto
}