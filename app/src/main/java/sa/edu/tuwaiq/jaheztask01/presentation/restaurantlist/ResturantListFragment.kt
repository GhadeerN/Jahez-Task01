package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sa.edu.tuwaiq.jaheztask01.R

class ResturantListFragment : Fragment() {

    companion object {
        fun newInstance() = ResturantListFragment()
    }

    private lateinit var viewModel: ResturantListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.resturant_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResturantListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}