package com.m4isper.common

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
}