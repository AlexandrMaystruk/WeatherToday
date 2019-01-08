package com.gmail.maystruks08.whatweathernow.presentation.presenter

import android.content.Context
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.dagger.background.BackgroundContract
import com.gmail.maystruks08.whatweathernow.presentation.base.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SelectBackgroundPresenter @Inject constructor(private var router: Router?) :
    BasePresenter(), BackgroundContract.Presenter {


    private var mView: BackgroundContract.View? = null

    override fun attach(view: BackgroundContract.View) {
        this.mView = view
    }

    override fun initUi(context: Context?) {

        compositeDisposable.add(
            Observable.just(generateImageList(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        mView?.showGridImage(it)
                    },
                    {
                        mView?.showError(it.localizedMessage)
                    })
        )
    }

    private fun generateImageList(context: Context?): ArrayList<BackgroundData> {

        val imageSource = ArrayList<BackgroundData>()
        imageSource.add(BackgroundData(R.drawable.clearly_sky, context?.getDrawable(R.drawable.clearly_sky_small)))
        imageSource.add(BackgroundData(R.drawable.clearly_sky2, context?.getDrawable(R.drawable.clearly_sky2_small)))
        imageSource.add(BackgroundData(R.drawable.cloudy, context?.getDrawable(R.drawable.cloudy_small)))
        imageSource.add(BackgroundData(R.drawable.cloudy2, context?.getDrawable(R.drawable.cloudy2_small)))
        imageSource.add(BackgroundData(R.drawable.cloudy3, context?.getDrawable(R.drawable.cloudy3_small)))
        imageSource.add(BackgroundData(R.drawable.default_back, context?.getDrawable(R.drawable.default_back_small)))
        imageSource.add(BackgroundData(R.drawable.light_snow, context?.getDrawable(R.drawable.light_snow_small)))
        imageSource.add(BackgroundData(R.drawable.light_snow2, context?.getDrawable(R.drawable.light_snow2_small)))
        imageSource.add(BackgroundData(R.drawable.smoke, context?.getDrawable(R.drawable.smoke_small)))
        imageSource.add(BackgroundData(R.drawable.smoke2, context?.getDrawable(R.drawable.smoke2_small)))
        imageSource.add(BackgroundData(R.drawable.snow, context?.getDrawable(R.drawable.snow_small)))
        imageSource.add(BackgroundData(R.drawable.suny, context?.getDrawable(R.drawable.suny_small)))
        imageSource.add(BackgroundData(R.drawable.suny2, context?.getDrawable(R.drawable.suny2_small)))
        imageSource.add(BackgroundData(R.drawable.suny3, context?.getDrawable(R.drawable.suny3_small)))

        return imageSource
    }

    override fun backgroundSelected() {
        router?.exit()
    }

    override fun detach() {
        compositeDisposable.dispose()
    }

}

