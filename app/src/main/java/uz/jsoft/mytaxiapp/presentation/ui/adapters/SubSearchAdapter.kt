package uz.jsoft.mytaxiapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.jsoft.mytaxiapp.data.remote.response.SearchResponse
import uz.jsoft.mytaxiapp.databinding.SubSearchItemBinding
import uz.jsoft.mytaxiapp.utils.`typealias`.SingleBlock
import uz.jsoft.mytaxiapp.utils.extensions.bindItem
import uz.jsoft.mytaxiapp.utils.metrToKM

/**
 * Created by Jasurbek Kurganbayev 01/04/2022
 */
class SubSearchAdapter(private val onSearchAddressClick: SingleBlock<SearchResponse.SearchAddressItem>) :
    ListAdapter<SearchResponse.SearchAddressItem, SubSearchAdapter.VH>(
        SEARCH_ITEM_CALLBACK
    ) {

//    private var itemClickListener: DoubleBlock<SearchResponse.SearchAddressItem, Int>? = null


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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        SubSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    inner class VH(private val binding: SubSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind() = bindItem {
            val data = getItem(absoluteAdapterPosition)
            binding.apply {

                subsearchItemDistance.text = data.distance?.toFloat()?.metrToKM()
                subsearchAddressName.text = data.address
                subsearchAddressDistrict.text = data.formattedAddress

                subItemRoot.setOnClickListener {
                    onSearchAddressClick.invoke(getItem(absoluteAdapterPosition))
                }
            }
        }
    }

}