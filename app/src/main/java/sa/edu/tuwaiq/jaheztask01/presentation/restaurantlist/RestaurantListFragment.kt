package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.databinding.RestaurantListFragmentBinding

private const val TAG = "RestaurantListFragment"

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {


    private val viewModel: RestaurantListViewModel by activityViewModels()
    private lateinit var binding: RestaurantListFragmentBinding
    private lateinit var restaurantListAdapter: RestaurantListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RestaurantListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantListAdapter = RestaurantListAdapter()
        binding.resturantsRecyclerView.adapter = restaurantListAdapter
        getRestaurantList()
    }

    private fun getRestaurantList() {
        lifecycleScope.launch {
            viewModel.restaurantsState.collect { state ->
                Log.d(TAG, "get list state: $state")
                when {
                    state.isLoading -> {
                        Log.d(TAG, "Loading..")
                        binding.restaurantListProgressBar.visibility = View.VISIBLE
                    }
                    state.restaurants.isNotEmpty() -> {
                        Log.d(TAG, "list: ${state.restaurants}")
                        binding.restaurantListProgressBar.visibility = View.GONE
                        restaurantListAdapter.submitList(state.restaurants)
                    }
                    state.error.isNotBlank() -> {
                        binding.restaurantListProgressBar.visibility = View.GONE
                        Log.d(TAG, "error: ${state.error}")
                    }
                }
            }
        }
    }
}