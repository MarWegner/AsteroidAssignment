package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }

    private val adapter = AsteroidAdapter(AsteroidListener { asteroid -> viewModel.onAsteroidClicked(asteroid) })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner, Observer {
                asteroid -> if(asteroid != null){
                    findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                    viewModel.navigateFinished()
                }
        })


        binding.asteroidRecycler.adapter = adapter

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroidList.observe(viewLifecycleOwner, Observer { asteroids -> asteroids.apply { adapter.submitList(asteroids)} })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_week_menu -> viewModel.onFilterClicked(Filter.WEEK)
            R.id.show_today_menu-> viewModel.onFilterClicked(Filter.TODAY)
            else -> viewModel.onFilterClicked(Filter.ALL)
        }
        return true
    }
}
