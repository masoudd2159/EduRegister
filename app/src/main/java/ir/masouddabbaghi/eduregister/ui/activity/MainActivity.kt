package ir.masouddabbaghi.eduregister.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.masouddabbaghi.eduregister.data.model.Slider
import ir.masouddabbaghi.eduregister.databinding.ActivityMainBinding
import ir.masouddabbaghi.eduregister.ui.adapter.HomeListAdapter
import ir.masouddabbaghi.eduregister.ui.adapter.SliderAdapter
import ir.masouddabbaghi.eduregister.ui.base.BaseActivity
import ir.masouddabbaghi.eduregister.ui.viewmodel.MainActivityViewModel
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var homeListAdapter: HomeListAdapter

    @Inject
    lateinit var sliderAdapter: SliderAdapter

    private val delay: Long = 8_000L
    private var currentPage = 0
    private var timer: Timer? = null

    override fun getLayoutResourceBinding(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel.fetchSlider()
        mainActivityViewModel.fetchHomeList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            mainActivityViewModel.homeListResponse.observe(this@MainActivity) { homeListResult ->
                recyclerViewProfile.setHasFixedSize(true)
                recyclerViewProfile.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                recyclerViewProfile.adapter = homeListAdapter
                homeListAdapter.setItemClickInterface { homeListItem ->
                    when (homeListItem.id) {
                        1 -> {}
                        2 -> {}
                        3 -> {}
                        4 -> {}
                        else -> Toast.makeText(this@MainActivity, homeListItem.title, Toast.LENGTH_SHORT).show()
                    }
                }
                homeListAdapter.updateItems(homeListResult.result)
            }

            mainActivityViewModel.sliderResponse.observe(this@MainActivity) { sliderResult ->
                sliderAdapter.updateItems(sliderResult.result)
                viewPager.adapter = sliderAdapter
                dotsIndicator.attachTo(viewPager)
                startAutoSlide(sliderResult.result)
            }
        }
    }

    private fun startAutoSlide(sliderModel: List<Slider.Result>) {
        Log.i(tagLog, "Start Auto Slide")
        val handler = Handler(Looper.getMainLooper())

        timer = Timer()
        timer?.schedule(
            object : TimerTask() {
                override fun run() {
                    handler.post {
                        currentPage = if (currentPage == sliderModel.size - 1) 0 else currentPage + 1
                        binding.viewPager.setCurrentItem(currentPage, true)
                    }
                }
            },
            delay,
            delay,
        )
    }
}
