package com.bahadir.core.domain.source

import com.bahadir.core.domain.model.UsageStateUI

interface UsageStateDataSource {
    fun getUsageStatesTime(startTime: Long): List<UsageStateUI>

}