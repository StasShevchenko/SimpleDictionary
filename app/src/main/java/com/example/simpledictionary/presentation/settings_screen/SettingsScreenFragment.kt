package com.example.simpledictionary.presentation.settings_screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.simpledictionary.R
import com.example.simpledictionary.data.local.preferences.AppTheme
import com.example.simpledictionary.data.local.preferences.PreferencesManager
import com.example.simpledictionary.databinding.SettingsScreenFragmentBinding
import com.example.simpledictionary.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsScreenFragment : Fragment(R.layout.settings_screen_fragment) {

    @Inject
    lateinit var dataStore: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding: SettingsScreenFragmentBinding = SettingsScreenFragmentBinding.bind(view)
        binding.apply {
            if (resources.configuration.locales.get(0).language == "en") {
                englishCheckImage.visibility = View.VISIBLE
            } else {
                russianCheckImage.visibility = View.VISIBLE
            }

            lifecycleScope.launchWhenCreated {
                dataStore.preferencesFlow.collectLatest { preferences ->
                    when (preferences.theme) {
                        AppTheme.VIOLET -> {
                            violetCheckImage.visibility = View.VISIBLE
                        }
                        AppTheme.BLUE -> {
                            blueThemeCheckImage.visibility = View.VISIBLE
                        }
                        AppTheme.GREEN -> {
                            greenThemeCheckImage.visibility = View.VISIBLE
                        }
                    }
                }
            }
            englishTextView.setOnClickListener {
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags("en")
                )

            }
            russianTextView.setOnClickListener {
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags("ru")
                )
            }

            greenThemeTextView.setOnClickListener {
                violetCheckImage.visibility = View.INVISIBLE
                blueThemeCheckImage.visibility = View.INVISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    dataStore.updateAppTheme(AppTheme.GREEN)
                    (requireActivity() as MainActivity).recreate()
                }
            }
            violetThemeTextView.setOnClickListener {
                greenThemeCheckImage.visibility = View.INVISIBLE
                blueThemeCheckImage.visibility = View.INVISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    dataStore.updateAppTheme(AppTheme.VIOLET)
                    (requireActivity() as MainActivity).recreate()
                }
            }
            blueThemeTextView.setOnClickListener {
                greenThemeCheckImage.visibility = View.INVISIBLE
                violetCheckImage.visibility = View.INVISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    dataStore.updateAppTheme(AppTheme.BLUE)
                    (requireActivity() as MainActivity).recreate()
                }
            }
        }
    }
}