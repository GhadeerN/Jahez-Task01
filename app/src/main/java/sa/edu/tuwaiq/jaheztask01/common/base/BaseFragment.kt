package sa.edu.tuwaiq.jaheztask01.common.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController

open class BaseFragment : Fragment() {
    /* This extension function is to fix the navController not found error. This issue is caused
    *  when an event that should call the navigation action fires multiple times in quick succession.
    *  Reference: https://nezspencer.medium.com/navigation-components-a-fix-for-navigation-action-cannot-be-found-in-the-current-destination-95b63e16152e
    * */
    fun NavController.safeNavigate(
        @IdRes currentDestinationId: Int,
        @IdRes id: Int,
        args: Bundle? = null
    ) {
        if (currentDestinationId == currentDestination?.id) {
            navigate(id, args)
        }
    }
}