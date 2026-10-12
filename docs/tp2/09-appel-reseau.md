# 9. L'appel réseau

> **Bloc 3** · Étapes 7 à 9 : remplacer les fausses données par les vraies
>
> 🧱 Composable → ViewModel → **Repository** → **API**

Il reste à écrire deux classes, puis à changer **une ligne** dans `AppContainer`. On le fait en quatre petites parties.

## Ktor, le client HTTP

Pour appeler une API, il faut un client HTTP. On utilise **Ktor**, la bibliothèque de JetBrains, parce qu'elle fonctionne sur toutes les plateformes. Tu l'as installée à l'étape précédente : `ktor-client-core` dans `commonMain`, et un **moteur** par plateforme. Tout le code de cette étape s'écrit dans `commonMain`.

## 🎯 Résultat attendu

L'application affiche les **vrais** cours. Clique sur « Rafraîchir » plusieurs fois : les prix changent, pour de bon cette fois.

Compare avec [binance.com](https://www.binance.com/fr/markets/overview) : tu dois retrouver les mêmes valeurs.

## 🗺️ Le plan

On avance en quatre petites parties, dans l'ordre du trajet des données. Chacune a ses propres indices : si tu bloques sur l'une, tu n'as pas besoin d'ouvrir la solution des autres.

| Partie | Ce que tu écris | Ce qu'elle fait |
|---|---|---|
| 1 | `BinanceApi.getTickers` | Appelle Binance et renvoie des `TickerDto` |
| 2 | `TickerDto.toCrypto()` | Convertit **un** DTO en `Crypto` |
| 3 | `RemoteCryptoRepository` | Enchaîne les deux : appelle l'API, puis convertit |
| 4 | Une ligne dans `AppContainer` | Branche le vrai repository à la place du faux |

Tu ne modifies **ni** le ViewModel, **ni** les composables.

L'application ne changera qu'à la partie 4 : d'ici là, vérifie simplement que le projet **compile** à la fin de chaque partie.

## 📚 Documentation

- [Ktor : faire une requête](https://ktor.io/docs/client-requests.html)
- [Ktor : lire une réponse](https://ktor.io/docs/client-responses.html)
- [Ktor : la sérialisation avec `ContentNegotiation`](https://ktor.io/docs/client-serialization.html)
- [Android : se connecter au réseau](https://developer.android.com/develop/connectivity/network-ops/connecting)

## Avant de commencer : configurer le client

La configuration du client HTTP est fournie. Remplace le contenu de `data/AppContainer.kt` :

```kotlin
package com.bvic.followmycrypto.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Injection de dépendances « à la main » : un seul endroit crée les objets partagés
object AppContainer {

    private val httpClient by lazy {
        HttpClient {
            // Une réponse 4xx ou 5xx lève une exception au lieu d'être ignorée
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    val cryptoRepository: CryptoRepository by lazy { FakeCryptoRepository() }
}
```

`ContentNegotiation` est le plugin qui convertit automatiquement le JSON de la réponse en objets Kotlin. On retrouve `ignoreUnknownKeys`, annoncé à l'étape précédente.

Pense à l'import de `kotlinx.serialization.json.Json` : il vient de la bibliothèque que tu as ajoutée.

## Partie 1 : appeler l'API

### 📦 Ce qui est fourni

Le squelette, dans `data/remote/BinanceApi.kt`. La construction du paramètre `symbols` est un peu pénible, elle est donc écrite :

```kotlin
package com.bvic.followmycrypto.data.remote

import io.ktor.client.HttpClient

class BinanceApi(private val client: HttpClient) {

    suspend fun getTickers(pairs: List<String>): List<TickerDto> {
        // ["BTCUSDT", "ETHUSDT"] devient le texte ["BTCUSDT","ETHUSDT"]
        val symbols = pairs.joinToString(separator = ",", prefix = "[", postfix = "]") { "\"$it\"" }
        TODO("Appeler l'API et renvoyer la réponse")
    }

    private companion object {
        // Domaine de Binance dédié aux données publiques de marché, sans clé d'API
        const val BASE_URL = "https://data-api.binance.vision"
    }
}
```

### 📏 À faire

Remplace le `TODO` : une requête `GET` sur `/api/v3/ticker/24hr`, avec le paramètre `symbols`, qui renvoie la liste des `TickerDto`.

<details>
<summary>💡 Indice</summary>

Tu as besoin de trois fonctions de Ktor : `client.get(url) { ... }`, `parameter(nom, valeur)` dans le bloc, et `.body()` sur le résultat, qui convertit le JSON dans le type attendu.

</details>

<details>
<summary>💡 La solution</summary>

```kotlin
package com.bvic.followmycrypto.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class BinanceApi(private val client: HttpClient) {

    suspend fun getTickers(pairs: List<String>): List<TickerDto> {
        // ["BTCUSDT", "ETHUSDT"] devient le texte ["BTCUSDT","ETHUSDT"]
        val symbols = pairs.joinToString(separator = ",", prefix = "[", postfix = "]") { "\"$it\"" }
        return client.get("$BASE_URL/api/v3/ticker/24hr") {
            parameter("symbols", symbols)
        }.body()
    }

    private companion object {
        // Domaine de Binance dédié aux données publiques de marché, sans clé d'API
        const val BASE_URL = "https://data-api.binance.vision"
    }
}
```

</details>

## Partie 2 : convertir un DTO en modèle

À l'étape 7, tu as relevé trois différences entre ce que Binance envoie et ce que l'application attend. Le **mapper** est la fonction qui les règle, pour **un** élément.

### 📦 Ce qui est fourni

La liste des cryptos qu'on suit, à placer dans un nouveau fichier `data/RemoteCryptoRepository.kt`. Elle associe chaque **paire** de Binance au **nom** à afficher, puisque l'API ne le fournit pas :

```kotlin
package com.bvic.followmycrypto.data

// Les cryptos suivies : la paire à demander à Binance, et le nom à afficher (Binance ne le fournit pas)
private val cryptoNames = mapOf(
    "BTCUSDT" to "Bitcoin",
    "ETHUSDT" to "Ethereum",
    "BNBUSDT" to "BNB",
    "SOLUSDT" to "Solana",
    "XRPUSDT" to "XRP",
    "ADAUSDT" to "Cardano",
    "DOGEUSDT" to "Dogecoin",
    "SHIBUSDT" to "Shiba Inu",
)
```

### 📏 À faire

Dans le même fichier, écris une fonction `toCrypto()` qui transforme un `TickerDto` en `Crypto` :

| Champ de `Crypto` | D'où il vient |
|---|---|
| `symbol` | `"BTCUSDT"` sans son `USDT` |
| `name` | `cryptoNames` |
| `price` | `lastPrice`, converti en nombre |
| `changePercent` | `priceChangePercent`, converti en nombre |

<details>
<summary>💡 Indice</summary>

C'est une **fonction d'extension** : `fun TickerDto.toCrypto(): Crypto`. À l'intérieur, tu accèdes directement aux propriétés du DTO.

Pour le symbole, regarde `removeSuffix`. Pour les nombres, `toDouble()`. Pour le nom, on lit une `Map` avec des crochets : `cryptoNames[cle]`.

</details>

<details>
<summary>💡 La solution</summary>

```kotlin
// DTO (ce que l'API envoie) → modèle (ce dont l'application a besoin)
private fun TickerDto.toCrypto(): Crypto {
    return Crypto(
        symbol = symbol.removeSuffix("USDT"),
        name = cryptoNames[symbol] ?: symbol,
        price = lastPrice.toDouble(),
        changePercent = priceChangePercent.toDouble(),
    )
}
```

`cryptoNames[symbol]` peut renvoyer `null` si la paire n'est pas dans notre liste. `?: symbol` donne une valeur de secours : on affiche la paire à la place du nom.

</details>

## Partie 3 : le repository

Il ne reste qu'à enchaîner les deux parties précédentes.

### 📦 Ce qui est fourni

Le squelette, toujours dans `data/RemoteCryptoRepository.kt`, entre la liste et le mapper :

```kotlin
class RemoteCryptoRepository(private val api: BinanceApi) : CryptoRepository {

    override suspend fun getCryptos(): List<Crypto> {
        // 1. Les paires à demander
        // 2. L'appel à l'API
        // 3. La conversion de chaque DTO en modèle
        TODO()
    }
}
```

### 📏 À faire

Écris les trois lignes, une par commentaire.

<details>
<summary>💡 Indice</summary>

1. Les paires sont les **clés** de `cryptoNames`. `getTickers` attend une `List` : regarde `keys` et `toList()`.
2. C'est la fonction de la partie 1.
3. Pour transformer chaque élément d'une liste, c'est `map`, avec la fonction de la partie 2.

</details>

<details>
<summary>💡 La solution</summary>

```kotlin
class RemoteCryptoRepository(private val api: BinanceApi) : CryptoRepository {

    override suspend fun getCryptos(): List<Crypto> {
        // 1. Les paires à demander
        val pairs = cryptoNames.keys.toList()
        // 2. L'appel à l'API
        val tickers = api.getTickers(pairs)
        // 3. La conversion de chaque DTO en modèle
        return tickers.map { ticker -> ticker.toCrypto() }
    }
}
```

</details>

## Partie 4 : brancher le vrai repository

### 📏 À faire

1. Dans `AppContainer`, fournis un `RemoteCryptoRepository` à la place du faux.
2. Lance l'application sur Desktop, **puis** sur Android.

> [!TIP]
> Ça ne marche pas et tu ne sais pas pourquoi ? Ton ViewModel attrape l'exception et affiche toujours le même message. Ajoute temporairement `println(e)` dans son `catch` : l'erreur réelle s'affichera dans l'onglet **Run** (Desktop) ou **Logcat** (Android).

<details>
<summary>💡 Indice : ça marche sur Desktop mais pas sur Android</summary>

Affiche l'exception avec `println(e)` et lis-la : Android te dit exactement ce qui manque.

Sur Android, une application doit **déclarer** qu'elle utilise Internet. Ça se passe dans `androidApp/src/main/AndroidManifest.xml`.

</details>

<details>
<summary>💡 La solution</summary>

Dans `data/AppContainer.kt`, la seule ligne qui change (avec l'import de `BinanceApi`) :

```kotlin
    // Pour travailler sans réseau, remplace cette ligne par FakeCryptoRepository()
    val cryptoRepository: CryptoRepository by lazy { RemoteCryptoRepository(BinanceApi(httpClient)) }
```

Dans `androidApp/src/main/AndroidManifest.xml`, juste sous la balise `<manifest>` :

```xml
    <uses-permission android:name="android.permission.INTERNET" />
```

</details>

Tu remarqueras que les cryptos ne sont plus dans le même ordre qu'avec le mock : c'est l'ordre dans lequel Binance les renvoie.

## 🤔 Qu'est-ce qui a changé, au juste ?

Fais le compte des fichiers que tu as touchés dans ce bloc : deux classes ajoutées dans `data`, une ligne modifiée dans `AppContainer`, une permission. **Rien** dans `ui`.

C'est tout l'intérêt du découpage en couches : chacune ne connaît que sa voisine, à travers un contrat. Tant que le contrat est respecté, on peut remplacer ce qu'il y a derrière.

Garde `FakeCryptoRepository` dans le projet. Il te resservira pour travailler sans connexion, ou pour reproduire un cas précis : une liste vide, une erreur, un chargement très long.

Teste aussi le cas d'erreur réel : coupe ton Wi-Fi et clique sur « Rafraîchir ». L'écran d'erreur de l'étape 6 doit s'afficher, sans que tu aies écrit une ligne de plus.

---

## 🏁 Fin du bloc 3

Un clic sur « Rafraîchir » traverse maintenant toute l'application, puis revient :

```
  clic                                                              requête
 ──────▶ Composable ──▶ ViewModel ──▶ Repository ──▶ BinanceApi ──────────▶ Binance
                                                                              │
 ◀────── Composable ◀── ViewModel ◀── Repository ◀── BinanceApi ◀────────────┘
 affichage   CryptoListUiState    List<Crypto>    List<TickerDto>      JSON
```

Si tu es en retard ou bloqué, récupère la correction du bloc :

```bash
git stash
git checkout tp2-bloc3
```

---

[⬅️ Étape précédente](08-bibliotheques.md) · [📚 Sommaire](README.md) · [Étape suivante : Navigation et écran de détail ➡️](10-navigation-et-detail.md)
