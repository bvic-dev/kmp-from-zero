# 5. ⭐ Le ViewModel

> **Bloc 2** · Étapes 5 et 6 : sortir les données et la logique de l'interface
>
> 🧱 Composable → **ViewModel** → Repository → API

On veut ajouter un bouton « Rafraîchir » qui fait bouger les prix. Il faut donc un **état** (la liste des cryptos) et de la **logique** (comment la liste change). Où les mettre ?

## Pourquoi pas dans le composable ?

Tu sais déjà le faire, avec ce que tu as vu au TP 1 :

```kotlin
// Ne pas copier
var cryptos by remember { mutableStateOf(mockCryptos) }
```

Ça marche, mais ça pose trois problèmes :

1. **L'état est fragile.** Sur Android, tourner le téléphone détruit et recrée l'écran : un état gardé par `remember` est perdu. `rememberSaveable` sauve de petites valeurs, pas une liste de données qui viendra d'un serveur.
2. **La logique est mélangée à l'affichage.** Le composable décide à la fois *quoi afficher* et *comment les données évoluent*. Il devient long, et impossible à tester sans lancer l'interface.
3. **Il n'y a pas de place pour un travail long.** Un appel réseau dure plusieurs secondes. Un composable, lui, peut être ré-exécuté des dizaines de fois par seconde.

## Le ViewModel

Un **ViewModel** est une classe qui **garde l'état d'un écran** et **décide comment il évolue**. Ce n'est pas un composable : c'est du Kotlin tout simple, qui hérite de `ViewModel`.

Ce qui le rend utile, c'est sa **durée de vie** :

```
                 rotation      rotation
                    ▼             ▼
Composable   ██████   ██████████   ████████     détruit et recréé à chaque fois
ViewModel    ██████████████████████████████     le même du début à la fin
             ▲                            ▲
        l'écran s'ouvre            l'écran est fermé
```

Il est créé quand l'écran s'ouvre, il survit aux recompositions et aux rotations, et il est détruit quand on quitte l'écran pour de bon.

La répartition des rôles est celle du TP 1, un cran plus loin : **l'état descend, les événements remontent**.

```
┌──────────────┐   état (la liste)    ┌──────────────┐
│              │ ◀─────────────────── │              │
│  Composable  │                      │  ViewModel   │
│              │ ───────────────────▶ │              │
└──────────────┘  événement (clic)    └──────────────┘
```

Le composable ne modifie jamais l'état lui-même : il **prévient** le ViewModel (« on a cliqué sur Rafraîchir »), le ViewModel **change** l'état, et le composable **se redessine**.

## Exposer l'état : `StateFlow`

Le ViewModel doit exposer un état que l'interface peut **observer**. Au TP 1, tu utilisais `mutableStateOf`. Ici, on utilise un **`StateFlow`**.

Un **`Flow`** est une suite de valeurs qui arrivent au fil du temps, comme un tuyau : d'un côté on y envoie des valeurs, de l'autre on les reçoit. On dit qu'on **collecte** le flow.

Un **`StateFlow`** est un flow particulier, fait pour représenter un état :

- il a **toujours une valeur** courante, lisible avec `.value` ;
- quand cette valeur change, tous ceux qui le collectent sont prévenus.

| | `mutableStateOf` (TP 1) | `StateFlow` |
|---|---|---|
| Vient de | Compose | Kotlin (coroutines) |
| Utilisable dans | un composable | n'importe quelle couche |
| Lire la valeur | `.value` | `.value` |
| Être prévenu d'un changement | automatique dans un composable | il faut le **collecter** |

On choisit `StateFlow` dans le ViewModel pour qu'il ne dépende pas de Compose : c'est une couche à part.

Il existe en deux versions : `MutableStateFlow`, modifiable, et `StateFlow`, en lecture seule. Le ViewModel garde la première pour lui et n'expose que la seconde. Tu verras partout cette écriture :

```kotlin
private val _cryptos = MutableStateFlow(mockCryptos)        // privé : le ViewModel écrit
val cryptos: StateFlow<List<Crypto>> = _cryptos.asStateFlow() // public : l'interface lit
```

Ainsi, l'interface **ne peut pas** modifier l'état : elle est obligée de passer par une fonction du ViewModel.

## 🎯 Résultat attendu

- Un bouton « Rafraîchir » dans la barre de titre.
- À chaque clic, tous les prix bougent un peu, au hasard.
- Sur Android : après un clic, tourne l'émulateur. Les prix **ne reviennent pas** à leur valeur de départ.

## 📦 Ce qui est fourni

Deux fonctions font le lien entre Compose et le ViewModel :

- **`viewModel { ... }`** : donne le ViewModel de l'écran. Le bloc entre accolades le crée la première fois ; les fois suivantes, la fonction renvoie **le même**.
- **`collectAsStateWithLifecycle()`** : s'appelle sur un `StateFlow`, le collecte et le transforme en état Compose. C'est elle qui déclenche la recomposition.

Les imports, pour gagner du temps :

```kotlin
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
```

## 📏 Contraintes

- Une classe `CryptoListViewModel`, dans `ui/list/CryptoListViewModel.kt`.
- **Le mock déménage** : `mockCryptos` quitte `CryptoListScreen.kt` et va dans le fichier du ViewModel. L'interface ne le connaît plus.
- Le ViewModel expose `cryptos`, en lecture seule, et une fonction `refresh()` qui fait varier chaque prix de ± 2 %.
- L'écran est coupé en deux composables :
  - `CryptoListScreen` récupère le ViewModel, collecte l'état, et c'est tout ;
  - `CryptoListContent` reçoit la liste et un callback `onRefresh`. Il **ne connaît pas** le ViewModel.
- Les aperçus appellent `CryptoListContent`, avec deux ou trois cryptos écrites à la main.

## 📚 Documentation

- [Présentation du ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Le ViewModel dans Compose Multiplatform](https://kotlinlang.org/docs/multiplatform/compose-viewmodel.html)
- [`StateFlow` et `SharedFlow`](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Les flows en Kotlin](https://kotlinlang.org/docs/flow.html)
- [La couche interface (*UI layer*) et le flux de données unidirectionnel](https://developer.android.com/topic/architecture/ui-layer)
- [Les barres d'application, pour le bouton d'action](https://developer.android.com/develop/ui/compose/components/app-bars)

## 💡 Indices

<details>
<summary>Indice 1 : par où commencer ?</summary>

Dans cet ordre :

1. le ViewModel, avec `cryptos` seulement ;
2. `CryptoListScreen` qui l'affiche : l'application doit se comporter comme avant ;
3. `refresh()` et le bouton.

Pour le bouton, `CenterAlignedTopAppBar` a un paramètre `actions`. L'icône existe déjà : `Icons.Filled.Refresh`.

Pour modifier un objet d'une `data class`, pense à `copy`. Pour le hasard, à `Random.nextDouble(from, until)`.

</details>

<details>
<summary>Indice 2 : le squelette</summary>

```kotlin
class CryptoListViewModel : ViewModel() {

    private val _cryptos = MutableStateFlow(mockCryptos)
    val cryptos: StateFlow<List<Crypto>> = _cryptos.asStateFlow()

    fun refresh() {
        TODO("Remplacer la valeur de _cryptos par une nouvelle liste")
    }
}
```

```kotlin
@Composable
fun CryptoListScreen(
    modifier: Modifier = Modifier,
    viewModel: CryptoListViewModel = TODO("Récupérer le ViewModel"),
) {
    val cryptos = TODO("Collecter viewModel.cryptos")

    CryptoListContent(cryptos = cryptos, onRefresh = TODO(), modifier = modifier)
}

@Composable
fun CryptoListContent(
    cryptos: List<Crypto>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Le Scaffold de l'étape 2, avec le bouton en plus
}
```

</details>

<details>
<summary>Indice 3 : la solution</summary>

`ui/list/CryptoListViewModel.kt` :

```kotlin
package com.bvic.followmycrypto.ui.list

import androidx.lifecycle.ViewModel
import com.bvic.followmycrypto.model.Crypto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

private val mockCryptos = listOf(
    // ... la liste de l'étape 3
)

class CryptoListViewModel : ViewModel() {

    private val _cryptos = MutableStateFlow(mockCryptos)
    val cryptos: StateFlow<List<Crypto>> = _cryptos.asStateFlow()

    fun refresh() {
        _cryptos.value = _cryptos.value.map { crypto ->
            crypto.copy(price = crypto.price * Random.nextDouble(0.98, 1.02))
        }
    }
}
```

`ui/list/CryptoListScreen.kt` :

```kotlin
@Composable
fun CryptoListScreen(
    modifier: Modifier = Modifier,
    viewModel: CryptoListViewModel = viewModel { CryptoListViewModel() },
) {
    val cryptos by viewModel.cryptos.collectAsStateWithLifecycle()

    CryptoListContent(
        cryptos = cryptos,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}

@Composable
fun CryptoListContent(
    cryptos: List<Crypto>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FollowMyCrypto", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Rafraîchir")
                    }
                },
            )
        },
    ) { innerPadding ->
        CryptoList(cryptos = cryptos, modifier = Modifier.padding(innerPadding))
    }
}

private val previewCryptos = listOf(
    Crypto(symbol = "BTC", name = "Bitcoin", price = 85613.50, changePercent = 0.88),
    Crypto(symbol = "ETH", name = "Ethereum", price = 3214.07, changePercent = -1.42),
)

@Preview(showBackground = true)
@Composable
fun CryptoListContentPreview() {
    FollowMyCryptoTheme {
        CryptoListContent(cryptos = previewCryptos, onRefresh = {})
    }
}
```

`viewModel::refresh` est une **référence de fonction** : c'est la même chose que `{ viewModel.refresh() }`.

</details>

## 🤔 Pourquoi couper l'écran en deux ?

`CryptoListScreen` a besoin d'un ViewModel pour fonctionner. Un aperçu `@Preview` ne sait pas en créer un : essaie, il ne s'affiche pas.

`CryptoListContent`, lui, ne reçoit que des données et des callbacks. On peut l'afficher dans un aperçu avec n'importe quelles valeurs, et on pourra le tester facilement. On dit qu'il est **sans état** (*stateless*).

C'est un découpage que tu retrouveras dans presque toutes les applications Compose :

| | Connaît le ViewModel | Aperçu possible |
|---|---|---|
| `CryptoListScreen` | oui | non |
| `CryptoListContent` | non | oui |

> [!TIP]
> 📐 **À retenir :** le ViewModel **garde l'état** de l'écran et **décide** comment il change. Il l'expose en lecture seule dans un `StateFlow`. Le composable **collecte** cet état pour l'afficher et **appelle** les fonctions du ViewModel quand l'utilisateur agit.

---

[⬅️ Étape précédente](04-liste.md) · [📚 Sommaire](README.md) · [Étape suivante : Le repository et les états de l'écran ➡️](06-repository-et-etats.md)
