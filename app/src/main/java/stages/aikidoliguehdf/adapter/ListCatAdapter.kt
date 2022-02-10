package stages.aikidoliguehdf.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import stages.aikidoliguehdf.data.Categories
import stages.aikidoliguehdf.databinding.ListCategoriesBinding


class ListCatAdapter: RecyclerView.Adapter<ListCatAdapter.ListCatViewHolder>() {

    var categories = mutableListOf<Categories>()
    var categoriesFiltered = mutableListOf<Categories>()

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setCatList(categories: ArrayList<Categories>) {
        var categoriesFiltered = categories.filter { it.count > "0" }
        this.categoriesFiltered = categoriesFiltered.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCatViewHolder {
        val binding = ListCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListCatViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ListCatViewHolder, position: Int) {
        val currentItem = categoriesFiltered[position]
        holder.binding.txtCat.text = currentItem.name
    }

    class ListCatViewHolder (val binding: ListCategoriesBinding, listener: onItemClickListener): RecyclerView.ViewHolder(binding.root) {
        init{
            itemView.setOnClickListener(){
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun getItemCount() = categoriesFiltered.size
}