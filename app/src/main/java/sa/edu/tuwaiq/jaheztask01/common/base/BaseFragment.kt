package sa.edu.tuwaiq.jaheztask01.common.base

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.presentation.MainActivity

private const val TAG = "BaseFragment"

open class BaseFragment : Fragment() {
    lateinit var _viewModel: BaseViewModel

    /* This function is to fix the navController not found error. This issue is caused
    *  when an event that should call the navigation action fires multiple times in quick succession.
    *  Reference: https://nezspencer.medium.com/navigation-components-a-fix-for-navigation-action-cannot-be-found-in-the-current-destination-95b63e16152e
    * */
    protected fun safeNavigate(
        @IdRes currentDestinationId: Int,
        @IdRes id: Int,
        args: Bundle? = null
    ) {
        val navController = (requireActivity() as MainActivity).navController
        if (currentDestinationId == navController.currentDestination?.id) {
            navController.navigate(id, args)
        }
    }

    // We created it to handle the coroutine and the state flow for whatever we pass
    fun <T> collectLatestLifecycleFlow(
        viewLifecycleOwner: LifecycleOwner,
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }

    // Base UI States
    protected fun setUIState() {
        Log.d(TAG, "----------- inside ui state ----------")
        lifecycleScope.launch {
            _viewModel.baseUIState.collect { state ->
                Log.d(TAG, "Base state: $state")
                when {
                    state.isLoading -> {
                        showProgressBar(true)
                    }
                    state.error.isNotBlank() -> {
                        showProgressBar(false)
                        Toast.makeText(requireActivity(), state.error, Toast.LENGTH_LONG).show()
                    }
                    else -> showProgressBar(false)
                }
            }
        }
    }

    private fun showProgressBar(state: Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.showProgressBar(state)
    }

//    fun <T> collectLifecycleFlow(
//        viewLifecycleOwner: LifecycleOwner,
//        flow: Flow<T>,
//        collect: suspend (T) -> Unit
//    ) {
//        lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                flow.collect(collect)
//            }
//        }
//    }
}