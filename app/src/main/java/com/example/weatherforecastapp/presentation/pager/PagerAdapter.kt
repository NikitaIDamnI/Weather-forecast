import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.presentation.activity.fragments.WeatherFragment

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private var sizePager = 0

    override fun getItemCount(): Int = sizePager

    override fun createFragment(position: Int): Fragment {
        return WeatherFragment().apply {
            arguments = getArgs(position)
        }
    }

    private fun getArgs(position: Int): Bundle {
        return Bundle().apply {
            putInt("position", position)

        }
    }

    fun submitList(newCities: List<City>) {
        val newSizePage = newCities.size
        if (newSizePage > sizePager) {
            sizePager = newSizePage
            notifyDataSetChanged()
        }
    }

}
