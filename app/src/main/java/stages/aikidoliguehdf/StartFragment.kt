package stages.aikidoliguehdf

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import stages.aikidoliguehdf.data.StagesDao
import stages.aikidoliguehdf.data.StagesRoomDatabase
import stages.aikidoliguehdf.databinding.FragmentStartBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast
import stages.aikidoliguehdf.data.Stages


class StartFragment<DataBaseHandler> : Fragment(R.layout.fragment_start) {
    lateinit var dao: StagesDao
    lateinit var db: StagesRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentStartBinding>(inflater, R.layout.fragment_start, container,false)
        // return inflater.inflate(R.layout.fragment_start, container, false)

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

        binding.facebook.setOnClickListener {
            var URL = "https://www.facebook.com/profile.php?id=100072962274878"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL)))
        }

        binding.twitter.setOnClickListener {
            var URL = "https://twitter.com/aikido_hdf"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL)))
        }

        binding.instagram.setOnClickListener {
            var URL = "https://www.instagram.com/aikido_hdf/"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL)))
        }


        db = StagesRoomDatabase.getInstance(activity as AppCompatActivity)
        dao = db.stagesDao()
        val testDatePattern = "yyyy-MM-dd"
        val testDateFormatter = DateTimeFormatter.ofPattern(testDatePattern)
        Log.i("db",db.toString())
        val current = LocalDate.now().plusDays(-1).toString()
        val nicecurrent =  testDateFormatter.format(LocalDate.parse(current))


            val nextStage = dao.getNext(nicecurrent).toList()
            val startDate = nextStage.first().startdate
            val europeanDatePattern = "dd.MM.yyyy"
            val europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern)
            val niceDate = europeanDateFormatter.format(LocalDate.parse(startDate))
            binding.nextStageTitle.text = nextStage.first().title
            binding.nextStageDate.text = niceDate

            binding.nextCourse.setOnClickListener() {
                val idNextStage = nextStage.first().idstages
                val action = StartFragmentDirections.actionStartFragmentToStageDetailFragment(idNextStage.toString()).setIdStage(idNextStage.toString())
                view?.findNavController()?.navigate(action)
            }


        return binding.root




    }
    fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}