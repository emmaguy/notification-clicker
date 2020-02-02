package com.emmaguy.notificationclicker.installedapp

import com.emmaguy.notificationclicker.core.BaseViewModel

class InstalledAppListViewModel(
    private val appRetriever: InstalledAppsRetriever
) : BaseViewModel<InstalledAppListState>(initialState = InstalledAppListState()) {

    init {
        setState {
            val apps = appRetriever.retrieve().sortedBy { it.name }
            copy(packages = apps.map {
                // TODO: Read what config we already have saved
                AppRow(it, actions = arrayListOf())
            })
        }
    }
}

data class InstalledAppListState(val packages: List<AppRow> = emptyList())
data class AppRow(val app: App, val actions: ArrayList<String>)
