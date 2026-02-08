package cz.mokripat.flickerfetch.di

import org.koin.dsl.module

/**
 * Main DI module that aggregates all application modules.
 */
val appModule = module {
    includes(networkModule, feedModule)
}