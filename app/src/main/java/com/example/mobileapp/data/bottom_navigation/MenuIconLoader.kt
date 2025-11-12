package com.example.mobileapp.data.bottom_navigation

import android.view.MenuItem

interface MenuIconLoader {
    fun loadIcon(menuItem: MenuItem, url: String)
}