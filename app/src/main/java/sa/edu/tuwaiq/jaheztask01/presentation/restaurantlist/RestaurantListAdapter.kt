package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import sa.edu.tuwaiq.jaheztask01.HungerZoneApplication
import sa.edu.tuwaiq.jaheztask01.R
import sa.edu.tuwaiq.jaheztask01.databinding.RestaurantItemLayoutBinding
import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem

class RestaurantListAdapter :
    RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<RestaurantItem>() {
        override fun areItemsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<RestaurantItem>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantListAdapter.ViewHolder {
        return ViewHolder(
            RestaurantItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class ViewHolder(val binding: RestaurantItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantItem) {
            binding.restaurant = item
            binding.offer = if (item.hasOffer) item.offer?.get(0).toString() else ""
            Glide.with(HungerZoneApplication.appContext).load(item.image)
                .into(binding.restaurantImageItem)
            binding.executePendingBindings()
        }
    }
}