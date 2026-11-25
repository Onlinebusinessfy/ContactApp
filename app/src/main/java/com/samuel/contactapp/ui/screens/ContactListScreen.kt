package com.samuel.contactapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.samuel.contactapp.data.Contact
import com.samuel.contactapp.ui.components.ContactDialog
import com.samuel.contactapp.ui.components.ContactItem
import com.samuel.contactapp.viewModels.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    viewModel: ContactViewModel,
    onContactClick: (Contact) -> Unit,
){
    val contacts by viewModel.contacts.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    if(showAddDialog){
        ContactDialog(
            contact = null,
            onConfirm = {newContact ->
                // save the new contact to the database via the viewModel
                // viewModel launches a couroutine to insert in the contact into the database
                viewModel.saveContact(newContact)
                // close the dialog after saving the contact
                showAddDialog = false
            },
            onDismiss = {
                showAddDialog = false
            }
        )
    } // end the showAddDialog
    // Scaffold provides the Material Design layout structure with the floating action button
    // topBar: the header at the top
    // FAB floating action button the + button to add new contact
    Scaffold(
        topBar = {
            TopAppBar(
                // Title displayed in the center of the TopAppBar
                title = { Text(text = "Contacts") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }, // end of the topbar
        // FAB circular button in the circular in the bottom-right corner
        floatingActionButton = {
            FloatingActionButton(
                onClick = {showAddDialog = true},
                shape = CircleShape
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Contact")
            }
        }
    ) {
        paddingValues ->
        if(contacts.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ){
                Text("The contact list is empty")
            }
        } else {
            // show the list of contacts
            // LazyColumn only renders visible items on screen
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                // Add vertical spacing between items
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(contacts, key = {it.id}) {contact ->
                    // render each contact using the ContactItem component
                    ContactItem(
                        contact = contact,
                        onclick = {
                            onContactClick(contact)
                        },
                        onDelete = {
                            viewModel.deleteContact(contact)
                        },
                    )
                }
            }
        }
    }

}