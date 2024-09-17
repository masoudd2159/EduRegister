package ir.masouddabbaghi.eduregister.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.masouddabbaghi.eduregister.data.model.HomeList
import ir.masouddabbaghi.eduregister.data.model.Slider
import ir.masouddabbaghi.eduregister.data.repository.HomeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
    @Inject
    constructor(
        private val homeRepository: HomeRepository,
    ) : ViewModel() {
        private val _homeListResponse = MutableLiveData<HomeList>()
        val homeListResponse: LiveData<HomeList> = _homeListResponse

        private val _sliderResponse = MutableLiveData<Slider>()
        val sliderResponse: LiveData<Slider> = _sliderResponse

        fun fetchHomeList() =
            viewModelScope.launch {
                _homeListResponse.value = homeRepository.homeList()
            }

        fun fetchSlider() =
            viewModelScope.launch {
                _sliderResponse.value = homeRepository.slider()
            }
    }
