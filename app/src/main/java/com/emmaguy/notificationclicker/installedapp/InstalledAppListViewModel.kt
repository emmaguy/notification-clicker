package com.emmaguy.notificationclicker.installedapp

import android.graphics.drawable.Drawable
import com.emmaguy.notificationclicker.core.BaseViewModel

class InstalledAppListViewModel(
    private val appRetriever: InstalledAppsRetriever
) : BaseViewModel<InstalledAppListState>(initialState = InstalledAppListState()) {

    init {
        setState { copy(packages = appRetriever.retrieve()) }
    }
}

data class InstalledAppListState(val packages: List<App> = emptyList())
data class App(val name: String, val icon: Drawable, val packageName: String)
