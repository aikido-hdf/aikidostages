package stages.aikidoliguehdf.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import stages.aikidoliguehdf.data.Stages
import stages.aikidoliguehdf.databinding.ListFavoritesBinding


class ListFavAdapter : RecyclerView.Adapter<ListFavAdapter.ListFavViewHolder>() {
    var items = ArrayList<Stages>()
    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    fun setDataList(items: List<Stages>) {
        this.items = items.toMutableList() as ArrayList<Stages>
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



    fun getFavId(position: Int): String {
        return items[position].idstages.toString()
    }


    fun deleteItem(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position);
    }



    class ListFavViewHolder (val binding : ListFavoritesBinding, listener: OnItemClickListener): RecyclerView.ViewHolder(binding.root){
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}