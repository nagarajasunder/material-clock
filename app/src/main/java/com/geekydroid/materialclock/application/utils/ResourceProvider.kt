package com.geekydroid.materialclock.application.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(
   @ApplicationContext private val context:Context
) {

    fun getString(@StringRes id:Int, vararg args:Any) : String {
        return context.getString(id,*args)
    }

}