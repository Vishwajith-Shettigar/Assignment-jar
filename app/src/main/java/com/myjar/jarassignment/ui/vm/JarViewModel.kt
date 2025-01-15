package com.myjar.jarassignment.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JarViewModel : ViewModel() {

  private val _filerListStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
  val filerListStringData: StateFlow<List<ComputerItem>>
    get() = _filerListStringData

  private var _listStringData = listOf<ComputerItem>()

  private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

  fun fetchData() {
    viewModelScope.launch {
    repository.fetchResults().collect {
        _filerListStringData.value = it
        _listStringData = it
      }
    }
  }

  fun getSearchResult(query: String) {
    if (query.isEmpty()) {
      _filerListStringData.value = _listStringData
      return
    }

    val filteredData = filterData(query)
    _filerListStringData.value = filteredData
  }

  fun filterData(query: String): List<ComputerItem> {
    return _listStringData.filter {
      it.name.contains(query)
    }.toList()
  }
}
