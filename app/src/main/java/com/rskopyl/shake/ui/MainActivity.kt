package com.rskopyl.shake.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.rskopyl.shake.ui.screen.ShakeApp
import com.rskopyl.shake.ui.screen.details.DetailsViewModel
import com.rskopyl.shake.ui.theme.ShakeTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShakeTheme {
                ShakeApp()
            }
        }
    }

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryEntryPoint {

        val detailsDaggerFactory: DetailsViewModel.DaggerFactory
    }
}