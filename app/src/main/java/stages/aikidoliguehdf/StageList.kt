package stages.aikidoliguehdf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit

class StageList : AppCompatActivity(R.layout.stage_list_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stage_list_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<StageListFragment<Any?>>(R.id.fragmentListContainerView)
            }
        }
    }
}