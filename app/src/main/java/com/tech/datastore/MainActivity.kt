package com.tech.datastore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tech.datastore.ui.theme.DataStoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var userDetails: UserDetails

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetails = UserDetails(this)
        setContent {
            DataStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CoroutineScope(IO).launch {
                        userDetails.storeUserData("Aman Kr", 22)
                    }

                    lifecycleScope.launch {
                        lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                            userDetails.getAge().collect { age ->
                                Log.i("@@datastore", "onCreate: $age")
                            }
                        }
                    }
                    lifecycleScope.launch {
                        lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                            userDetails.getName().collect { name ->
                                Log.i("@@datastore", "onCreate: $name")
                            }
                        }
                    }
                }
            }
        }
    }
}