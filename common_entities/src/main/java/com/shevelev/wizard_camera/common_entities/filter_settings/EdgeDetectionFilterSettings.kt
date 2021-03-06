package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

data class EdgeDetectionFilterSettings(
    override val code: FilterCode = FilterCode.EDGE_DETECTION,
    val isInverted: Boolean
): FilterSettings