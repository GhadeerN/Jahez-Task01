package sa.edu.tuwaiq.jaheztask01.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import sa.edu.tuwaiq.jaheztask01.R
import sa.edu.tuwaiq.jaheztask01.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // For the action bar
    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

//        setupActionBarWithNavController(navController)

        // to link the nav bottom bar with nav host
        val bottomNav = binding.bottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController)

        // Hide/show bottom nav bar on the fragments
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> bottomNav.visibility = View.GONE
                R.id.signUpFragment -> bottomNav.visibility = View.GONE
                else -> bottomNav.visibility = View.VISIBLE
            }
        }
        Log.d(TAG, "inside main: progress bar ----- ${binding.mainProgressBar.isVisible}")
    }

    // Show/Hide progressbar
    fun showProgressBar(state: Boolean) {
        Log.d(TAG, "progress bar ----- $state")
        if (state)
            binding.mainProgressBar.visibility = View.VISIBLE
        else
            binding.mainProgressBar.visibility = View.GONE
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}