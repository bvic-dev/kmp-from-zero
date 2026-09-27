# 4. Premiers pas avec Compose

> **Bloc 1** · Étapes 4 à 7 : composables, `Modifier`, `Row` / `Column`

Dans cette étape, tu vas remplacer le code de départ par ton premier composable et découvrir les briques de base de Compose.

## Faire place nette

Supprime les fichiers `Greeting.kt` et `GreetingUtil.kt` de `shared/src/commonMain/kotlin/com/bvic/followmycrypto/`. Ils servaient à la démo de départ.

> [!NOTE]
> Garde `Platform.kt` et ses versions `actual` : ils ne gênent pas, et c'est un bon exemple d'`expect`/`actual` à garder sous le coude.

Remplace ensuite **tout le contenu** de `App.kt` par :

```kotlin
package com.bvic.followmycrypto

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true, name = "Text preview")
@Composable
fun GreetingPreview() {
    MaterialTheme {
        Greeting(name = "Android")
    }
}
```

Lance l'app sur Desktop : tu dois voir « Hello Android! » en haut à gauche de la fenêtre.

## Fonctions composables

Une fonction composable est une fonction standard annotée avec `@Composable`. Cette annotation permet à ta fonction d'appeler d'autres fonctions `@Composable`. Comme tu peux le constater, la fonction `Greeting` est annotée avec `@Composable`. Elle génère une partie de la hiérarchie de l'interface qui affiche le texte reçu en paramètre, `name`. `Text` est une fonction composable fournie par la bibliothèque.

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}
```

> [!NOTE]
> Les fonctions composables sont des fonctions Kotlin annotées avec `@Composable`. Par convention, leur nom commence par une majuscule, comme une classe, parce qu'elles décrivent un élément d'interface.

## Compose dans une application multiplateforme

Sur chaque plateforme, une coquille appelle notre fonction `App()` (voir l'[étape 2](02-kotlin-multiplatform.md)). Sur Android par exemple, `MainActivity` est lancée lorsque l'utilisateur ouvre l'application, et elle appelle `setContent { App() }`. On ne décrit pas l'interface dans un fichier XML, comme on le faisait avant Compose : on appelle simplement des fonctions composables.

Dans `App()` :

- **`MaterialTheme`** définit le style (couleurs, polices, formes) des composables qu'il contient. On créera notre propre thème à l'[étape 13](13-style-et-theme.md).
- **`Scaffold`** (« échafaudage ») fournit la structure de base d'un écran Material Design : fond, barre du haut, bouton flottant… Il calcule aussi `innerPadding`, les marges à appliquer pour que ton contenu ne passe pas sous la barre d'état ou l'encoche d'un téléphone.

> [!NOTE]
> Le paramètre `modifier: Modifier = Modifier` de `Greeting` a une **valeur par défaut** : `Modifier`, un modificateur vide. On peut donc appeler `Greeting("Android")` sans le préciser. On revient sur les modificateurs à l'étape suivante.

## L'aperçu (Preview)

Pour voir à quoi ressemble un composable, tu peux lancer l'application, ou utiliser **l'aperçu d'Android Studio**.

Pour cela, il suffit d'annoter avec `@Preview` une fonction composable sans paramètre (ou dont tous les paramètres ont une valeur par défaut), puis de compiler le projet. Tu peux afficher plusieurs aperçus dans un même fichier et leur donner un nom :

```kotlin
@Preview(showBackground = true, name = "Text preview")
@Composable
fun GreetingPreview() {
    MaterialTheme {
        Greeting(name = "Android")
    }
}
```

L'aperçu ne s'affiche pas si le mode **Code** est sélectionné en haut à droite de l'éditeur. Clique sur **Split** (diviser) pour afficher le code et l'aperçu côte à côte.

![Aperçu dans Android Studio](img/google-fb011e374b98ccff.png)

> [!TIP]
> Si l'aperçu ne s'affiche pas ou reste bloqué, pas de panique : travaille avec l'app Desktop et son **hot reload** 🔥. Enregistre ton fichier et la fenêtre se met à jour toute seule.

> [!IMPORTANT]
> Quand Android Studio te propose plusieurs imports pour une même classe, choisis ceux qui commencent par :
>
> - `androidx.compose.*` pour le compilateur et le runtime de Compose ;
> - `androidx.compose.ui.*` pour le kit d'interface ;
> - `androidx.compose.material3.*` pour les composants Material Design.
>
> Oui, même en multiplateforme, les packages s'appellent `androidx.compose` : c'est le même code que sur Android.

---

[⬅️ Étape précédente](03-lancer-l-application.md) · [📚 Sommaire](README.md) · [Étape suivante : Modifier l'UI ➡️](05-modifier-l-ui.md)
