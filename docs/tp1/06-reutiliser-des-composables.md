# 6. Réutiliser des composables

> **Bloc 1** · Étapes 4 à 7 : composables, `Modifier`, `Row` / `Column`

Plus tu ajoutes de composants à l'interface, plus tu crées de niveaux d'imbrication, et plus une fonction devient difficile à lire. Créer de petits composants réutilisables permet de construire facilement une bibliothèque d'éléments d'interface pour ton application. Chaque composable est responsable d'une partie de l'écran et peut être modifié indépendamment.

> [!TIP]
> **Bonne pratique :** ajoute toujours à tes composables un paramètre `modifier: Modifier = Modifier`, et transmets-le au **premier** composable que tu appelles dans ta fonction. Ainsi, celui qui appelle ton composable peut adapter sa mise en page (taille, marges…) depuis l'extérieur.

Crée un composable appelé `MyApp` qui contient le message d'accueil :

```kotlin
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        Greeting("Android")
    }
}
```

Tu peux maintenant simplifier `App()` et l'aperçu en réutilisant `MyApp`, ce qui évite de dupliquer du code. Dans l'aperçu, appelle `MyApp` et supprime le nom de l'aperçu.

Ton fichier `App.kt` doit ressembler à ceci :

```kotlin
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
        Scaffold { innerPadding ->
            MyApp(modifier = Modifier.padding(innerPadding).fillMaxSize())
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        Greeting("Android")
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        MyApp()
    }
}
```

> [!NOTE]
> Regarde la ligne `Modifier.padding(innerPadding).fillMaxSize()` dans `App()` : on **enchaîne** deux modificateurs. `MyApp` prend d'abord les marges du `Scaffold`, puis remplit tout l'espace restant.

---

[⬅️ Étape précédente](05-modifier-l-ui.md) · [📚 Sommaire](README.md) · [Étape suivante : Créer des lignes et des colonnes ➡️](07-lignes-et-colonnes.md)
