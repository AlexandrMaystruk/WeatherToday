package com.gmail.maystruks08.whatweathernow.ui.settings

import android.content.Context
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.ui.base.BasePresenterImpl
import com.gmail.maystruks08.whatweathernow.ui.settings.adapter.BackgroundData
import javax.inject.Inject

class SelectBackgroundPresenterImpl @Inject constructor(
    private val context: Context
) : BasePresenterImpl<BackgroundContract.View>(),
    BackgroundContract.Presenter {


    override fun initUi() {
        kotlin
            .runCatching { generateImageList(context) }
            .onSuccess {
                view.showGridImage(it)
            }
            .onFailure {
//               view.showError(it.localizedMessage.orEmpty())
            }
    }

    private fun generateImageList(context: Context?): List<BackgroundData> {
        return ArrayList<BackgroundData>().apply {
         add(BackgroundData(R.drawable.clearly_sky, context?.getDrawable(R.drawable.clearly_sky_small)))
         add(BackgroundData(R.drawable.clearly_sky2, context?.getDrawable(R.drawable.clearly_sky2_small)))
         add(BackgroundData(R.drawable.cloudy, context?.getDrawable(R.drawable.cloudy_small)))
         add(BackgroundData(R.drawable.cloudy2, context?.getDrawable(R.drawable.cloudy2_small)))
         add(BackgroundData(R.drawable.cloudy3, context?.getDrawable(R.drawable.cloudy3_small)))
         add(BackgroundData(R.drawable.default_back, context?.getDrawable(R.drawable.default_back_small)))
         add(BackgroundData(R.drawable.light_snow, context?.getDrawable(R.drawable.light_snow_small)))
         add(BackgroundData(R.drawable.light_snow2, context?.getDrawable(R.drawable.light_snow2_small)))
         add(BackgroundData(R.drawable.smoke, context?.getDrawable(R.drawable.smoke_small)))
         add(BackgroundData(R.drawable.smoke2, context?.getDrawable(R.drawable.smoke2_small)))
         add(BackgroundData(R.drawable.snow, context?.getDrawable(R.drawable.snow_small)))
         add(BackgroundData(R.drawable.suny, context?.getDrawable(R.drawable.suny_small)))
         add(BackgroundData(R.drawable.suny2, context?.getDrawable(R.drawable.suny2_small)))
         add(BackgroundData(R.drawable.suny3, context?.getDrawable(R.drawable.suny3_small)))
        }
    }

    override fun backgroundSelected() {
       view.navigateBack()
    }

}

