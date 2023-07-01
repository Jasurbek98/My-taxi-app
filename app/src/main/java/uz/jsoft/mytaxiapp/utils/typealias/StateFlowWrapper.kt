package uz.jsoft.mytaxiapp.utils.`typealias`

import uz.jsoft.mytaxiapp.data.local_models.dashboard.DataWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 *Created by Jasurbek Kurganbayev on 10/27/21 11:48 PM
 *
 **/
typealias StateFlowWrapper<T> = StateFlow<DataWrapper<T>>
typealias MutableStateFlowWrapper<T> = MutableStateFlow<DataWrapper<T>>

@Suppress("FunctionName")
fun <T> MutableStateFlowWrapper(value: DataWrapper<T> = DataWrapper.Empty()) =
    MutableStateFlow(value)