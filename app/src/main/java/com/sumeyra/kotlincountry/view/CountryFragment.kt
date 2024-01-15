package com.sumeyra.kotlincountry.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.sumeyra.kotlincountry.databinding.FragmentCountryBinding
import com.sumeyra.kotlincountry.util.downloadFromUrl
import com.sumeyra.kotlincountry.util.placeHolderProgressBar
import com.sumeyra.kotlincountry.viewmodel.CountryViewModel


class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CountryViewModel


    private var countryUuid=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCountryBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {
            countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryUuid)


        observeLiveData()

    }

    private fun observeLiveData(){
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer {country->
            country?.let {
                binding .selectedCountryName.text = country.countryName
                binding.selectedCountryRegion.text = country.countryRegion
                binding.selectedCountryCapital.text = country.countryCapital
                binding.selectedCountryCurrency.text = country.countryCurrency
                binding.selectedCountryLanguage.text = country.countryLanguage

                //görsel en son
                context?.let{
                    binding.selectedCountryImage.downloadFromUrl(country.countryFlagurl, placeHolderProgressBar(it))

                }

            }
        })
    }

}