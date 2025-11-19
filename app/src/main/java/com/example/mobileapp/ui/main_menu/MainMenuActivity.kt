package com.example.mobileapp.ui.main_menu

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.mobileapp.R
import com.example.mobileapp.ui.bottom_navigation.BottomNavigationConfig
import com.example.mobileapp.ui.bottom_navigation.BottomNavigationConfigBuilder
import com.example.mobileapp.ui.bottom_navigation.IconSource
import com.example.mobileapp.ui.bottom_navigation.IconSource.Companion.resource
import com.example.mobileapp.databinding.ActivityMainMenuBinding
import com.example.mobileapp.ui.sign_in.SignInViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private val viewModel: SignInViewModel by viewModel()

    private val INTENT_USER_EMAIL = "UserEmail"

    private lateinit var navController: NavController

    @SuppressLint("ServiceCast", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerMain) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setup {
            sections {
                section {
                    title("Main")
                    iconSource(resource(R.drawable.free_icon_font_home_3917032))
                    link("main")
                }
                section {
                    title("Settings")
                    iconSource(resource(R.drawable.free_icon_font_settings_3917051))
                    link("settings")
                }
            }
            tint(R.color.black)
            //remoteLoader(GlideMenuIconLoader(context = context.applicationContext))
            onItemClicked { section ->
                navController.navigate(route = section.link)
                Log.d(TAG, "section clicked: $section")
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