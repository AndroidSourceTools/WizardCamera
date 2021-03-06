package com.shevelev.wizard_camera.main_activity.dto

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.shevelev.wizard_camera.common_entities.enums.FilterCode

data class FilterDisplayData(
    val code: FilterCode,
    @DrawableRes val icon: Int,
    @StringRes val title: Int
)