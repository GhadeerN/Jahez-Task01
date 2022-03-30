package sa.edu.tuwaiq.jaheztask01.presentation.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import sa.edu.tuwaiq.jaheztask01.common.base.BaseFragment
import sa.edu.tuwaiq.jaheztask01.databinding.FragmentOffersBinding
import sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist.RestaurantListAdapter
import sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist.RestaurantListViewModel

private const val TAG = "OffersFragment"

class OffersFragment : BaseFragment() {

    lateinit var binding: FragmentOffersBinding
    private lateinit var restaurantListAdapter: RestaurantListAdapter
    private val viewModel: RestaurantListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOffersBinding.inflate(inflater, container, false)
        setBaseViewModel(viewModel)
        setUIState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurantListAdapter = RestaurantListAdapter()
        binding.offersRecyclerView.adapter = restaurantListAdapter

        // Get the restaurant list to filter it
        lifecycleScope.launchWhenStarted {
            viewModel.restaurantsState.collectLatest { state ->
                if (state.isNotEmpty()) {
                    restaurantListAdapter.submitList(state.filter { it.hasOffer })
                }
            }
        }
    }
}