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
import stages.aikidoliguehdf.adapter.ListPlacesAdapter
import stages.aikidoliguehdf.data.Places
import stages.aikidoliguehdf.data.StagesRoomDatabase
import stages.aikidoliguehdf.databinding.FragmentStagePlaceBinding

class StagePlaceFragment : Fragment() {

    val placesAdapter = ListPlacesAdapter()
    private lateinit var binding: FragmentStagePlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentStagePlaceBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = placesAdapter
        getPlacesList()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Lieux de stages"
        placesAdapter.setOnItemClickListener(object : ListPlacesAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {

                val idPlace = placesAdapter.placesFiltered[position].idplace
                val action = StagePlaceFragmentDirections.actionStagePlaceFragmentToStageListFragment(

                ).setIdPlaces(idPlace)
                view.findNavController().navigate(action)
            }
        })
    }

    private fun getPlacesList(): ArrayList<Places> {
        val db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()

        val placesList = db.stagesDao().getAllPlaces()
        placesAdapter.setPlacesList(placesList as ArrayList<Places>)
        return placesList
    }
}