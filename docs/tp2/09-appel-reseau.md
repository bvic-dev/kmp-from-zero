# 9. L'appel réseau

> **Bloc 3** · Étapes 7 à 9 : remplacer les fausses données par les vraies
>
> 🧱 Composable → ViewModel → **Repository** → **API**

Il reste à écrire deux classes, puis à changer **une ligne** dans `AppContainer`.

## Ktor, le client HTTP

Pour appeler une API, il faut un client HTTP. On utilise **Ktor**, la bibliothèque de JetBrains, parce qu'elle fonctionne sur toutes les plateformes. Tu l'as installée à l'étape précédente : `ktor-client-core` dans `commonMain`, et un **moteur** par plateforme. Tout le code de cette étape s'écrit dans `commonMain`.

## 📦 Ce qui est fourni

**La configuration du client**, dans `AppContainer`. Remplace le contenu de `data/AppContainer.kt` :

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

**Le squelette de l'API**, dans `data/remote/BinanceApi.kt`. La construction du paramètre `symbols` est un peu pénible, elle est donc écrite :

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

**Les noms des cryptos**, que l'API ne fournit pas. À placer en haut de ton futur `RemoteCryptoRepository.kt` :

```kotlin
// Binance ne fournit pas le nom des cryptos : on garde notre propre liste
private val cryptoNames = mapOf(
    "BTC" to "Bitcoin",
    "ETH" to "Ethereum",
    "BNB" to "BNB",
    "SOL" to "Solana",
    "XRP" to "XRP",
    "ADA" to "Cardano",
    "DOGE" to "Dogecoin",
    "SHIB" to "Shiba Inu",
)
```

## 🎯 Résultat attendu

L'application affiche les **vrais** cours. Clique sur « Rafraîchir » plusieurs fois : les prix changent, pour de bon cette fois.

Compare avec [binance.com](https://www.binance.com/fr/markets/overview) : tu dois retrouver les mêmes valeurs.

## 📏 Contraintes

1. **`BinanceApi.getTickers`** fait une requête `GET` sur `/api/v3/ticker/24hr`, avec le paramètre `symbols`, et renvoie la liste des `TickerDto`.
2. **Un mapper** `TickerDto.toCrypto()` convertit un DTO en `Crypto`. C'est lui qui règle les trois différences relevées à l'étape 7.
3. **`RemoteCryptoRepository`**, dans `data/RemoteCryptoRepository.kt`, implémente `CryptoRepository`. Il reçoit une `BinanceApi` dans son constructeur, demande les paires de toutes les cryptos de `cryptoNames` et renvoie des `Crypto`.
4. **`AppContainer`** fournit un `RemoteCryptoRepository` à la place du faux.
5. Tu ne modifies **ni** le ViewModel, **ni** les composables.
6. L'application fonctionne sur Desktop **et** sur Android.

> [!TIP]
> Ça ne marche pas et tu ne sais pas pourquoi ? Ton ViewModel attrape l'exception et affiche toujours le même message. Ajoute temporairement `println(e)` dans son `catch` : l'erreur réelle s'affichera dans l'onglet **Run** (Desktop) ou **Logcat** (Android).

## 📚 Documentation

- [Ktor : faire une requête](https://ktor.io/docs/client-requests.html)
- [Ktor : lire une réponse](https://ktor.io/docs/client-responses.html)
- [Ktor : la sérialisation avec `ContentNegotiation`](https://ktor.io/docs/client-serialization.html)
- [Android : se connecter au réseau](https://developer.android.com/develop/connectivity/network-ops/connecting)

## 💡 Indices

<details>
<summary>Indice 1 : faire une requête avec Ktor</summary>

Tu as besoin de trois fonctions : `client.get(url) { ... }`, `parameter(nom, valeur)` dans le bloc, et `.body()` sur le résultat, qui convertit le JSON dans le type attendu.

</details>

<details>
<summary>Indice 2 : le mapper</summary>

C'est une **fonction d'extension** sur `TickerDto`. Pour le symbole, regarde `removeSuffix`. Pour les nombres, `toDouble()`. Pour le nom, tu as `cryptoNames`.

</details>

<details>
<summary>Indice 3 : ça marche sur Desktop mais pas sur Android</summary>

Affiche l'exception avec `println(e)` et lis-la : Android te dit exactement ce qui manque.

Sur Android, une application doit **déclarer** qu'elle utilise Internet. Ça se passe dans `androidApp/src/main/AndroidManifest.xml`.

</details>

<details>
<summary>Indice 4 : la solution</summary>

`data/remote/BinanceApi.kt` :

```kotlin
package com.bvic.followmycrypto.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class BinanceApi(private val client: HttpClient) {

    // pairs = ["BTCUSDT", "ETHUSDT"] devient le paramètre symbols=["BTCUSDT","ETHUSDT"]
    suspend fun getTickers(pairs: List<String>): List<TickerDto> =
        client.get("$BASE_URL/api/v3/ticker/24hr") {
            parameter("symbols", pairs.joinToString(separator = ",", prefix = "[", postfix = "]") { "\"$it\"" })
        }.body()

    private companion object {
        // Domaine de Binance dédié aux données publiques de marché, sans clé d'API
        const val BASE_URL = "https://data-api.binance.vision"
    }
}
```

`data/RemoteCryptoRepository.kt` :

```kotlin
package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.data.remote.BinanceApi
import com.bvic.followmycrypto.data.remote.TickerDto
import com.bvic.followmycrypto.model.Crypto

// Binance ne fournit pas le nom des cryptos : on garde notre propre liste
private val cryptoNames = mapOf(
    // ... la liste fournie
)

private const val QUOTE = "USDT"

class RemoteCryptoRepository(private val api: BinanceApi) : CryptoRepository {

    override suspend fun getCryptos(): List<Crypto> {
        val tickers = api.getTickers(cryptoNames.keys.map { it + QUOTE }).associateBy { it.symbol }
        // Binance ne garantit pas l'ordre de la réponse : on garde celui de notre liste
        return cryptoNames.keys.mapNotNull { symbol -> tickers[symbol + QUOTE]?.toCrypto() }
    }
}

// DTO (ce que l'API envoie) → modèle (ce dont l'application a besoin)
private fun TickerDto.toCrypto(): Crypto {
    val symbol = symbol.removeSuffix(QUOTE)
    return Crypto(
        symbol = symbol,
        name = cryptoNames[symbol] ?: symbol,
        price = lastPrice.toDouble(),
        changePercent = priceChangePercent.toDouble(),
    )
}
```

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
