package cz.mokripat.flickerfetch.di

import cz.mokripat.flickerfetch.data.repository.FeedRepositoryImpl
import cz.mokripat.flickerfetch.domain.repository.FeedRepository
import cz.mokripat.flickerfetch.domain.usecase.GetFeedUseCase
import cz.mokripat.flickerfetch.domain.usecase.GetFeedUseCaseImpl
import cz.mokripat.flickerfetch.ui.feed.FeedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val feedModule = module {
    single<FeedRepository> { FeedRepositoryImpl(get()) }

    factory<GetFeedUseCase> { GetFeedUseCaseImpl(get()) }

    viewModel { FeedViewModel(get()) }
}