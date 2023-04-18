package com.xtensolutions.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xtensolutions.sample.R
import com.xtensolutions.sample.core.adapter.BaseViewHolder
import com.xtensolutions.sample.databinding.FragmentRestaurantBinding
import com.xtensolutions.sample.ui.adapter.RestaurantAdapter
import com.xtensolutions.sample.ui.viewmodel.RestaurantViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 20:43.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class RestaurantFragment : Fragment(R.layout.fragment_restaurant),
    BaseViewHolder.ViewHolderClickListener {
    private lateinit var binding: FragmentRestaurantBinding
    private val viewModel: RestaurantViewModel by viewModels()
    private lateinit var adapter: RestaurantAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantBinding.bind(view)
        initList()
        loadRestaurant()
    }

    private fun initList() {
        adapter = RestaurantAdapter(requireContext(), emptyList())
        adapter.viewHolderClickListener = this
        binding.rvRestaurants.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRestaurants.adapter = adapter
//        binding.rvRestaurants.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
    }

    private fun loadRestaurant() {
        viewModel.getRestaurants().observe(viewLifecycleOwner) {
            it ?: showNoDataView(true)

            if (it.isEmpty()) {
                showNoDataView(true)
                return@observe
            }

            adapter.updateList(it.toMutableList())
            showNoDataView(false)
//            binding.progressRestaurant.isVisible = false
        }
    }

    private fun showNoDataView(visibility: Boolean) {
        requireActivity().runOnUiThread {
            binding.progressRestaurant.isVisible = false
            binding.viewNoData.root.isVisible = visibility
        }
    }

    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == R.id.clRowRestaurantRoot) {
            val restaurant = adapter.getItem(position)
            val direction = RestaurantFragmentDirections.restaurantToDeal(restaurant)
            findNavController().navigate(direction)
        }
    }
}