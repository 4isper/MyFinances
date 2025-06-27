package com.m4isper.myfinances.domain

import com.m4isper.myfinances.domain.model.AccountModel
import com.m4isper.myfinances.domain.model.CategoryModel
import com.m4isper.myfinances.domain.model.TransactionModel

val accountDemo = AccountModel(
    id = 1,
    name = "Основной счёт",
    balance = "-670 000",
    currency = "₽"
)

val categoriesDemo = listOf(
    CategoryModel( 1, "Аренда квартиры", "\uD83C\uDFE1" , false),
    CategoryModel( 2, "Одежда", "\uD83D\uDC57", false),
    CategoryModel( 3, "На собачку", "\uD83D\uDC36", false),
    CategoryModel( 4, "На собачку", "\uD83D\uDC36", false),
    CategoryModel( 5, "Ремонт квартиры", "РК", false),
    CategoryModel( 6, "Продукты", "\uD83C\uDF6D", false),
    CategoryModel( 7, "Спортзал", "\uD83C\uDFCB\uFE0F", false),
    CategoryModel( 8, "Медицина", "\uD83D\uDC8A", false),
)

//val expensesDemo = listOf(
//    TransactionModel(1, accountDemo, categoriesDemo[0], "100 000", "", null, "", ""),
//    TransactionModel(2, accountDemo, categoriesDemo[1], "100 000", "", null, "", ""),
//    TransactionModel(3, accountDemo, categoriesDemo[2], "100 000", "", "Джек", "", ""),
//    TransactionModel(4, accountDemo, categoriesDemo[3], "100 000", "", "Энни", "", ""),
//    TransactionModel(5, accountDemo, categoriesDemo[4], "100 000", "", null, "", ""),
//    TransactionModel(6, accountDemo, categoriesDemo[5], "100 000", "", null, "", ""),
//    TransactionModel(7, accountDemo, categoriesDemo[6], "100 000", "", null, "", ""),
//    TransactionModel(8, accountDemo, categoriesDemo[7], "100 000", "", null, "", ""),
//)
//
//val incomeDemo = listOf(
//    TransactionModel(10, accountDemo, CategoryModel(10, "Зарплата", "", true), "500 000", "", null, "", ""),
//    TransactionModel(11, accountDemo, CategoryModel(11, "Подработка", "", true), "100 000", "", null, "", ""),
//)