package com.bahadir.services.ui.activity

import com.bahadir.core.common.ServiceName
import com.bahadir.services.base.State

data class ActivityUIState(
    var isLoading: Boolean = false,
    val serviceState: String? = null,
    val serviceName: ServiceName? = null,
    val soundState: String? = null
) : State