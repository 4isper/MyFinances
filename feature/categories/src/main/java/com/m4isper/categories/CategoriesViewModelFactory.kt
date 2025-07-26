package com.m4isper.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.domain.usecase.GetCategoriesUseCase
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
