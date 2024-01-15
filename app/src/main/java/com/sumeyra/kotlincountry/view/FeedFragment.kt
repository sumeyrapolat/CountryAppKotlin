package com.sumeyra.kotlincountry.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sumeyra.kotlincountry.adapter.CountryAdapter
import com.sumeyra.kotlincountry.databinding.FragmentFeedBinding
import com.sumeyra.kotlincountry.viewmodel.FeedViewModel


class FeedFragment : Fragment() {


    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())

    //----------------------------------------------------------------------------------------------


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedBinding.inflate(inflater,container,false)
        val view = binding.root
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        viewModel.refreshData() //dataların gelmesini sağlıyor

        binding.countryList.layoutManager = LinearLayoutManager(context)
        binding.countryList.adapter = countryAdapter

        swipeRefreshData()

        observeLiveData()

    }

    fun swipeRefreshData(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.countryList.visibility= View.GONE
            binding.countryError.visibility = View.GONE
            binding.countryLoading.visibility = View.VISIBLE
            viewModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    fun observeLiveData() {
        viewModel.countries.observe(viewLifecycleOwner,Observer{
            it?.let {
                binding.countryList.visibility = View.VISIBLE
                //binding.swipeRefreshLayout.isRefreshing=false
                countryAdapter.setData(it)

            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer { error->
            error?.let{
                if (it){// hata mesajım varsa
                    binding.countryError.visibility = View.VISIBLE
                    //countryList.visibility= View.GONE
                }else{//hata mesajım yoksa
                    binding.countryError.visibility = View.GONE
                    //binding.countryList.visibility = View.VISIBLE
                    //binding.swipeRefreshLayout.isRefreshing=false

                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if (it){
                    binding.countryLoading.visibility = View.VISIBLE
                    binding.countryList.visibility = View.GONE
                    binding.countryError.visibility = View.GONE
                }else{
                    binding.countryLoading.visibility = View.GONE

                }
            }
        })

    }

}