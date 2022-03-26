package sa.edu.tuwaiq.jaheztask01.common.util

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class BottomNavBarUtil(private val bottomNav: BottomNavigationView) {

    fun hidNavBar() {
        bottomNav.visibility = View.GONE
    }

    fun showNavBarWithAnimation() {
        bottomNav.animate().alpha(1.0f).setDuration(100).withEndAction {
            bottomNav.visibility = View.VISIBLE
        }
    }

    companion object {
        private var INSTANCE: BottomNavBarUtil? = null
        fun init(bottomNav: BottomNavigationView) {
            if (INSTANCE == null) {
                INSTANCE = BottomNavBarUtil(bottomNav)
            }
        }

        fun get(): BottomNavBarUtil {
            return INSTANCE ?: throw Exception("BottomNavBarUtil instance is null")
        }
    }
}