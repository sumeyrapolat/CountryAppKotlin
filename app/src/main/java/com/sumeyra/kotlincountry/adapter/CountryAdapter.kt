package com.sumeyra.kotlincountry.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sumeyra.kotlincountry.databinding.ItemCountryRowBinding
import com.sumeyra.kotlincountry.model.Country
import com.sumeyra.kotlincountry.util.downloadFromUrl
import com.sumeyra.kotlincountry.util.placeHolderProgressBar
import com.sumeyra.kotlincountry.view.FeedFragment
import com.sumeyra.kotlincountry.view.FeedFragmentDirections


//Country listesine verileri eklemek için yazdığımız adapter
class CountryAdapter( var countryList: ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.countryViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun setData(data:List<Country>){
        countryList.clear()
        countryList.addAll(data)
        notifyDataSetChanged()
    }

    //verisetinde değişiklik olduğu zaman kullanmamız lazım

    inner class countryViewHolder(val binding : ItemCountryRowBinding ):ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): countryViewHolder {
       return countryViewHolder(ItemCountryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        //buradaki view ım ViewHolder a paslanacak oradanki view olduktan sonra recyclerview a paslanacak
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: countryViewHolder, position: Int) {
        holder.binding.countryName.text= countryList[position].countryName
        holder.binding.regionName.text = countryList[position].countryRegion
        holder.binding.countryName.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)

        }

        //görünüm  için util yazdıktan sonra
        holder.binding.imageView.downloadFromUrl(countryList[position].countryFlagurl, placeHolderProgressBar(holder.itemView.context))

    }



}