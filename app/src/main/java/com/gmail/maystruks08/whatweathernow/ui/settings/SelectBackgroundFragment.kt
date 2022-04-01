package com.gmail.maystruks08.whatweathernow.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.dagger.background.BackgroundContract
import com.gmail.maystruks08.whatweathernow.ui.base.BaseFragment
import com.gmail.maystruks08.whatweathernow.ui.settings.adapter.BackgroundData
import com.gmail.maystruks08.whatweathernow.ui.settings.adapter.BackgroundRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_select_background.*
import javax.inject.Inject

class SelectBackgroundFragment : BaseFragment(), BackgroundContract.View {

    @Inject
    lateinit var mSelectBackgroundPresenter: SelectBackgroundPresenterImpl
    private lateinit var mAdapter: BackgroundRecyclerAdapter

    override fun provideLayoutRes(): Int {
        return R.layout.fragment_select_background
    }

    companion object {
        fun newInstance(): SelectBackgroundFragment {
            val fragment = SelectBackgroundFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun initPresenter() {
        WeatherApplication.component.provideBackgroundComponent().inject(this)
        mSelectBackgroundPresenter.attach(this)
    }

    override fun initViews() {
        activity()?.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        activity()?.showOptionMenu(false)

        recyclerBackgroundImage.layoutManager = GridLayoutManager(context, 2)
        mSelectBackgroundPresenter.initUi()

    }

    override fun showGridImage(listImage: List<BackgroundData>) {
        mAdapter = BackgroundRecyclerAdapter(listImage){
            activity()?.setBackground(it)
            //Save to local settings background image id
            val pref = context?.getSharedPreferences("BACKGROUND_IMAGE", Context.MODE_PRIVATE)
            val ed = pref?.edit()
            ed?.putString("BACKGROUND_IMAGE", it.toString())
            ed?.apply()
            mSelectBackgroundPresenter.backgroundSelected()
        }
        recyclerBackgroundImage.adapter = mAdapter
    }

    override fun navigateBack() {

    }

}
