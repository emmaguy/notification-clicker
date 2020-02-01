package com.emmaguy.notificationclicker.installedapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.compose.Context

class InstalledAppsRetriever(private val context: Context) {

    fun retrieve(): List<App> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        return context.packageManager.queryIntentActivities(mainIntent, 0)
            .map {
                App(
                    name = it.loadLabel(context.packageManager).toString(),
                    icon = it.loadIcon(context.packageManager),
                    packageName = it.activityInfo.packageName
                )
            }
    }
}

data class App(
    val name: String,
    val icon: Drawable,
    val packageName: String
)