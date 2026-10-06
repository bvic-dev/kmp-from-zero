# 11. 🎉 Récapitulatif

Bravo ! Ton application affiche de vraies données, et tu as construit toi-même son architecture, couche par couche.

## 🧠 À retenir

**Les couches**

```
┌──────────────┐  état   ┌──────────────┐          ┌──────────────┐          ┌──────────────┐
│  Composable  │ ◀────── │  ViewModel   │ ───────▶ │  Repository  │ ───────▶ │     API      │
│              │ ──────▶ │              │ ◀─────── │              │ ◀─────── │              │
└──────────────┘ événem. └──────────────┘  modèle  └──────────────┘   DTO    └──────────────┘
   ui/list                  ui/list             data                  data/remote
```

| Couche | Son rôle | Dans le projet |
|---|---|---|
| Composable | Afficher un état, remonter les actions de l'utilisateur | `CryptoListScreen`, `CryptoListContent`, `CryptoList`, `CryptoRow` |
| ViewModel | Garder l'état de l'écran, décider comment il évolue | `CryptoListViewModel`, `CryptoListUiState` |
| Repository | Fournir les données, convertir les DTO en modèles | `CryptoRepository`, `RemoteCryptoRepository`, `FakeCryptoRepository` |
| API | Parler au serveur | `BinanceApi`, `TickerDto` |

Cette organisation porte un nom : **MVVM**, pour *Model – View – ViewModel*. La *View* est le composable, le *Model* regroupe les données et tout ce qui les fournit.

**Les notions**

| Notion | En une phrase |
|---|---|
| `Scaffold` | La structure d'un écran : barres et contenu. Son `innerPadding` évite de dessiner sous les barres. |
| `ViewModel` | Une classe qui survit aux recompositions et aux rotations, et garde l'état d'un écran |
| `Flow` | Une suite de valeurs qui arrivent au fil du temps |
| `StateFlow` | Un flow qui a toujours une valeur courante : fait pour exposer un état |
| `MutableStateFlow` / `StateFlow` | La version modifiable reste privée, la version en lecture seule est exposée |
| `collectAsStateWithLifecycle()` | Collecte un `StateFlow` dans un composable et déclenche la recomposition |
| État de l'interface (*UI state*) | Un type qui décrit tout ce que l'écran peut afficher : chargement, erreur, succès. Centralisé dans le ViewModel, c'est la seule source de vérité de l'écran |
| `sealed interface` | Une interface dont tous les cas sont connus : le `when` est vérifié par le compilateur |
| `suspend` | Une fonction qui peut se mettre en pause sans bloquer l'interface |
| `viewModelScope.launch` | Démarre une coroutine, annulée automatiquement quand le ViewModel est détruit |
| Repository | Le seul endroit qui sait d'où viennent les données |
| Interface + implémentations | Un contrat, et plusieurs façons de le remplir : la fausse, la vraie |
| Injection de dépendances | Une classe **reçoit** ce dont elle a besoin au lieu de le créer |
| DTO | Le miroir exact du JSON, qui ne sert qu'à lire la réponse |
| Mapper | La fonction qui convertit un DTO en modèle |
| Catalogue de versions | `libs.versions.toml` : toutes les bibliothèques du projet et leurs versions, à un seul endroit |
| Ktor | Le client HTTP multiplateforme, avec un moteur par plateforme |
| Composable avec / sans état | `Screen` connaît le ViewModel ; `Content` ne reçoit que des données, et a un aperçu |

## 🧪 Vérifie que tu as compris

Essaie de répondre sans regarder le code :

1. L'utilisateur tourne son téléphone pendant un chargement. Que devient la requête ? Que voit-il après la rotation ?
2. Binance renomme le champ `lastPrice` en `price`. Quels fichiers dois-tu modifier ?
3. On veut afficher les prix en euros. Quelle couche est concernée ?
4. Pourquoi l'interface ne peut-elle pas écrire `viewModel.uiState.value = ...` ?
5. À quoi sert encore `FakeCryptoRepository`, maintenant que l'API fonctionne ?

<details>
<summary>💡 Les réponses</summary>

1. Rien ne change pour la requête : elle tourne dans `viewModelScope`, et le ViewModel survit à la rotation. Le composable est recréé, collecte de nouveau `uiState` et affiche l'état courant : l'indicateur de chargement, puis la liste.
2. Un seul : `TickerDto` (et le mapper qui lit ce champ). Le modèle `Crypto`, le ViewModel et l'interface ne voient jamais le DTO.
3. Le repository et l'API : il faut demander d'autres paires (`BTCEUR`). L'affichage du symbole « € » relève de `Format.kt`. Le ViewModel ne change pas.
4. Parce que `uiState` est exposé comme un `StateFlow`, en lecture seule. Seul `_uiState`, privé, est modifiable.
5. À travailler sans réseau, à écrire des aperçus et des tests, et à reproduire des cas difficiles à provoquer : erreur, lenteur, liste vide.

</details>

## 🏷️ La correction

| Tag | Contenu |
|---|---|
| `tp2-start` | Le point de départ |
| `tp2-bloc1` | L'interface, sur de fausses données |
| `tp2-bloc2` | Le ViewModel, le repository et les états de l'écran |
| `tp2-bloc3` | L'appel à l'API Binance |
| `tp2-end` | Le bonus : navigation et écran de détail |

## ➡️ La suite

Au TP 3, on ajoute la navigation et l'écran de détail, puis tu fais évoluer l'application **en autonomie** : favoris, recherche, tri, graphique… Tu as maintenant toutes les couches pour le faire.

---

[⬅️ Étape précédente](10-navigation-et-detail.md) · [📚 Sommaire](README.md)
