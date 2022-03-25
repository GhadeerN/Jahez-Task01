package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.R
import sa.edu.tuwaiq.jaheztask01.databinding.RestaurantListFragmentBinding
import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem

private const val TAG = "RestaurantListFragment"

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {


    private val viewModel: RestaurantListViewModel by activityViewModels()
    private lateinit var binding: RestaurantListFragmentBinding
    private lateinit var restaurantListAdapter: RestaurantListAdapter
    lateinit var restaurantList: List<RestaurantItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // This line is to show the menu items in the action/tool bar
        setHasOptionsMenu(true)

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
                        restaurantList = state.restaurants
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // This will connect the main_menu.xml with the action/tool bar :)
        requireActivity().menuInflater.inflate(R.menu.filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.all_filter -> {
                restaurantListAdapter.submitList(restaurantList.filter { it.hasOffer })
            }
            R.id.rate_filter -> {
                restaurantListAdapter.submitList(restaurantList.sortedByDescending { it.rating })
            }
            R.id.distance_filter -> {
                restaurantListAdapter.submitList(restaurantList.sortedByDescending { it.distance })
            }
        }
        return super.onOptionsItemSelected(item)
    }
}