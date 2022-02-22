package stages.aikidoliguehdf

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import stages.aikidoliguehdf.adapter.ListStageAdapter
import stages.aikidoliguehdf.data.Stages
import stages.aikidoliguehdf.data.StagesRoomDatabase
import stages.aikidoliguehdf.databinding.FragmentStageListBinding
import java.time.LocalDate


class StageListFragment<DataBaseHandler> : Fragment(R.layout.fragment_stage_list) {

    val adapter = ListStageAdapter()
    private var mContext: Context? = null
    private lateinit var binding: FragmentStageListBinding
    private val args : StageListFragmentArgs by navArgs<StageListFragmentArgs>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = activity

        binding = FragmentStageListBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        val argsCategory = args.idCategory.toString()
        val argsPlaces = args.idPlaces.toString()

        if(argsCategory != "null"){
            val idCat = "%$argsCategory%"
            getItemsListByCat(idCat)
        }
        else if(argsPlaces != "null") {
            val idPlace = "%$argsPlaces%"
            getItemsListByPlace(idPlace)
        }else {

            getItemsList()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Liste des stages"

        adapter.setOnItemClickListener(object : ListStageAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {
                val idStage = adapter.items[position].idstages
                val action = StageListFragmentDirections.actionStageListFragmentToStageDetailFragment(
                    idStage.toString()

                ).setIdStage(idStage.toString())
                view.findNavController().navigate(action)
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getItemsList(): ArrayList<Stages> {
        val db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()
        val current = LocalDate.now().minusMonths(3)
        val stageList = db.stagesDao().getAllxMonths(current.toString())
        adapter.setDataList(stageList as ArrayList<Stages>)
        return stageList
    }

    private fun getItemsListByCat(idcat: String): ArrayList<Stages> {
        val db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()
        val stageList = db.stagesDao().getAllbyCat(idcat = idcat)

        adapter.setDataList(stageList as ArrayList<Stages>)
        return stageList
    }

    private fun getItemsListByPlace(idplace: String): ArrayList<Stages> {
        val db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()
        val stageList = db.stagesDao().getAllbyPlace(idplace = idplace)

        adapter.setDataList(stageList as ArrayList<Stages>)
        return stageList
    }
}