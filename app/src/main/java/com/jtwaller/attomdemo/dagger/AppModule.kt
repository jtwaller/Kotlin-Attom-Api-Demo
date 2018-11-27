package com.jtwaller.attomdemo.dagger

import android.app.Application
import android.content.Context
import com.jtwaller.attomdemo.AttomApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: AttomApp) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideApplication(): Application = app

}