
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherforecastapp.presentation.activity.fragments.WeatherFragment

class PagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val argsList: List<Bundle>
) : FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return argsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return WeatherFragment().apply {
            arguments = argsList[position]
        }
    }


}