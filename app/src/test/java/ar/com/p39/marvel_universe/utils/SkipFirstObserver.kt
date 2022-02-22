package ar.com.p39.marvel_universe.utils

import androidx.lifecycle.*

class SkipFirstObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
    private val lifecycle = LifecycleRegistry(this)
    private var skip = true

    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun onChanged(t: T) {
        if (skip) {
            skip = false
        } else {
            handler(t)
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }
    }
}
fun <T> LiveData<T>.skipFirstAndObserveOnce(onChangeHandler: (T) -> Unit) {
    val observer = SkipFirstObserver(handler = onChangeHandler)
    observe(observer, observer)
}