package stages.aikidoliguehdf.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import stages.aikidoliguehdf.data.Favorites
import stages.aikidoliguehdf.data.Stages
import stages.aikidoliguehdf.databinding.ListFavoritesBinding


class ListFavAdapter : RecyclerView.Adapter<ListFavAdapter.ListFavViewHolder>() {
    var items = ArrayList<Stages>()
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setDataList(items: List<Stages>) {
        this.items = items.toMutableList() as ArrayList<Stages>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListFavViewHolder {
        val binding = ListFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListFavViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ListFavViewHolder, position: Int) {
        val item = items[position]
        holder.binding.txtTitle.text = item.title
        holder.binding.txtDate.text = item.startdate
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun getFavId (position: Int): String{
        val item =  items[position].idstages.toString()
        return item

    }


    fun deleteItem(position: Int){

        items.removeAt(position)

        notifyDataSetChanged()

    }



    class ListFavViewHolder (val binding : ListFavoritesBinding, listener: onItemClickListener): RecyclerView.ViewHolder(binding.root){
        init{
            itemView.setOnClickListener(){
                listener.onItemClick(adapterPosition)
            }
        }
    }
}