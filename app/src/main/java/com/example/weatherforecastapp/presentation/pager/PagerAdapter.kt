
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherforecastapp.presentation.activity.fragments.WeatherFragment

class PagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val idLocation: Int,
    private val argsList: List<Bundle>
) : FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        Log.d("PagerAdapter_Log", "getItemCount:${argsList.size} ")
        return argsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return WeatherFragment().apply {
            arguments = argsList[position]
        }
    }


}