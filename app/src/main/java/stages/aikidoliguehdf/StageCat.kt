package stages.aikidoliguehdf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit

class StageCat  : AppCompatActivity(R.layout.stage_cat_activity) {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stage_cat_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<StageCatFragment<Any?>>(R.id.fragmentCatContainerView)
            }
        }
    }
}