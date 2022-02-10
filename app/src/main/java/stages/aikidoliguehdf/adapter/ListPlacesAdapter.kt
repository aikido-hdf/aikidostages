package stages.aikidoliguehdf.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import stages.aikidoliguehdf.data.Places
import stages.aikidoliguehdf.databinding.ListPlacesBinding


class ListPlacesAdapter: RecyclerView.Adapter<ListPlacesAdapter.ListPlacesViewHolder>() {

    var places = mutableListOf<Places>()
    var placesFiltered = mutableListOf<Places>()

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: ListPlacesAdapter.onItemClickListener){
        mListener = listener
    }

    fun setPlacesList(places: List<Places>) {

        var placesFiltered = places.filter { it.count > "0" }
        this.placesFiltered = placesFiltered.toMutableList()
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlacesViewHolder {
        val binding = ListPlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListPlacesViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ListPlacesViewHolder, position: Int) {
        val currentItem = placesFiltered[position]
        holder.binding.txtPlace.text = currentItem.name
    }


    class ListPlacesViewHolder (val binding: ListPlacesBinding, listener: ListPlacesAdapter.onItemClickListener): RecyclerView.ViewHolder(binding.root) {
        init{
            itemView.setOnClickListener(){
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun getItemCount() = placesFiltered.size
}