package com.emmaguy.notificationclicker

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName.contains("netflix")) {
            val skipIntroAction = sbn.notification.actions
                .find { it.title.contains("Skip Intro", ignoreCase = true) }

            skipIntroAction?.actionIntent?.send()
        }

        super.onNotificationPosted(sbn)
    }
}