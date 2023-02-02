package mongodb.mobile.foodfinder.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import mongodb.mobile.foodfinder.databinding.LocatorItemBinding
import mongodb.mobile.foodfinder.models.Restaurant

class FoodAdapter(data: OrderedRealmCollection<Restaurant>): RealmRecyclerViewAdapter<Restaurant, FoodHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val itemBinding = LocatorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val restaurant = getItem(position)
        holder.bindValues(restaurant!!)

    }
}