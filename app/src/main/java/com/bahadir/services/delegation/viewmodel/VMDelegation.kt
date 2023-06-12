package com.bahadir.services.delegation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow

interface VMDelegation<EFFECT : com.bahadir.services.base.Effect, EVENT : com.bahadir.services.base.Event, STATE : com.bahadir.services.base.State> {
    fun viewModel(viewModel: ViewModel)
    fun setEffect(effect: EFFECT)
    fun setEvent(event: EVENT)
    fun setState(state: STATE)

    val effect: SharedFlow<EFFECT>
    val event: SharedFlow<EVENT>
    val state: SharedFlow<STATE>

}