package stages.aikidoliguehdf

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import stages.aikidoliguehdf.data.StagesDao
import stages.aikidoliguehdf.data.StagesRoomDatabase
import stages.aikidoliguehdf.databinding.FragmentStartBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.content.Intent
import android.net.Uri


class StartFragment<DataBaseHandler> : Fragment(R.layout.fragment_start) {
    private lateinit var dao: StagesDao
    lateinit var db: StagesRoomDatabase

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentStartBinding>(inflater, R.layout.fragment_start, container,false)
        // return inflater.inflate(R.layout.fragment_start, container, false)

        /*nav buttons*/
        binding.button.setOnClickListener{
                view : View -> view.findNavController().navigate(R.id.action_startFragment_to_stageListFragment)
        }
        binding.button3.setOnClickListener{
                view : View -> view.findNavController().navigate(R.id.action_startFragment_to_stagePlaceFragment)
        }
        binding.button2.setOnClickListener{
                view : View -> view.findNavController().navigate(R.id.action_startFragment_to_stageCatFragment)
        }
        binding.buttonFav.setOnClickListener {
            view:View->view.findNavController().navigate(R.id.action_startFragment_to_favListFragment)
        }

        /*social media buttons*/
        binding.facebook.setOnClickListener {
            val url = "https://www.facebook.com/profile.php?id=100072962274878"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        binding.twitter.setOnClickListener {
            val url = "https://twitter.com/aikido_hdf"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        binding.instagram.setOnClickListener {
            val url = "https://www.instagram.com/aikido_hdf/"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        /*Next Course*/
        db = StagesRoomDatabase.getInstance(activity as AppCompatActivity)
        dao = db.stagesDao()

        /*format date Now to same pattern as StartDate*/
        val datePattern = "yyyy-MM-dd"
        val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
        val current = LocalDate.now().plusDays(-1).toString()
        val niceCurrentDate =  dateFormatter.format(LocalDate.parse(current))

        /*Query next course from tomorrow*/
        val nextCourse = dao.getNext(niceCurrentDate).toList()
        val startDate = nextCourse.firstOrNull()?.startdate
        val europeanDatePattern = "dd.MM.yyyy"
        val europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern)
        if(startDate != null){
            val niceDate = europeanDateFormatter.format(LocalDate.parse(startDate))
            binding.nextStageDate.text = niceDate
        }else{
            binding.nextStageDate.text = "donnée vide"
        }

        binding.nextStageTitle.text = nextCourse.firstOrNull()?.title

        binding.nextCourse.setOnClickListener {
            val idNextStage = nextCourse.firstOrNull()?.idstages
            val action = StartFragmentDirections.actionStartFragmentToStageDetailFragment(idNextStage.toString()).setIdStage(idNextStage.toString())
            view?.findNavController()?.navigate(action)
        }
      return binding.root
    }
}