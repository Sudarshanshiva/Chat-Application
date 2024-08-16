package com.example.chatter.home

import androidx.lifecycle.ViewModel
import com.example.chatter.feature.model.Channel
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _channel = MutableStateFlow<List<Channel>>(emptyList())
    val channels = _channel.asStateFlow()

    init {
        getChannels()
    }

    private fun getChannels(){
        firebaseDatabase.getReference("channel").get().addOnSuccessListener {
            val list = mutableListOf<Channel>()
            it.children.forEach{ data->
                val channel = Channel(data.key!!,data.value.toString())
                list.add(channel)
            }
            _channel.value = list
        }
    }

    fun addChannel(name:String){
        val key = firebaseDatabase.getReference("channel").push().key
        firebaseDatabase.getReference("channel").child(key!!).setValue(name).addOnSuccessListener {
            getChannels()

        }
    }
}