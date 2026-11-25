package com.samuel.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samuel.contactapp.data.Contact
import com.samuel.contactapp.ui.screens.ContactListScreen
import com.samuel.contactapp.ui.theme.ContactAppTheme
import com.samuel.contactapp.viewModels.ContactViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel : ContactViewModel = viewModel()
            // This variable will hold the selected contact, and the entire navigation
            // selectedContact = null -> show the list screen
            val selectedContact = remember { mutableStateOf<Contact?>(null) }
            ContactAppTheme {
                if (selectedContact.value == null){
                    // Show the list screen-user is browsing the list of contacts
                    ContactListScreen(
                        viewModel = viewModel,
                        // Callback when the user taps a contact of the list
                        // Change the selectedContact variable to show the detail screen
                        onContactClick = {contact ->
                            selectedContact.value = contact
                        }
                    )
                }
            }
        }
    }
}