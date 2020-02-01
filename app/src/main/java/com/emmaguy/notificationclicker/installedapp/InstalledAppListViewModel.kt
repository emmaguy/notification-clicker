package com.emmaguy.notificationclicker.installedapp

import com.emmaguy.notificationclicker.core.BaseViewModel

class InstalledAppListViewModel(
    private val appRetriever: InstalledAppsRetriever
) : BaseViewModel<InstalledAppListState>(initialState = InstalledAppListState()) {

    init {
        setState {
            val apps = appRetriever.retrieve()
            copy(packages = apps.map {
                AppRow(it, onClick = {
                    // TODO: Launch UI where you can enter what keywords to click
                })
            })
        }
    }
}

data class InstalledAppListState(val packages: List<AppRow> = emptyList())
data class AppRow(val app: App, val onClick: (() -> Unit)? = null)
