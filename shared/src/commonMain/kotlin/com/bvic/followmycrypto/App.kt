package com.bvic.followmycrypto

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        // Scaffold fournit la structure de base d'un écran Material Design
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // innerPadding contient les marges à appliquer pour que le contenu
            // ne soit pas caché par la barre d'état, l'encoche ou la barre de navigation
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(24.dp),
        )
    }
}

@Preview(showBackground = true, name = "Text preview")
@Composable
fun GreetingPreview() {
    MaterialTheme {
        Greeting(name = "Android")
    }
}
