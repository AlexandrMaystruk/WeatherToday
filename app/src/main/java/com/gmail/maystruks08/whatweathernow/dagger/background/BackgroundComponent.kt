package com.gmail.maystruks08.whatweathernow.dagger.background

import com.gmail.maystruks08.whatweathernow.ui.fragments.SelectBackgroundFragment
import dagger.Subcomponent

@Subcomponent(modules = [BackgroundModule::class])
@BackgroundScope
interface BackgroundComponent {

    fun inject(selectBackgroundFragment: SelectBackgroundFragment)

}