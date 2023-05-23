package com.bahadir.core.delegation.viewmodel

import androidx.lifecycle.ViewModel
import com.bahadir.core.base.Effect
import com.bahadir.core.base.Event
import com.bahadir.core.base.State
import kotlinx.coroutines.flow.SharedFlow

interface VMDelegation<EFFECT : Effect, EVENT : Event, STATE : State> {
    fun viewModel(viewModel: ViewModel)
    fun setEffect(effect: EFFECT)
    fun setEvent(event: EVENT)
    fun setState(state: STATE)

    val effect: SharedFlow<EFFECT>
    val event: SharedFlow<EVENT>
    val state: SharedFlow<STATE>

}