package cz.mokripat.flickerfetch.di

import cz.mokripat.flickerfetch.data.repository.FeedRepositoryImpl
import cz.mokripat.flickerfetch.domain.repository.FeedRepository
import org.koin.dsl.module

val feedModule = module {
    single<FeedRepository> { FeedRepositoryImpl(get()) }
}