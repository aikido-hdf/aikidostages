package stages.aikidoliguehdf

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONTokener
import stages.aikidoliguehdf.data.*
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    lateinit var dao: StagesDao
    lateinit var db: StagesRoomDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        db = StagesRoomDatabase.getInstance(this)
        dao = db.stagesDao()

        parseJSON()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseJSON() {

        GlobalScope.launch(Dispatchers.IO) {
            val urlEvents =
                URL("https://aikido-hdf.fr/wp-json/wp/v2/mec-events?per_page=50")
            val httpsURLConnection = urlEvents.openConnection() as HttpsURLConnection
            httpsURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = false
            // Check if the connection is successful
            val responseCode = httpsURLConnection.responseCode
            Log.i("test connexion", responseCode.toString())
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    // Convert raw JSON to pretty JSON using GSON library

                    val jsonArray = JSONTokener(response).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {

                        val id = jsonArray.getJSONObject(i).getString("id")
                        val title = jsonArray.getJSONObject(i).getString("title")
                            .replace("{\"rendered\":\"", "").replace("\"}", "")
                            .replace("&rsquo;", "'")
                            .replace("&#8211;", ":").replace("&#8212;", "_")
                        val starthours = jsonArray.getJSONObject(i).getString("starthours")
                        val startminutes = jsonArray.getJSONObject(i).getString("startminutes")
                        val endhours = jsonArray.getJSONObject(i).getString("endhours")
                        val endminutes = jsonArray.getJSONObject(i).getString("endminutes")
                        val startampm = jsonArray.getJSONObject(i).getString("startampm")
                        val endampm = jsonArray.getJSONObject(i).getString("endampm")

                        val startdate = jsonArray.getJSONObject(i).getString("startdate")

                        val media = jsonArray.getJSONObject(i).getString("featured_media")
                        val category =
                            jsonArray.getJSONObject(i).getString("mec_category").replace(",", ", ")
                                .replace("[", "")
                                .replace("]", "")

                        val cost = jsonArray.getJSONObject(i).getString("cost")

                        val places = jsonArray.getJSONObject(i).getString("places").replace("[", "")
                            .replace("]", "").replace("\"", "")

                        val renderedExcerpt = jsonArray.getJSONObject(i).getJSONObject("excerpt")
                        val excerpt = renderedExcerpt.getString("rendered")
                        val link = jsonArray.getJSONObject(i).getString("link")

                        val renderedContentJsonObject =
                            jsonArray.getJSONObject(i).getJSONObject("content")
                        val content = renderedContentJsonObject.getString("rendered")


                        val model =
                            Stages(
                                id.toLong(),
                                title,
                                startdate,
                                media,
                                category,
                                cost,
                                starthours,
                                endhours,
                                startminutes,
                                endminutes,
                                startampm,
                                endampm,
                                places,
                                excerpt,
                                link,
                                content
                            )
                        Log.i("stages", model.toString())
                        dao.insertAll(model)
                    }
                }
            } else {
                //Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
            val urlCategories =
                URL("https://aikido-hdf.fr/wp-json/wp/v2/mec_category?per_page=50")
            val httpsURLConnectiontoURLCategories =
                urlCategories.openConnection() as HttpsURLConnection
            httpsURLConnectiontoURLCategories.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpsURLConnectiontoURLCategories.requestMethod = "GET"
            httpsURLConnectiontoURLCategories.doInput = true
            httpsURLConnectiontoURLCategories.doOutput = false
            // Check if the connection is successful
            val responseCodetoCat = httpsURLConnectiontoURLCategories.responseCode
            if (responseCodetoCat == HttpsURLConnection.HTTP_OK) {
                val responseCat = httpsURLConnectiontoURLCategories.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val jsonArrayCat = JSONTokener(responseCat).nextValue() as JSONArray
                    for (i in 0 until jsonArrayCat.length()) {

                        val id = jsonArrayCat.getJSONObject(i).getString("id")
                        val name = jsonArrayCat.getJSONObject(i).getString("name")
                        val count = jsonArrayCat.getJSONObject(i).getString("count")

                        val modelforCat =
                            Categories(
                                id, name, count
                            )

                        dao.insertCategory(modelforCat)

                        val listcat = mutableListOf(id.toLong())

                        for (i in listcat) {
                            //val tempMap = mutableMapOf<String, String>()
                            val item = id.toString()
                            val test = db.stagesDao().getByCat(item)

                            for (y in test) {
                                val idstages = y.idstages.toString()
                                val newList = mutableListOf<StagesCatMap>()

                                listcat.forEach {
                                    newList += StagesCatMap(idstages, item)
                                }
                                dao.insertAllStagesCatMap(newList)

                            }
                        }
                    }
                }
            } else {
                //Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
            val urlLocations =
                URL("https://aikido-hdf.fr/wp-json/wp/v2/mec_location?per_page=50")
            val httpsURLConnectionforLocations = urlLocations.openConnection() as HttpsURLConnection
            httpsURLConnectionforLocations.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpsURLConnectionforLocations.requestMethod = "GET"
            httpsURLConnectionforLocations.doInput = true
            httpsURLConnectionforLocations.doOutput = false
            // Check if the connection is successful
            val responseCodeforLoc = httpsURLConnection.responseCode
            if (responseCodeforLoc == HttpsURLConnection.HTTP_OK) {
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    // Convert raw JSON to pretty JSON using GSON library

                    val jsonArrayLoc = JSONTokener(response).nextValue() as JSONArray
                    for (i in 0 until jsonArrayLoc.length()) {

                        val id = jsonArrayLoc.getJSONObject(i).getString("id")
                        val name = jsonArrayLoc.getJSONObject(i).getString("name")
                        val count = jsonArrayLoc.getJSONObject(i).getString("count")
                        val address = jsonArrayLoc.getJSONObject(i).getString("address")
                        val modelforLocations =
                            Places(
                                id, name, count, address
                            )
                        dao.insertAllPlaces(modelforLocations)
                    }
                }


    /*fun parseJSONPlaces() {

        GlobalScope.launch(Dispatchers.IO) {
            val url =
                URL("https://aikido-hdf.fr/wp-json/wp/v2/mec_location?per_page=50")
            val httpsURLConnection = url.openConnection() as HttpsURLConnection
            httpsURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = false
            // Check if the connection is successful
            val responseCode = httpsURLConnection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    // Convert raw JSON to pretty JSON using GSON library

                    val jsonArray = JSONTokener(response).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {

                        val id = jsonArray.getJSONObject(i).getString("id")
                        val name = jsonArray.getJSONObject(i).getString("name")
                        val count = jsonArray.getJSONObject(i).getString("count")
                        val address = jsonArray.getJSONObject(i).getString("address")
                        val model =
                            Places(
                                id, name, count, address
                            )

                        dao.insertAllPlaces(model)
                    }
                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())*/
            }
        }
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
