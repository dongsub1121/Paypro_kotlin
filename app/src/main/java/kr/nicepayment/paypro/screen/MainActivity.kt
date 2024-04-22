package kr.nicepayment.paypro.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kr.nicepayment.paypro.R
import kr.nicepayment.paypro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_master)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_sales_report, R.id.navigation_pay, R.id.navigation_sales_record
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // 'Settings' 메뉴 아이템 클릭 시 실행할 코드
                gotoSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun gotoSettings() {
        if (navController.currentDestination?.id == R.id.navigation_pay) {
            navController.navigate(R.id.action_navigation_pay_to_merchantFragment)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        val navController = findNavController(R.id.nav_host_fragment_activity_master)
        if (navController.currentDestination?.id == R.id.navigation_pay) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finish()
            } else {

                val rootView = window.decorView.findViewById<View>(android.R.id.content)
                Snackbar.make(rootView, "한 번 더 누르면 앱이 종료됩니다.", Snackbar.LENGTH_SHORT)
                    .setAction("종료하기"){
                        finish()
                    }.show()
                backPressedTime = System.currentTimeMillis()
            }
        } else {
            super.onBackPressed()
        }
    }
}