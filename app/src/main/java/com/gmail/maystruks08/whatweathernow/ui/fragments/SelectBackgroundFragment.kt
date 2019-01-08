package com.gmail.maystruks08.whatweathernow.ui.fragments

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.dagger.background.BackgroundContract
import com.gmail.maystruks08.whatweathernow.presentation.presenter.BackgroundData
import com.gmail.maystruks08.whatweathernow.presentation.presenter.SelectBackgroundPresenter
import com.gmail.maystruks08.whatweathernow.ui.adapter.BackgroundRecyclerAdapter
import com.gmail.maystruks08.whatweathernow.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_select_background.*
import javax.inject.Inject

class SelectBackgroundFragment : BaseFragment(), BackgroundContract.View {

    @Inject
    lateinit var mSelectBackgroundPresenter: SelectBackgroundPresenter
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
        mSelectBackgroundPresenter.initUi(context)

    }

    override fun showGridImage(listImage: ArrayList<BackgroundData>) {
        mAdapter = BackgroundRecyclerAdapter(listImage)
        recyclerBackgroundImage.adapter = mAdapter

        compositeDisposable(mAdapter.subscribeItemClick {
            activity()?.setBackground(it)
            //Save to local settings background image id
            val pref = context?.getSharedPreferences("BACKGROUND_IMAGE", Context.MODE_PRIVATE)
            val ed = pref?.edit()
            ed?.putString("BACKGROUND_IMAGE", it.toString())
            ed?.apply()
            mSelectBackgroundPresenter.backgroundSelected()

        })
    }


    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

}
