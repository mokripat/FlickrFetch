package cz.mokripat.flickerfetch.di

import cz.mokripat.flickerfetch.common.HttpClientFactory
import cz.mokripat.flickerfetch.data.api.PublicFeedApi
import org.koin.dsl.module

/**
 * DI module for network components using [HttpClientFactory].
 */
val networkModule = module {

    single { HttpClientFactory.createJson() }

    single { HttpClientFactory.createLoggingInterceptor() }

    single { HttpClientFactory.createOkHttpClient(get()) }

    single { HttpClientFactory.createRetrofit(okHttpClient = get(), json = get()) }

    single { HttpClientFactory.createApiService<PublicFeedApi>(get()) }
}
