# 11. Enregistrer l'état

> **Bloc 3** · Étapes 10 et 11 : `LazyColumn` et `rememberSaveable`

Notre application a deux problèmes.

## Enregistrer l'état de l'écran d'accueil

Lance l'app **sur Android**, clique sur « Continue », puis fais pivoter l'écran (bouton de rotation dans la barre de l'émulateur) : **l'écran d'accueil réapparaît**.

`remember` ne fonctionne que tant que le composable reste dans la composition. Quand tu fais pivoter un téléphone Android, toute l'activité est recréée, et l'état est perdu. C'est pareil lors d'un changement de configuration (passage en mode sombre, changement de langue…) ou quand le système arrête l'application en arrière-plan pour libérer de la mémoire.

Au lieu de `remember`, utilise **`rememberSaveable`**. Il enregistre l'état pour qu'il survive aux changements de configuration (comme la rotation) et à l'arrêt du processus.

Remplace `remember` par `rememberSaveable` pour `shouldShowOnboarding` :

```kotlin
import androidx.compose.runtime.saveable.rememberSaveable
// ...

var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
```

Relance l'app, clique sur « Continue » et fais pivoter l'écran : cette fois, l'écran d'accueil ne réapparaît pas.

> [!NOTE]
> Sur Desktop, redimensionner la fenêtre ne recrée pas l'application : tu ne verras pas le problème. C'est un bon exemple de comportement **propre à une plateforme**, que Compose Multiplatform gère pour toi avec le même code.

## Enregistrer l'état des éléments de la liste

Cette fois, tu peux tester sur Desktop. Agrandis un élément de la liste, fais défiler jusqu'à ce qu'il sorte de l'écran, puis reviens-y : **il a retrouvé sa taille initiale**.

C'est normal : pour économiser la mémoire, la `LazyColumn` retire de la composition les éléments qui ne sont plus visibles, et leur état `remember` est perdu.

Pour régler ce problème, utilise aussi `rememberSaveable` pour l'état `expanded` :

```kotlin
var expanded by rememberSaveable { mutableStateOf(false) }
```

Refais le test : l'élément reste agrandi.

En à peine plus de 100 lignes de code, tu affiches une longue liste performante, qui défile, et dont chaque élément a son propre état. Et comme tu peux le constater si ton système est en mode sombre, **ton application a déjà un mode sombre fonctionnel**, sans une ligne de code en plus. On verra comment personnaliser le thème un peu plus loin.

---

## 🏁 Fin du bloc 3

L'atelier « de base » est terminé : tu as vu tous les concepts essentiels de Compose. Les étapes suivantes sont des **bonus** pour aller plus loin.

Si tu es en retard ou bloqué, récupère la correction du bloc :

```bash
git stash
git checkout tp1-bloc3
```

<details>
<summary>📄 Code complet de <code>App.kt</code> à la fin du bloc 3</summary>

```kotlin
package com.bvic.followmycrypto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked,
        ) {
            Text("Continue")
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" },
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val extraPadding = if (expanded) 48.dp else 0.dp

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding),
            ) {
                Text(text = "Hello, ")
                Text(text = name)
            }
            ElevatedButton(
                onClick = { expanded = !expanded },
            ) {
                Text(if (expanded) "Show less" else "Show more")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingsPreview() {
    MaterialTheme {
        Greetings()
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    MaterialTheme {
        OnboardingScreen(onContinueClicked = {}) // Ne fait rien au clic
    }
}

@Preview
@Composable
fun MyAppPreview() {
    MaterialTheme {
        MyApp(Modifier.fillMaxSize())
    }
}
```

</details>

---

[⬅️ Étape précédente](10-liste-performante.md) · [📚 Sommaire](README.md) · [Étape suivante : Animer ta liste ➡️](12-animer-ta-liste.md)
