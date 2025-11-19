package com.example.mobileapp.ui.bottom_navigation

import android.view.MenuItem

interface MenuIconLoader {
    fun loadIcon(menuItem: MenuItem, url: String)
}