package stages.aikidoliguehdf.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import stages.aikidoliguehdf.data.Categories
import stages.aikidoliguehdf.databinding.ListCategoriesBinding


class ListCatAdapter: RecyclerView.Adapter<ListCatAdapter.ListCatViewHolder>() {

   var categoriesFiltered = mutableListOf<Categories>()

    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    fun setCatList(categories: ArrayList<Categories>) {
        val categoriesFiltered = categories.filter { it.count > "0" }
        this.categoriesFiltered = categoriesFiltered.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCatViewHolder {
        val binding = ListCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListCatViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ListCatViewHolder, position: Int) {
        val currentItem = categoriesFiltered[position]
        holder.binding.txtCat.text = currentItem.name
    }

    class ListCatViewHolder (val binding: ListCategoriesBinding, listener: OnItemClickListener): RecyclerView.ViewHolder(binding.root) {
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun getItemCount() = categoriesFiltered.size
}