package stages.aikidoliguehdf

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import stages.aikidoliguehdf.data.Favorites
import stages.aikidoliguehdf.data.Stages
import stages.aikidoliguehdf.data.StagesDao
import stages.aikidoliguehdf.data.StagesRoomDatabase
import stages.aikidoliguehdf.databinding.FragmentStageDetailBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class StageDetailFragment : Fragment() {

    private val args : StageDetailFragmentArgs by navArgs<StageDetailFragmentArgs>()
    lateinit var dao: StagesDao
    lateinit var db: StagesRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val binding = FragmentStageDetailBinding.inflate(layoutInflater)
        val idStage = args.idStage

        getItemsById(idStage)
        val item = getItemsById(idStage)
        val catname = getCatName(idStage).toString().replace("[","").replace("]","")

        /*Format Place with Address*/
        lateinit var placeStage : String

        if(item.first().places == "1"){
            placeStage = "A définir"
        }else {
            placeStage = getPlaceName(item.first().places) + "\n" + getPlaceAddress(item.first().places)
        }

        /*convert starthours*/
        lateinit var starthours : String
        if (item.first().startampm == "PM") {
            when (item.first().starthours) {
                "0" -> starthours = "00"
                "1" -> starthours = "13"
                "2" -> starthours = "14"
                "3" -> starthours = "15"
                "4" -> starthours = "16"
                "5" -> starthours = "17"
                "6" -> starthours = "18"
                "7" -> starthours = "19"
                "8" -> starthours = "20"
                "9" -> starthours = "21"
                "10" -> starthours = "22"
                "11" -> starthours = "23"
                "12" -> starthours = "12"

            }

        }
        if (item.first().startampm == "AM") {
            when (item.first().starthours) {
                "0" -> starthours = "00"
                "1" -> starthours = "01"
                "2" -> starthours = "02"
                "3" -> starthours = "03"
                "4" -> starthours = "04"
                "5" -> starthours = "05"
                "6" -> starthours = "06"
                "7" -> starthours = "07"
                "8" -> starthours = "08"
                "9" -> starthours = "09"
                "10" -> starthours = "10"
                "11" -> starthours = "11"
                "12" -> starthours = "00"
            }
        }
        /*convert startminutes*/
        lateinit var startminutes : String

        when (item.first().startminutes) {
            "0" -> startminutes = "00"
            "15" -> startminutes = "15"
            "30" -> startminutes = "30"
            "00" -> startminutes = "00"
        }


        /*convert endhours*/
        lateinit var endhours : String
        if (item.first().endampm == "PM") {
            when (item.first().endhours) {
                "0" -> endhours = "00"
                "1" -> endhours = "13"
                "2" -> endhours = "14"
                "3" -> endhours = "15"
                "4" -> endhours = "16"
                "5" -> endhours = "17"
                "6" -> endhours = "18"
                "7" -> endhours = "19"
                "8" -> endhours = "20"
                "9" -> endhours = "21"
                "10" -> endhours = "22"
                "11" -> endhours = "23"
                "12" -> endhours = "12"

            }
        }
        if (item.first().endampm == "AM") {
            when (item.first().endhours) {
                "0" -> endhours = "00"
                "6" -> endhours = "06"
                "7" -> endhours = "07"
                "8" -> endhours = "08"
                "9" -> endhours = "09"
            }
        }

        /*convert endminutes*/
        lateinit var endminutes : String

        when (item.first().endminutes) {
            "0" -> endminutes = "00"
            "15" -> endminutes = "15"
            "30" -> endminutes = "30"
            "00" -> endminutes = "00"
        }

        /*Format Date*/
        val startDate = item.first().startdate
        //LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("dd/mm/yyyy"))
        val europeanDatePattern = "dd.MM.yyyy"
        val europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern)
        val niceDate = europeanDateFormatter.format(LocalDate.parse(startDate.toString()))


        val itemTitle: TextView = binding.detItemTitle
        itemTitle.text = item.first().title
        //val itemContent: TextView = binding.detItemContent
        /*itemContent.text = item.first().excerpt*/
        binding.detItemStartdate.setText("Date : $niceDate")
        binding.detItemStartHour.setText("Horaires : " + starthours + "h"+ startminutes + " - " + endhours + "h"+ endminutes )
        binding.detItemLink.setText("Lien : " + item.first().link)

        binding.detItemCost.setText("Coût : " + item.first().cost + "€")
        binding.detItemCat.setText("Type : $catname")
        binding.detItemPlace.setText("Dojo : $placeStage")

        val enroute = binding.fabDirection
        val calendar = binding.fabCalendar
        val addfav = binding.fabFavorite

        if (checkFav(idStage) != null) {
            binding.fabFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24))
            addfav.setOnClickListener {
               val toast = Toast.makeText(context,"Stage déjà présent dans les favoris",Toast.LENGTH_SHORT)
                toast.show()

            }
        } else {
            addfav.setOnClickListener {
                val idstagetofav = item.first().idstages
                this.insertfav(idfav = 0, idstagesfav = idstagetofav.toString())
                val toast = Toast.makeText(context,"Stage ajouté dans les favoris",Toast.LENGTH_SHORT)
                toast.show()
            }
        }




        /*GPS Google Maps*/
        enroute.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=" + getPlaceAddress(item.first().places) )
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        /*Add to Calendar*/
        //calcul date et heure de début du stage et conversion en milliseconds
        val zone: ZoneId = ZoneId.of("Europe/Paris")
        val dtStart =  item.first().startdate + "T" + starthours + ":" + startminutes + ":00"
        val dateStart = LocalDateTime.parse(dtStart, DateTimeFormatter.ISO_DATE_TIME).atZone(zone)
        val startMill = dateStart.toInstant().epochSecond*1000

        //calcul date et heure de fin du stage et conversion en milliseconds
        val dtEnd =  item.first().startdate.toString() + "T" + endhours + ":" + endminutes + ":00"

        val dateEnd = LocalDateTime.parse(dtEnd, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(zone)
        val endMill = dateEnd.toInstant().epochSecond*1000

        calendar.setOnClickListener {
            val calIntent = Intent(Intent.ACTION_INSERT)
            calIntent.setData(CalendarContract.Events.CONTENT_URI)
            calIntent.putExtra(CalendarContract.Events.TITLE, item.first().title)
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMill)
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMill)
            calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, placeStage)
            //calIntent.putExtra(CalendarContract.Events.DTEND, timeInMilliseconds)
            startActivity(calIntent)

        }


        (activity as AppCompatActivity).supportActionBar?.title = item.first().title

        return binding.root
    }



    private fun getItemsById(idstages: String): List<Stages> {
        var db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()

        var stageList = db.stagesDao().loadAllByIds(idstages = idstages)
        return stageList

    }

    private fun getCatName(idstages: String): List<String> {
        var db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()


        var catName = db.stagesDao().getCatName(idstages = idstages)
        return catName
    }

    private fun getPlaceName(idplace: String): String {
        var db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()


        var placeName = db.stagesDao().loadNamePlaceById(idplace = idplace)
        return placeName
    }
    private fun getPlaceAddress(idplace: String): String {
        var db = Room.databaseBuilder(
            activity as AppCompatActivity,
            StagesRoomDatabase::class.java, "StagesDatabase"
        ).allowMainThreadQueries().build()


        var placeAddress = db.stagesDao().loadAddressPlaceById(idplace = idplace)
        return placeAddress
    }

    private fun insertfav(idfav: Int, idstagesfav: String){
        db = StagesRoomDatabase.getInstance(activity as AppCompatActivity)
        dao = db.stagesDao()
        dao.insertFav(Favorites(0, idstagesfav = idstagesfav))

    }

    private fun checkFav(idstage: String): String {
        db = StagesRoomDatabase.getInstance(activity as AppCompatActivity)
        dao = db.stagesDao()
        val favList = db.stagesDao().getFavsbyStageId(idstage = idstage )
        return favList

    }
}

