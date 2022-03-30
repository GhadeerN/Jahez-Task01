package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.R
import sa.edu.tuwaiq.jaheztask01.common.base.BaseFragment
import sa.edu.tuwaiq.jaheztask01.databinding.RestaurantListFragmentBinding

private const val TAG = "RestaurantListFragment"

@AndroidEntryPoint
class RestaurantListFragment : BaseFragment() {


    private val viewModel: RestaurantListViewModel by activityViewModels()
    private lateinit var binding: RestaurantListFragmentBinding
    private lateinit var restaurantListAdapter: RestaurantListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // This line is to show the menu items in the action/tool bar
        setHasOptionsMenu(true)

        binding = RestaurantListFragmentBinding.inflate(inflater, container, false)
        _viewModel = viewModel
        setUIState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurantListAdapter = RestaurantListAdapter()
        binding.resturantsRecyclerView.adapter = restaurantListAdapter
        getRestaurantList()
    }

    private fun getRestaurantList() {
        Log.d(TAG, "getRestaurantList --------------------------")
        lifecycleScope.launch {
            viewModel.restaurantsState.collect { state ->
                Log.d(TAG, "get list state: $state")
                if (state.isNotEmpty()) {
                    restaurantListAdapter.submitList(state)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // This will connect the main_menu.xml with the action/tool bar :)
        requireActivity().menuInflater.inflate(R.menu.filter_menu, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)

        /* Normally the searchItem will be treated as normal menuItem, so we have to emphasis that
           it's a view */
        val searchView = searchItem.actionView as SearchView

        // Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                restaurantListAdapter.submitList(
                    viewModel.restaurantsState.value.filter {
                        it.name.lowercase().contains(query!!.lowercase())
                    }
                )
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                restaurantListAdapter.submitList(viewModel.restaurantsState.value)
                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.all_filter -> {
                restaurantListAdapter.submitList(viewModel.restaurantsState.value)
            }
            R.id.rate_filter -> {
                restaurantListAdapter.submitList(viewModel.restaurantsState.value.sortedByDescending { it.rating })
            }
            R.id.distance_filter -> {
                restaurantListAdapter.submitList(viewModel.restaurantsState.value.sortedByDescending { it.distance })
            }
        }
        return super.onOptionsItemSelected(item)
    }
}