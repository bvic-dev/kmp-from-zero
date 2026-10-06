# 6. ⭐ Le repository et les états de l'écran

> **Bloc 2** · Étapes 5 et 6 : sortir les données et la logique de l'interface
>
> 🧱 Composable → ViewModel → **Repository** → API

Le mock est sorti de l'interface, mais il est maintenant dans le ViewModel. Or le rôle du ViewModel est de gérer l'état de l'écran, pas de savoir **d'où viennent les données**. Demain elles viendront du réseau, après-demain peut-être d'un cache sur le téléphone : le ViewModel ne devrait pas avoir à changer pour autant.

## Le repository

Un **repository** (« dépôt ») est la classe qui **fournit les données** au reste de l'application. Le ViewModel lui demande « donne-moi les cryptos », sans savoir comment il se débrouille.

On l'écrit en deux morceaux :

- une **interface**, le contrat : *ce que* le repository sait faire ;
- une ou plusieurs **implémentations** : *comment* il le fait.

Dans cette étape, on écrit une fausse implémentation qui renvoie le mock. À l'étape 9, on en écrira une vraie qui appelle Binance, et **le ViewModel ne changera pas d'une ligne**.

## Des données qui se font attendre

De vraies données ont deux particularités que le mock n'a pas : elles mettent **du temps** à arriver, et la demande peut **échouer**. Notre faux repository va simuler les deux.

### Attendre sans bloquer : les coroutines

Sur toutes les plateformes, un seul fil d'exécution (le *main thread*) dessine l'interface et réagit aux clics. Si on le fait attendre deux secondes, l'application est **figée** pendant deux secondes.

Kotlin résout ça avec les **coroutines** :

- Une fonction marquée **`suspend`** peut se mettre en pause, puis reprendre plus tard, **sans bloquer** le fil qui l'exécute. `delay(1_500)` en est une : elle attend 1,5 seconde pendant que l'interface continue de vivre.
- Une fonction `suspend` ne peut être appelée que depuis une autre fonction `suspend`, ou depuis une **coroutine**.
- On démarre une coroutine avec **`launch { ... }`**, dans un **scope** qui détermine sa durée de vie.

Le ViewModel a son propre scope : **`viewModelScope`**. Une coroutine lancée dedans est **annulée automatiquement** quand le ViewModel est détruit. Si l'utilisateur quitte l'écran pendant un chargement, le travail s'arrête tout seul.

```kotlin
viewModelScope.launch {
    val cryptos = repository.getCryptos() // en pause ici, sans rien bloquer
    // reprend ici quand les données sont arrivées
}
```

## L'état de l'interface

Avant de coder, pose-toi la question : **que peut afficher cet écran ?**

| État | Ce que l'utilisateur voit |
|---|---|
| Chargement | Un indicateur qui tourne |
| Erreur | Un message et un bouton « Réessayer » |
| Succès | La liste |

Une simple `List<Crypto>` ne suffit plus à décrire l'écran : une liste vide, est-ce un chargement, une erreur, ou vraiment aucune crypto ?

On crée donc un type qui décrit **tout ce que l'écran peut afficher, et rien d'autre** : l'**état de l'interface** (*UI state*). C'est une photo de l'écran à un instant donné.

```
                    ┌────────────────────────┐
                    │       ViewModel        │
   Repository ────▶ │                        │
                    │  uiState = Loading     │ ──────▶  Composable
   Clic       ────▶ │          | Error       │   affiche ce qu'on lui donne
                    │          | Success     │
                    └────────────────────────┘
                     un seul état, à un seul endroit
```

Cet état est **centralisé dans le ViewModel**, et ça change la façon de raisonner :

- **Une seule source de vérité.** Pour savoir ce que l'écran affiche, il suffit de regarder une valeur : `uiState`. Personne d'autre ne garde une copie de l'état dans son coin.
- **Le composable ne décide plus de rien.** Il reçoit un état et le dessine : `interface = f(état)`. C'est le principe du TP 1 (« l'interface est une fonction de l'état »), appliqué à un écran entier.
- **Seul le ViewModel fait changer l'état.** Un chargement qui commence, une réponse qui arrive, une erreur : chaque événement se traduit par une nouvelle valeur de `uiState`, et l'écran suit.
- **Chaque état se teste et s'affiche à part.** Un aperçu par état, sans réseau ni attente : il suffit de passer `Loading`, `Error` ou `Success` au composable.

## 🎯 Résultat attendu

Au lancement, puis à chaque clic sur « Rafraîchir », l'indicateur tourne pendant 1,5 seconde, puis la liste apparaît.

![Chargement](img/04-loading.png)

Quand le repository échoue, l'écran affiche un message et un bouton qui relance le chargement.

![Erreur](img/05-error.png)

## 📦 Ce qui est fourni

**Le contrat du repository**, dans `data/CryptoRepository.kt` :

```kotlin
package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.model.Crypto

// Le contrat : « je sais fournir la liste des cryptos ». D'où elles viennent ne regarde que l'implémentation.
interface CryptoRepository {
    suspend fun getCryptos(): List<Crypto>
}
```

**L'état de l'interface**, dans son propre fichier `ui/list/CryptoListUiState.kt` :

```kotlin
package com.bvic.followmycrypto.ui.list

import com.bvic.followmycrypto.model.Crypto

// Tout ce que l'écran peut afficher, et rien d'autre
sealed interface CryptoListUiState {
    data object Loading : CryptoListUiState
    data class Error(val message: String) : CryptoListUiState
    data class Success(val cryptos: List<Crypto>) : CryptoListUiState
}
```

Il a son propre fichier parce qu'il n'appartient ni au ViewModel ni au composable : c'est le **contrat** entre les deux. Le ViewModel le produit, le composable le consomme.

Une `sealed interface` est une interface dont **toutes les implémentations sont connues** à la compilation. Dans un `when`, Kotlin vérifie que tu as traité tous les cas : si tu oublies `Error`, le code ne compile pas.

**Le conteneur**, dans `data/AppContainer.kt` : l'unique endroit qui choisit quelle implémentation du repository l'application utilise.

```kotlin
package com.bvic.followmycrypto.data

// Injection de dépendances « à la main » : un seul endroit crée les objets partagés
object AppContainer {

    val cryptoRepository: CryptoRepository by lazy { FakeCryptoRepository() }
}
```

## 📏 Contraintes

- **Le mock déménage encore** : il va dans `data/FakeCryptoRepository.kt`, avec une classe `FakeCryptoRepository` qui implémente `CryptoRepository`, attend 1,5 seconde et renvoie le mock.
- Le ViewModel **reçoit** le repository dans son constructeur. Il ne le crée pas lui-même et ne connaît que l'interface `CryptoRepository`.
- Le ViewModel expose un seul état : `uiState`, de type `StateFlow<CryptoListUiState>`. Il charge les données dès sa création, et `refresh()` les recharge.
- `CryptoListContent` reçoit un `CryptoListUiState` et affiche l'un des trois contenus. Les trois gardent la barre de titre.
- Le bouton « Réessayer » relance le chargement.
- Un aperçu par état.

**Pour tester l'erreur :** fais échouer ton faux repository une fois sur deux, par exemple avec `error("Serveur en panne")`. Pense à retirer cette ligne ensuite.

## 📚 Documentation

- [Définir l'état de l'interface (*UI state*)](https://developer.android.com/topic/architecture/ui-layer)
- [La couche données (*data layer*) et les repositories](https://developer.android.com/topic/architecture/data-layer)
- [Les bases des coroutines](https://kotlinlang.org/docs/coroutines-basics.html)
- [Les coroutines sur Android, dont `viewModelScope`](https://developer.android.com/kotlin/coroutines)
- [Les classes et interfaces scellées (*sealed*)](https://kotlinlang.org/docs/sealed-classes.html)
- [Les indicateurs de progression](https://developer.android.com/develop/ui/compose/components/progress)
- [L'injection de dépendances manuelle](https://developer.android.com/training/dependency-injection/manual)

## 💡 Indices

<details>
<summary>Indice 1 : dans quel ordre ?</summary>

1. `FakeCryptoRepository` : c'est une classe avec une seule fonction.
2. Le ViewModel : remplace `_cryptos` par `_uiState`, qui vaut `Loading` au départ. Écris une fonction privée `load()`, appelée dans un bloc `init { }` et par `refresh()`.
3. `CryptoListScreen` : il faut maintenant passer le repository au ViewModel, là où tu le crées.
4. `CryptoListContent` : un `when (uiState)` dans le contenu du `Scaffold`.

Pour attraper une erreur dans une coroutine, c'est un `try` / `catch` ordinaire.

Pour l'indicateur de chargement, cherche « ProgressIndicator » dans l'autocomplétion.

</details>

<details>
<summary>Indice 2 : le squelette du ViewModel</summary>

```kotlin
class CryptoListViewModel(
    private val repository: CryptoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CryptoListUiState>(CryptoListUiState.Loading)
    val uiState: StateFlow<CryptoListUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun refresh() = load()

    private fun load() {
        // 1. Passer en état Loading
        // 2. Lancer une coroutine dans viewModelScope
        // 3. Dedans : appeler le repository, puis passer en Success
        // 4. En cas d'exception : passer en Error
        TODO()
    }
}
```

</details>

<details>
<summary>Indice 3 : la solution</summary>

`data/FakeCryptoRepository.kt` :

```kotlin
package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.model.Crypto
import kotlinx.coroutines.delay

private val mockCryptos = listOf(
    // ... la liste de l'étape 3
)

// Un faux repository : il simule un serveur lent, sans réseau
class FakeCryptoRepository : CryptoRepository {

    override suspend fun getCryptos(): List<Crypto> {
        delay(1_500)
        return mockCryptos
    }
}
```

`ui/list/CryptoListViewModel.kt` :

```kotlin
class CryptoListViewModel(
    private val repository: CryptoRepository,
) : ViewModel() {

    // Version modifiable, privée : seul le ViewModel peut changer l'état
    private val _uiState = MutableStateFlow<CryptoListUiState>(CryptoListUiState.Loading)

    // Version en lecture seule, exposée à l'interface
    val uiState: StateFlow<CryptoListUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun refresh() = load()

    private fun load() {
        _uiState.value = CryptoListUiState.Loading
        viewModelScope.launch {
            try {
                _uiState.value = CryptoListUiState.Success(repository.getCryptos())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CryptoListUiState.Error("Impossible de charger les cours.")
            }
        }
    }
}
```

`ui/list/CryptoListScreen.kt` :

```kotlin
@Composable
fun CryptoListScreen(
    modifier: Modifier = Modifier,
    viewModel: CryptoListViewModel = viewModel { CryptoListViewModel(AppContainer.cryptoRepository) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CryptoListContent(
        uiState = uiState,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}

// Version sans état : elle ne connaît pas le ViewModel, on peut donc l'afficher dans un aperçu
@Composable
fun CryptoListContent(
    uiState: CryptoListUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { /* inchangé */ },
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding).fillMaxSize()
        when (uiState) {
            CryptoListUiState.Loading -> LoadingContent(modifier = contentModifier)
            is CryptoListUiState.Error -> ErrorContent(
                message = uiState.message,
                onRetry = onRefresh,
                modifier = contentModifier,
            )
            is CryptoListUiState.Success -> CryptoList(cryptos = uiState.cryptos, modifier = contentModifier)
        }
    }
}

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = message)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) {
            Text("Réessayer")
        }
    }
}
```

Le code complet est sur le tag `tp2-bloc2`.

</details>

## 🤔 Quelques questions à se poser

**Pourquoi le ViewModel reçoit-il le repository, au lieu de le créer ?**

S'il écrivait `FakeCryptoRepository()` lui-même, il serait lié à cette implémentation : il faudrait le modifier pour passer à la vraie. En le **recevant** de l'extérieur, il ne dépend que du contrat. C'est ce qu'on appelle l'**injection de dépendances**. Ici, c'est `AppContainer` qui choisit l'implémentation, à un seul endroit. Dans les gros projets, des bibliothèques comme Koin ou Hilt automatisent ce travail.

**Pourquoi un `catch` spécial pour `CancellationException` ?**

Quand une coroutine est annulée (l'utilisateur a quitté l'écran), Kotlin l'arrête en levant cette exception. Ce n'est pas une erreur : il faut la laisser passer, sinon la coroutine ne s'arrête pas proprement. C'est un réflexe à avoir dès qu'on écrit `catch (e: Exception)` dans une coroutine.

**Pourquoi `when (uiState)` plutôt que trois variables `isLoading`, `error`, `cryptos` ?**

Avec trois variables, rien n'empêche `isLoading = true` **et** une erreur en même temps : que doit afficher l'écran ? Avec une `sealed interface`, ces états impossibles ne peuvent pas exister.

---

## 🏁 Fin du bloc 2

Regarde où est passé le mock depuis l'étape 3 :

| Étape | Où est le mock | Qui le connaît |
|---|---|---|
| 3 – 4 | `CryptoListScreen.kt` | L'interface |
| 5 | `CryptoListViewModel.kt` | Le ViewModel |
| 6 | `FakeCryptoRepository.kt` | Une implémentation du repository, et personne d'autre |

L'interface et le ViewModel sont **terminés** : ils ne bougeront plus quand on branchera le réseau.

Si tu es en retard ou bloqué, récupère la correction du bloc :

```bash
git stash
git checkout tp2-bloc2
```

---

[⬅️ Étape précédente](05-viewmodel.md) · [📚 Sommaire](README.md) · Étape suivante : *à venir*
