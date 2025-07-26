package com.m4isper.settings

sealed class SettingItem(val title: String) {
    data class Static(val label: String) : SettingItem(label)
    data class Navigable(val label: String, val route: String) : SettingItem(label)
}