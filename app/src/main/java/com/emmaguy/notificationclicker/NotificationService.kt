package com.emmaguy.notificationclicker

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.emmaguy.notificationclicker.installedapp.ActionStorage

class NotificationService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val actionStorage = ActionStorage(this)

        val actions = actionStorage.actions(sbn.packageName)
        if (actions.isNotEmpty()) {
            actions.flatMap { action ->
                sbn.notification.actions.filter {
                    it.title.contains(
                        action,
                        ignoreCase = true
                    )
                }
            }.forEach { it?.actionIntent?.send() }
        }

        super.onNotificationPosted(sbn)
    }
}