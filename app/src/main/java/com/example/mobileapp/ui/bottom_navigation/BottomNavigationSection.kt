package com.example.mobileapp.ui.bottom_navigation

data class BottomNavigationSection(
    val title: String,
    val iconSource: IconSource = IconSource.NotDefined,
    val link: String
)