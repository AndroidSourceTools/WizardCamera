package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

interface FilterSettingsWidget {
    val title: Int

    fun init(startSettings: FilterSettings)

    fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?)
}