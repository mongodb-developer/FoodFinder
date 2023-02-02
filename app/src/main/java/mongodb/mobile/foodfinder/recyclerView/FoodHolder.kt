package mongodb.mobile.foodfinder.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import mongodb.mobile.foodfinder.databinding.LocatorItemBinding
import mongodb.mobile.foodfinder.models.Restaurant

class FoodHolder(private val itemBinding: LocatorItemBinding): RecyclerView.ViewHolder(itemBinding.root) {

    fun bindValues(restaurant: Restaurant){
        itemBinding.tvName.text = restaurant.name
        itemBinding.tvCuisine.text = restaurant.cuisine

        "${restaurant.borough}  ${restaurant.address?.street}".also { itemBinding.tvStreet.text = it }
    }
}