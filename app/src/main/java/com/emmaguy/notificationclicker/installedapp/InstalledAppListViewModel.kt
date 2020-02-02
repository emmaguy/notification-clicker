package com.emmaguy.notificationclicker.installedapp

import com.emmaguy.notificationclicker.core.BaseViewModel

class InstalledAppListViewModel(
    private val appRetriever: InstalledAppsRetriever,
    private val actionStorage: ActionStorage
) : BaseViewModel<InstalledAppListState>(initialState = InstalledAppListState()) {

    init {
        setState {
            val apps = appRetriever.retrieve().sortedBy { it.name }
            copy(packages = apps.map { app ->
                AppRow(
                    app = app,
                    actions = ArrayList(actionStorage.actions(app.packageName)),
                    onActionsChanged = { actions ->
                        actionStorage.saveAction(app.packageName, actions)
                    })
            })
        }
    }
}

data class InstalledAppListState(val packages: List<AppRow> = emptyList())
data class AppRow(
    val app: App,
    val actions: ArrayList<String>,
    val onActionsChanged: ((List<String>) -> Unit)? = null
)
