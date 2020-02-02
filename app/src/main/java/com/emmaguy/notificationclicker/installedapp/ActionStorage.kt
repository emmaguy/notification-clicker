package com.emmaguy.notificationclicker.installedapp

import android.content.Context

class ActionStorage(private val context: Context) {
    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            "action_storage",
            Context.MODE_PRIVATE
        )
    }

    fun saveAction(packageName: String, actions: List<String>) {
        sharedPreferences.edit().putStringSet(packageName, actions.toSet()).apply()
    }

    fun actions(packageName: String): Set<String> {
        return sharedPreferences.getStringSet(packageName, emptySet())!!
    }
}