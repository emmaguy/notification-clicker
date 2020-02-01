package com.emmaguy.notificationclicker.core

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<S : Any>(initialState: S) : ViewModel() {
    private val _viewState = MutableLiveData<S>().apply {
        value = initialState
    }
    val viewState: LiveData<S> = _viewState

    @MainThread
    protected fun setState(reducer: S.() -> S) {
        val currentState = _viewState.value!!
        val newState = currentState.reducer()
        if (newState != currentState) {
            _viewState.value = newState
        }
    }
}