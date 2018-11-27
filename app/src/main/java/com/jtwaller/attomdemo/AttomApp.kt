package com.jtwaller.attomdemo

import android.app.Application
import com.jtwaller.attomdemo.dagger.AppModule
import com.jtwaller.attomdemo.dagger.DaggerNetworkComponent
import com.jtwaller.attomdemo.dagger.NetworkComponent

class AttomApp: Application() {

    companion object {
        lateinit var networkComponent: NetworkComponent
    }

    override fun onCreate() {
        super.onCreate()
        networkComponent = DaggerNetworkComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

}