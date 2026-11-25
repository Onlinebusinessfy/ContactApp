package com.samuel.contactapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samuel.contactapp.data.Contact
import com.samuel.contactapp.data.ContactDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactViewModel (application: Application): AndroidViewModel (application){
    // Get the ContactDao from the ContactDatabase
    // This is the only place in the app that needs to know about the database
    private val _dao = ContactDatabase.getDatabase(application).contactDao()

    // Expose all contacts as StateFlow
    // stateIn() converts the Flow into a StateFlow
    // scope is the coroutine scope for the flow
    // stated : start collecting when someone listens, stops collectin when no one is listening
    // initialValue is the first emitted by the StateFlow
    val contacts: StateFlow<List<Contact>> = _dao.getAllContacts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    // Save a new contact to the database
    // viewModelScope start a coroutine in the background thread
    fun saveContact(contact: Contact){
        viewModelScope.launch {
            _dao.insertContact(contact)
        }
    }

    // Challenge Delete and Update contact
    fun updateContact(contact: Contact){
        viewModelScope.launch {
            _dao.updateContact(contact)
        }
    }

    fun deleteContact(contact: Contact){
        viewModelScope.launch {
            _dao.deleteContact(contact)
        }
    }
}