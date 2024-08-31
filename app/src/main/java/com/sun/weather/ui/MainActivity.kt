package com.sun.weather.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sun.weather.R
import com.sun.weather.base.BaseActivity
import com.sun.weather.databinding.ActivityMainBinding
import com.sun.weather.ui.home.HomeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override val viewModel: MainViewModel by viewModel()
    override val sharedViewModel: SharedViewModel by viewModel()

    override fun initialize() {
        viewModel.locationData.observe(
            this,
            Observer { location ->
                val (latitude, longitude) = location
                // Chuyển dữ liệu tọa độ sang HomeFragment
                val homeFragment = HomeFragment.newInstance(latitude, longitude)
                setNextFragment(homeFragment)
            },
        )

        // Gọi hàm yêu cầu vị trí người dùng
        viewModel.requestLocationAndFetchWeather(this)
    }

    private fun setNextFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(fragment::javaClass.name)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
