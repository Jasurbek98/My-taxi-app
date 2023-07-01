package uz.jsoft.mytaxiapp.presentation.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.jsoft.mytaxiapp.R
import uz.jsoft.mytaxiapp.utils.extensions.bindItem
import uz.jsoft.mytaxiapp.data.remote.response.SearchResponse
import uz.jsoft.mytaxiapp.databinding.SearchItemBinding
import uz.jsoft.mytaxiapp.utils.`typealias`.DoubleBlock
import uz.jsoft.mytaxiapp.utils.`typealias`.SingleBlock
import uz.jsoft.mytaxiapp.utils.hide
import uz.jsoft.mytaxiapp.utils.metrToKM
import uz.jsoft.mytaxiapp.utils.show

/**
 * Created by Jasurbek Kurganbayev 28/03/2022
 */
class SearchAdapter(
    private val onSubSearchAddressItemClick: SingleBlock<SearchResponse.SearchAddressItem>
) : ListAdapter<SearchResponse.SearchAddressItem, SearchAdapter.VH>(
    SEARCH_ITEM_CALLBACK
) {

    private var itemClickListener: DoubleBlock<SearchResponse.SearchAddressItem, Int>? = null

    companion object {
        var SEARCH_ITEM_CALLBACK =
            object : DiffUtil.ItemCallback<SearchResponse.SearchAddressItem>() {
                override fun areItemsTheSame(
                    oldItem: SearchResponse.SearchAddressItem,
                    newItem: SearchResponse.SearchAddressItem
                ) = newItem.addressId == oldItem.addressId

                override fun areContentsTheSame(
                    oldItem: SearchResponse.SearchAddressItem,
                    newItem: SearchResponse.SearchAddressItem
                ) = oldItem.address == newItem.address &&
                        oldItem.distance == newItem.distance &&
                        oldItem.formattedAddress == newItem.formattedAddress
            }
    }

    fun onItemClickListener(block: DoubleBlock<SearchResponse.SearchAddressItem, Int>) {
        itemClickListener = block
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()


    inner class VH(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var subSearchAdapter: SubSearchAdapter


        fun bind() = bindItem {
            val data = getItem(absoluteAdapterPosition)
            binding.apply {

                if (data.addressId != null) {
                    subSearchAdapter = SubSearchAdapter(onSubSearchAddressItemClick)
                    binding.subSearchList.adapter = subSearchAdapter
                    subSearchAdapter.submitList(data.subSearchList)
                    subsearchHomeImage.show()
                    searchItemDistance.hide()
                }
                searchItemDistance.text = data.distance?.toFloat()?.metrToKM()
                searchHomeName.text = data.address
                searchItemDistrict.text = data.formattedAddress


                rootItem.setOnClickListener {
                    itemClickListener?.invoke(data, absoluteAdapterPosition)
                    if (data.addressId != null) {
                        if (subSearchList.visibility == View.GONE) {
                            subSearchList.visibility = View.VISIBLE
                            subsearchHomeImage.setImageResource(R.drawable.ic_arrow_down)
                        } else {

                            subSearchList.visibility = View.GONE
                            subsearchHomeImage.setImageResource(R.drawable.ic_arrow_up)
                        }
                    }

                }

            }
        }
    }

}