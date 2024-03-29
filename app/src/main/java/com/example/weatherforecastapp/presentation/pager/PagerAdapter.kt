import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherforecastapp.presentation.activity.WeatherFragment

class PagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val itemCount: Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun createFragment(position: Int): Fragment {
        return WeatherFragment.newInstance(position)
    }

}