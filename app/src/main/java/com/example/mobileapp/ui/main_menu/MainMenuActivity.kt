package com.example.mobileapp.ui.main_menu

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mobileapp.R
import com.example.mobileapp.ui.bottom_navigation.BottomNavigationConfig
import com.example.mobileapp.ui.bottom_navigation.BottomNavigationConfigBuilder
import com.example.mobileapp.ui.bottom_navigation.IconSource
import com.example.mobileapp.ui.bottom_navigation.IconSource.Companion.resource
import com.example.mobileapp.databinding.ActivityMainMenuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        // Получаем NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerMain) as? NavHostFragment

        if (navHostFragment == null) {
            // Если нет NavHostFragment, создаем его
            setupNavHostFragment()
        } else {
            navController = navHostFragment.navController
            setupBottomNavigation()
        }
    }

    private fun setupNavHostFragment() {
        // Создаем NavHostFragment
        val navHostFragment = NavHostFragment.create(R.navigation.nav_graph)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerMain, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()

        navController = navHostFragment.navController
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        // Настройка стандартной навигации с BottomNavigationView
        binding.bottomNavigationView.setupWithNavController(navController)

        // Дополнительная кастомная настройка
        binding.bottomNavigationView.setup {
            sections {
                section {
                    title("Main")
                    iconSource(resource(R.drawable.free_icon_font_home_3917032))
                    link("main_fragment")
                }
                section {
                    title("Settings")
                    iconSource(resource(R.drawable.free_icon_font_settings_3917051))
                    link("settings_fragment")
                }
            }
            tint(R.color.black)
            onItemClicked { section ->
                try {
                    when (section.link) {
                        "main_fragment" -> navController.navigate(R.id.main_menuFragment)
                        "settings_fragment" -> navController.navigate(R.id.settingsFragment)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Navigation error: ${e.message}", e)
                }
            }
        }
    }

    fun BottomNavigationView.setup(builder: BottomNavigationConfigBuilder.() -> Unit = {}) {
        val bottomNavigationConfig = BottomNavigationConfigBuilder().apply(builder).build()
        setBottomNavigationSections(bottomNavigationConfig)
        setBottomNavigationTint(bottomNavigationConfig)
    }

    private fun BottomNavigationView.setBottomNavigationSections(bottomNavigationConfig: BottomNavigationConfig) {
        menu.clear()
        bottomNavigationConfig.sectionList.forEachIndexed { index, bottomNavigationSection ->
            menu.add(0, index, index, bottomNavigationSection.title).apply {
                when (val src = bottomNavigationSection.iconSource) {
                    is IconSource.ResourceId -> setIcon(src.drawableResourceId)
                    is IconSource.Url -> bottomNavigationConfig.loader.loadIcon(this, src.url)
                    IconSource.NotDefined -> {}
                }

                setOnMenuItemClickListener {
                    bottomNavigationConfig.onItemClicked(bottomNavigationSection)
                    false
                }
            }
        }
    }

    private fun BottomNavigationView.setBottomNavigationTint(config: BottomNavigationConfig) {
        config.tint?.let {
            itemIconTintList = ContextCompat.getColorStateList(context, it)
            itemTextColor = ContextCompat.getColorStateList(context, it)
        }
    }
}