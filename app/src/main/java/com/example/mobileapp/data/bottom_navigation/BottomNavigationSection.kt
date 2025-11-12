package com.example.mobileapp.data.bottom_navigation

data class BottomNavigationSection(
    val title: String,
    val iconSource: IconSource = IconSource.NotDefined,
    val link: String
)