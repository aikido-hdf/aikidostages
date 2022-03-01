package stages.aikidoliguehdf.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import stages.aikidoliguehdf.data.Stages
import stages.aikidoliguehdf.databinding.ListStagesBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListStageViewHolder, position: Int) {
        val item = items[position]
        val startDate = item.startdate
        val europeanDatePattern = "dd.MM.yyyy"
        val europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern)
        val niceDate = europeanDateFormatter.format(LocalDate.parse(startDate))

        holder.binding.txtTitle.text = item.title
        holder.binding.txtDate.text = niceDate
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