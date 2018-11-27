package com.jtwaller.attomdemo.dagger

import com.jtwaller.attomdemo.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface NetworkComponent {
    fun inject(mainActivity: MainActivity)
}