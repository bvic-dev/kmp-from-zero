# 2. L'écran et sa barre de titre

> **Bloc 1** · Étapes 2 à 4 : construire l'interface, avec de fausses données
>
> 🧱 **Composable** → ViewModel → Repository → API

Presque tous les écrans d'une application ont la même structure : une barre en haut, un contenu, parfois une barre en bas ou un bouton flottant. Material fournit un composable qui pose cette structure pour toi : le `Scaffold` (« échafaudage »).

## 🎯 Résultat attendu

![Un écran vide avec sa barre de titre](img/01-scaffold.png)

## 📏 Contraintes

- L'écran est un composable `CryptoListScreen`, dans un nouveau fichier `ui/list/CryptoListScreen.kt`.
- Il a un paramètre `modifier`, comme tous tes composables depuis le TP 1.
- Le titre est centré dans la barre.
- `App()` ne fait plus qu'une chose : appliquer le thème et afficher `CryptoListScreen`.
- Le contenu reste vide pour l'instant.

## 📚 Documentation

- [Scaffold](https://developer.android.com/develop/ui/compose/components/scaffold)
- [Les barres d'application (*app bars*)](https://developer.android.com/develop/ui/compose/components/app-bars)

## 💡 Indices

<details>
<summary>Indice 1 : quels composables chercher ?</summary>

`Scaffold` a un paramètre `topBar`. Material 3 propose plusieurs barres : regarde du côté de `TopAppBar` et de ses variantes dans l'autocomplétion.

</details>

<details>
<summary>Indice 2 : la solution</summary>

`ui/list/CryptoListScreen.kt` :

```kotlin
package com.bvic.followmycrypto.ui.list

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CryptoListScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FollowMyCrypto", fontWeight = FontWeight.Bold) },
            )
        },
    ) { innerPadding ->
        // Le contenu arrive à l'étape suivante
    }
}
```

`App.kt` :

```kotlin
package com.bvic.followmycrypto

import androidx.compose.runtime.Composable
import com.bvic.followmycrypto.theme.FollowMyCryptoTheme
import com.bvic.followmycrypto.ui.list.CryptoListScreen

@Composable
fun App() {
    FollowMyCryptoTheme {
        CryptoListScreen()
    }
}
```

</details>

## 🤔 À quoi sert `innerPadding` ?

Le `Scaffold` passe un paramètre à son contenu : `innerPadding`. C'est la place occupée par les barres. Si tu ne l'appliques pas à ton contenu, celui-ci sera dessiné **sous** la barre de titre.

Android Studio te signale d'ailleurs un avertissement tant que tu ne l'utilises pas. On s'en servira à l'étape suivante.

---

[⬅️ Étape précédente](01-avant-de-commencer.md) · [📚 Sommaire](README.md) · [Étape suivante : Une ligne de crypto ➡️](03-crypto-row.md)
