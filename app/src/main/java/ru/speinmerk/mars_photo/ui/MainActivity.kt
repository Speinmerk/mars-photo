package ru.speinmerk.mars_photo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.speinmerk.mars_photo.R
import ru.speinmerk.mars_photo.dataSourceModule
import ru.speinmerk.mars_photo.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}
