package stages.aikidoliguehdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import stages.aikidoliguehdf.adapter.ListCatAdapter
import stages.aikidoliguehdf.data.Categories
import stages.aikidoliguehdf.data.StagesRoomDatabase
import stages.aikidoliguehdf.databinding.FragmentStageCatBinding

class StageCatFragment<DataBaseHandler>  : Fragment() {

    val catAdapter = ListCatAdapter()
    private lateinit var binding: FragmentStageCatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentStageCatBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = catAdapter
        getCatList()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Catégories de stages"
        catAdapter.setOnItemClickListener(object : ListCatAdapter.onItemClickListener {

            override fun onItemClick(position: Int) {
                val idCat = catAdapter.categoriesFiltered[position].idcat
                val action = StageCatFragmentDirections.actionStageCatFragmentToStageListFragment(
                ).setIdCategory(idCat.toString())
                view.findNavController().navigate(action)
            }
        })
    }

    private fun getCatList(): ArrayList<Categories> {
        val db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()
        val catList = db.stagesDao().getAllCategories()

        catAdapter.setCatList(catList as ArrayList<Categories>)
        return catList
    }
}