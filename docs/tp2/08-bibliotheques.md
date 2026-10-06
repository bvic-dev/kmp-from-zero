# 8. Ajouter les bibliothèques

> **Bloc 3** · Étapes 7 à 9 : remplacer les fausses données par les vraies
>
> 🧱 Composable → ViewModel → Repository → **API**

Faire une requête HTTP et lire du JSON ne font pas partie de Kotlin : il faut des **bibliothèques**. Jusqu'ici, celles du projet avaient été choisies par l'assistant de création. Cette fois, c'est toi qui les ajoutes.

## Ce dont on a besoin

| Besoin | Bibliothèque |
|---|---|
| Faire des requêtes HTTP | **Ktor**, le client HTTP de JetBrains, qui fonctionne sur toutes les plateformes |
| Transformer du JSON en objets Kotlin | **kotlinx.serialization** |

## Ajouter une dépendance dans un projet KMP

Il y a trois questions à se poser.

**1. Quelle bibliothèque, et quelle version ?** Dans ce projet, tout est déclaré à un seul endroit : le **catalogue de versions**, `gradle/libs.versions.toml`. Ouvre-le, il a trois sections :

```toml
[versions]    # les numéros de version
[libraries]   # les bibliothèques, qui font référence à une version
[plugins]     # les plugins Gradle
```

Chaque entrée du catalogue devient accessible dans les fichiers Gradle sous le nom `libs.…`, les tirets devenant des points : `compose-material3` s'écrit `libs.compose.material3`.

**2. Pour quelle plateforme ?** C'est la question propre à Kotlin Multiplatform. Dans `shared/build.gradle.kts`, les dépendances sont rangées par **source set** :

```kotlin
sourceSets {
    androidMain.dependencies { /* Android seulement */ }
    commonMain.dependencies { /* toutes les plateformes */ }
}
```

Une bibliothèque utilisée dans `commonMain` doit exister pour **toutes** les plateformes du projet.

**3. Faut-il un plugin ?** Certaines bibliothèques génèrent du code à la compilation et ont besoin d'un plugin Gradle en plus de la dépendance.

## 📦 Ce qui est fourni

Les coordonnées des bibliothèques, avec les versions testées pour ce TP. Ne prends pas « la dernière version » trouvée sur Internet : une version de Ktor ou de kotlinx.serialization n'est compatible qu'avec certaines versions de Kotlin.

| Coordonnées | Version |
|---|---|
| `io.ktor:ktor-client-core` | 3.6.0 |
| `io.ktor:ktor-client-content-negotiation` | 3.6.0 |
| `io.ktor:ktor-serialization-kotlinx-json` | 3.6.0 |
| `io.ktor:ktor-client-okhttp` | 3.6.0 |
| `io.ktor:ktor-client-darwin` | 3.6.0 |
| `org.jetbrains.kotlinx:kotlinx-serialization-json` | 1.11.0 |

Et un plugin, `org.jetbrains.kotlin.plugin.serialization`, qui doit avoir **la même version que Kotlin**.

## 📏 Contraintes

- Tout est déclaré dans `gradle/libs.versions.toml`. Aucun numéro de version dans les fichiers `build.gradle.kts`.
- Les six bibliothèques ne vont pas toutes dans `commonMain` : à toi de trouver lesquelles sont propres à une plateforme, et à laquelle. Le projet en a trois : Android, iOS et Desktop (`jvm`).
- Le plugin est déclaré comme les autres plugins du projet. Regarde comment ils le sont : il y a **deux** fichiers à modifier.
- Après tes modifications, synchronise Gradle (l'éléphant 🐘 en haut à droite d'Android Studio).

## ✅ Vérifier que ça marche : écrire le DTO

Tu sauras que tout est en place quand ce qui suit compile.

Crée la classe qui représente un élément de la réponse de Binance, vue à l'étape 7 :

- une `data class TickerDto`, dans `data/remote/TickerDto.kt` ;
- annotée `@Serializable` ;
- avec les trois champs dont on a besoin, nommés **exactement comme dans le JSON** ;
- avec les types **du JSON**, pas ceux qu'on aimerait avoir.

Lance ensuite l'application sur Desktop, puis sur Android : elle doit démarrer comme avant.

> [!WARNING]
> **Le piège du plugin oublié.** Sans le plugin, `@Serializable` compile quand même : l'annotation existe dans la bibliothèque. Mais aucun code de lecture du JSON n'est généré, et l'application échouera **à l'exécution**, à l'étape suivante, avec l'erreur `Serializer for class 'TickerDto' is not found`. Si tu la rencontres, tu sauras où regarder.

## 📚 Documentation

- [Ajouter des dépendances à un projet Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform/multiplatform-add-dependencies.html)
- [Les catalogues de versions de Gradle](https://docs.gradle.org/current/userguide/version_catalogs.html)
- [Ktor : les dépendances du client et les moteurs](https://ktor.io/docs/client-dependencies.html)
- [Ktor : créer une application multiplateforme](https://ktor.io/docs/client-create-multiplatform-application.html)
- [Ktor : la sérialisation](https://ktor.io/docs/client-serialization.html)
- [kotlinx.serialization](https://kotlinlang.org/docs/serialization.html)

## 💡 Indices

<details>
<summary>Indice 1 : quelle bibliothèque pour quelle plateforme ?</summary>

Ouvrir une connexion réseau ne se fait pas de la même façon sur Android et sur iOS. Ktor sépare donc son client en deux :

- le **cœur**, commun à toutes les plateformes : c'est avec lui que tu écris ton code ;
- un **moteur** (*engine*) par plateforme, qui fait réellement la requête.

Parmi les six bibliothèques, deux sont des moteurs. La page « dépendances du client » de Ktor te dit lequel va sur quelle plateforme.

</details>

<details>
<summary>Indice 2 : où déclarer le plugin ?</summary>

Regarde comment `composeCompiler` est déclaré : une ligne dans `[plugins]` du catalogue, une ligne `apply false` dans le `build.gradle.kts` **à la racine**, et une ligne dans le bloc `plugins` de `shared/build.gradle.kts`.

Pour que le plugin ait la même version que Kotlin, fais-le pointer vers la même référence de version que `kotlinMultiplatform`.

</details>

<details>
<summary>Indice 3 : la solution</summary>

`gradle/libs.versions.toml`, les lignes à ajouter dans chaque section :

```toml
[versions]
kotlinx-serialization = "1.11.0"
ktor = "3.6.0"

[libraries]
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-contentNegotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinxJson = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
kotlinx-serializationJson = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization" }

[plugins]
kotlinSerialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

`build.gradle.kts`, à la racine du projet, dans le bloc `plugins` :

```kotlin
alias(libs.plugins.kotlinSerialization) apply false
```

`shared/build.gradle.kts` :

```kotlin
plugins {
    // ... les plugins existants
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    // ...
    sourceSets {
        androidMain.dependencies {
            // ... les dépendances existantes
            implementation(libs.ktor.client.okhttp)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            // ... les dépendances existantes
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization.kotlinxJson)
            implementation(libs.kotlinx.serializationJson)
        }
    }
}
```

`data/remote/TickerDto.kt` :

```kotlin
package com.bvic.followmycrypto.data.remote

import kotlinx.serialization.Serializable

// Binance renvoie tous les nombres sous forme de String : "85613.50000000"
@Serializable
data class TickerDto(
    val symbol: String,
    val lastPrice: String,
    val priceChangePercent: String,
)
```

</details>

## 🤔 Pourquoi un moteur par plateforme ?

C'est Kotlin Multiplatform en action. Le code que **tu** écris est dans `commonMain` et n'utilise que `ktor-client-core`. Derrière, chaque plateforme fait la requête avec ses propres outils : OkHttp sur Android et Desktop, Darwin (le réseau d'Apple) sur iOS. Ktor choisit le bon moteur tout seul, selon la plateforme sur laquelle l'application tourne.

C'est le même principe que `expect` / `actual`, vu au TP 1 : une façade commune, une implémentation par plateforme.

> [!NOTE]
> Le JSON de Binance contient une vingtaine de champs et notre DTO trois. Par défaut, kotlinx.serialization refuse un champ qu'il ne connaît pas et lève une erreur. On lui dira de les ignorer à l'étape suivante, avec l'option `ignoreUnknownKeys`.

---

[⬅️ Étape précédente](07-explorer-l-api.md) · [📚 Sommaire](README.md) · [Étape suivante : L'appel réseau ➡️](09-appel-reseau.md)
