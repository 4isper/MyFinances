package com.m4isper.myfinances.ui.screens.categoriesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.myfinances.domain.usecase.GetCategoriesUseCase
import javax.inject.Inject

class CategoriesViewModelFactory @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)){
            return CategoriesViewModel(getCategoriesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
