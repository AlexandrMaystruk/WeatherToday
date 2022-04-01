package com.gmail.maystruks08.whatweathernow.ui.editlocation

import com.gmail.maystruks08.whatweathernow.dagger.city.CityContract
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.ui.base.BasePresenterImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityPresenterImpl @Inject constructor(
    private val localeStorage: LocaleStorage
) : BasePresenterImpl<CityContract.View>(), CityContract.Presenter {

    override fun initUi() {
        presenterCoroutineScope.launch {
            localeStorage.getLocationSettingsConfig()
                .collect { shouldUseGeoLocation ->
                    withContext(Dispatchers.Main) {
                        view.setSwitchState(shouldUseGeoLocation)
                    }
                }
        }
    }

    override fun onUseGeoLocationSettingsChanged(shouldUseGeoLocation: Boolean) {
        presenterCoroutineScope.launch {
            localeStorage.saveLocationSettingsConfig(shouldUseGeoLocation)
        }
    }
}
