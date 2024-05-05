package stages.aikidoliguehdf

import android.annotation.SuppressLint
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
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONTokener
import stages.aikidoliguehdf.data.*
import java.net.URL
import java.time.LocalDate
import javax.net.ssl.HttpsURLConnection

@DelicateCoroutinesApi
@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    private lateinit var dao: StagesDao
    lateinit var db: StagesRoomDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /*connection to DB*/
        db = StagesRoomDatabase.getInstance(this)
        dao = db.stagesDao()

        /*fetch data if network OK*/
        checkForInternet(this)
        Log.i("check", checkForInternet(this).toString())
        if(checkForInternet(this)){
            parseJSON()
            parseJSONLocs()
        }else{
            val toast = Toast.makeText(this,"Pas de connexion réseau",Toast.LENGTH_SHORT)
            toast.show()
        }


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }


    @DelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.O)
    /*JSON with list of courses and list of categories*/
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

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    //Delete all entry before uploading JSON (refresh db)
                    dao.deleteAll()

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


                        dao.insertAll(model)
                        val current = LocalDate.now().minusMonths(1)
                        dao.deleteAllxMonths(current.toString())
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
                        val count = dao.countcat(id)

                        val modelforCat =
                            Categories(
                                id, name, count
                            )

                        dao.insertCategory(modelforCat)

                        val listcat = mutableListOf(id.toLong())

                        for (item in listcat) {
                            val items = id.toString()
                            val test = db.stagesDao().getByCat(items)

                            for (value in test) {
                                val idstages = value.idstages.toString()
                                val newList = mutableListOf<StagesCatMap>()

                                repeat(listcat.size) {
                                    newList += StagesCatMap(idstages, items)
                                }
                                dao.insertAllStagesCatMap(newList)

                            }
                        }
                    }
                }
            }
        }
    }



    /*JSON with list of locations*/
    private fun parseJSONLocs() {

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
                        val count = dao.countplaces(id)
                        val address = jsonArray.getJSONObject(i).getString("address")
                        val model =
                            Places(
                                id, name, count, address
                            )

                        dao.insertAllPlaces(model)
                    }
                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
    }

    private fun checkForInternet(context: Context): Boolean {

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
