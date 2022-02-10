package stages.aikidoliguehdf

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.LENGTH_LONG
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import stages.aikidoliguehdf.adapter.ListFavAdapter
import stages.aikidoliguehdf.data.*
import stages.aikidoliguehdf.databinding.FragmentFavListBinding



class FavListFragment<DataBaseHandler>  : Fragment(R.layout.fragment_fav_list) {
    val adapter = ListFavAdapter()
    private var mContext: Context? = null
    private lateinit var binding: FragmentFavListBinding

    private lateinit var deletedItem : String
    //private val args : FavListFragmentArgs by navArgs<FavListFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = getActivity()


        // Inflate the layout for this fragment
        binding = FragmentFavListBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

       getItemsList()


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Liste des favoris"

        val swipegesture = object : SwipeGesture(activity as AppCompatActivity){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT ->{

                        val idfav = adapter.getFavId(viewHolder.adapterPosition)

                        adapter.deleteItem(viewHolder.adapterPosition)

                        deleteFav(idfav)

                        Snackbar.make(binding.recyclerView,"Supprimer ce favori ?", Snackbar.LENGTH_LONG)
                        .show()

                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipegesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        adapter.setOnItemClickListener(object : ListFavAdapter.onItemClickListener {

            override fun onItemClick(position: Int) {
                val idStage = adapter.items[position].idstages
                val action = FavListFragmentDirections.actionFavListFragmentToStageDetailFragment(
                    idStage.toString()
                ).setIdStage(idStage.toString())
                view.findNavController().navigate(action)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getItemsList() {
        val db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()
        val newList = mutableListOf<Stages>()
        var stageList : ArrayList<Stages>
        val favList = db.stagesDao().getAllFavs()
        favList.forEach {
            val item = it.idstagesfav
            stageList = db.stagesDao().loadAllByIds(item) as ArrayList<Stages>
            newList += stageList
        }
        adapter.setDataList(newList)
    }

    private fun deleteFav(id : String){
        val db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()
        db.stagesDao().deleteFavbyId(id = id)


    }
}