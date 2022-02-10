package stages.aikidoliguehdf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit

class StagePlaces  : AppCompatActivity(R.layout.stage_places_activity) {


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stage_places_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<StagePlaceFragment>(R.id.fragmentPlacesContainerView)

            }
        }
    }


}