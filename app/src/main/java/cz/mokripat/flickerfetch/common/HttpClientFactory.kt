package cz.mokripat.flickerfetch.common

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Factory object for creating HTTP client components.
 *
 * Encapsulates all networking configuration logic, separating it from DI concerns.
 * Configuration values are sourced from [ApiConstants].
 */
object HttpClientFactory {

    /**
     * Creates a configured JSON serializer for API responses.
     * - Ignores unknown keys for forward compatibility
     * - Lenient parsing for flexible JSON handling
     * - Coerces input values to handle type mismatches
     */
    fun createJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
    }

    /**
     * Creates an HTTP logging interceptor for debugging network traffic.
     *
     * @param level The logging level (default: BODY for full request/response logging)
     */
    fun createLoggingInterceptor(level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            this.level = level
        }
    }

    /**
     * Creates an OkHttpClient with configured timeouts and interceptors.
     * Timeout values are sourced from [ApiConstants].
     *
     * @param loggingInterceptor Interceptor for logging HTTP traffic
     */
    fun createOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Creates a Retrofit instance configured for the Flickr API.
     *
     * @param baseUrl The API base URL (default from [ApiConstants.BASE_URL])
     * @param okHttpClient Configured HTTP client
     * @param json JSON serializer for request/response conversion
     */
    fun createRetrofit(
        baseUrl: String = ApiConstants.BASE_URL,
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    /**
     * Creates a Retrofit API service instance.
     *
     * @param T The API interface type
     * @param retrofit Configured Retrofit instance
     * @return Implementation of the API interface
     */
    inline fun <reified T> createApiService(retrofit: Retrofit): T {
        return retrofit.create(T::class.java)
    }
}
