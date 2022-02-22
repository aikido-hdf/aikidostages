package stages.aikidoliguehdf.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import stages.aikidoliguehdf.data.Stages
import stages.aikidoliguehdf.databinding.ListStagesBinding


class ListStageAdapter: RecyclerView.Adapter<ListStageAdapter.ListStageViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStageViewHolder {
        val binding = ListStagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStageViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ListStageViewHolder, position: Int) {
        val item = items[position]
        holder.binding.txtTitle.text = item.title
        holder.binding.txtDate.text = item.startdate
    }

    class ListStageViewHolder (val binding: ListStagesBinding, listener: OnItemClickListener): RecyclerView.ViewHolder(binding.root) {
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