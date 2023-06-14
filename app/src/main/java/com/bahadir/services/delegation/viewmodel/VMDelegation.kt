package com.bahadir.services.delegation.viewmodel

import androidx.lifecycle.ViewModel
import com.bahadir.services.base.Effect
import com.bahadir.services.base.Event
import com.bahadir.services.base.State
import kotlinx.coroutines.flow.SharedFlow

interface VMDelegation<EFFECT : Effect, EVENT : Event, STATE : State> {
    fun viewModel(viewModel: ViewModel)
    fun setEffect(effect: EFFECT)
    fun setEvent(event: EVENT)
    fun setState(state: STATE)

    fun getCurrentState(): STATE

    val effect: SharedFlow<EFFECT>
    val event: SharedFlow<EVENT>
    val state: SharedFlow<STATE>
}