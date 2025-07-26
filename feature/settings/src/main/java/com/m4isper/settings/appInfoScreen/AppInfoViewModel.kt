package com.m4isper.settings.appInfoScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue

data class AppInfo(
    val versionName: String,
    val lastUpdateDate: String
)

class AppInfoViewModel(application: Application) : AndroidViewModel(application) {

    val appInfo: AppInfo by lazy {
        val context = getApplication<Application>()
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionName = packageInfo.versionName ?: "N/A"

        val lastUpdate = Date(packageInfo.lastUpdateTime)
        val formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(lastUpdate)

        AppInfo(versionName, formattedDate)
    }
}