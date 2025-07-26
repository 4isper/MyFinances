package com.m4isper.di

import com.m4isper.transaction.TransactionViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@TransactionScope
@Subcomponent
interface TransactionComponent {
    fun transactionViewModelFactory(): TransactionViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @TransactionIdQualifier transactionId: Int): TransactionComponent
    }
}