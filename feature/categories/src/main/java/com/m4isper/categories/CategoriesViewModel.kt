package com.m4isper.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.usecase.GetCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import com.m4isper.common.Result

class CategoriesViewModel (
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val searchQuery = MutableStateFlow("")

    val filteredCategories = combine(categories, searchQuery) { list, query ->
        if (query.isBlank()) list
        else list.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadCategories() {
        viewModelScope.launch {
            when (val result = getCategoriesUseCase()) {
                is Result.Success -> _categories.value = result.data
                is Result.Failure -> _error.value = result.exception.message
            }
        }
    }
}