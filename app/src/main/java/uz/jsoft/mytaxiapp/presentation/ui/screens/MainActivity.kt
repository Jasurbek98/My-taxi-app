package uz.jsoft.mytaxiapp.presentation.ui.screens

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint
import uz.jsoft.mytaxiapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")


    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //make fullscreen, so we can draw behind status bar
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

            //make status bar color transparent
            window.statusBarColor = Color.TRANSPARENT
        }

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}