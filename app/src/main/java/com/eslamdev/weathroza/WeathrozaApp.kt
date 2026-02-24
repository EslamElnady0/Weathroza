package com.eslamdev.weathroza

import android.app.Application

class WeathrozaApp : Application() {
    var appContainer: AppContainer = AppContainerImpl(this)

}