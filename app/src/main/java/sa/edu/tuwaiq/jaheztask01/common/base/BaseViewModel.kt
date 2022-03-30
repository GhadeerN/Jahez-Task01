package sa.edu.tuwaiq.jaheztask01.common.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import sa.edu.tuwaiq.jaheztask01.domain.model.BaseUIState
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    protected val _baseUIState = MutableSharedFlow<BaseUIState>()
    val baseUIState = _baseUIState.asSharedFlow()

}