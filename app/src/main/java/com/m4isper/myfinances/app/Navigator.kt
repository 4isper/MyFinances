package com.m4isper.myfinances.app

import androidx.navigation3.runtime.NavKey

/**
 * Обрабатывает навигационные события (вперёд/назад), меняя [NavigationState].
 * Табы нижней навигации — переключение стека; остальные ключи — push в текущий стек.
 */
class Navigator(
    val state: NavigationState,
) {
    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            // верхнеуровневый таб — переключаем стек
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack =
            state.backStacks[state.topLevelRoute]
                ?: error("Стек для ${state.topLevelRoute} не найден")
        val currentRoute = currentStack.last()

        // если мы в корне текущего таба — возвращаемся на стек стартового таба
        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}
