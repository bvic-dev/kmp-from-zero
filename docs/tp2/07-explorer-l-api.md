# 7. Explorer l'API Binance

> **Bloc 3** · Étapes 7 à 9 : remplacer les fausses données par les vraies
>
> 🧱 Composable → ViewModel → Repository → **API**

Avant d'écrire du code qui appelle une API, on la regarde. Dans cette étape, tu n'ouvres presque pas Android Studio.

## L'API

Binance met à disposition une API publique des cours, **sans clé ni inscription**. Celle qui nous intéresse donne les statistiques des dernières 24 heures :

```
https://data-api.binance.vision/api/v3/ticker/24hr?symbols=["BTCUSDT","ETHUSDT"]
```

`BTCUSDT` est une **paire** : le prix du Bitcoin (BTC) exprimé en USDT, une crypto dont la valeur suit celle du dollar.

## 🔎 À toi d'enquêter

Ouvre cette adresse dans ton navigateur (ou avec `curl`), puis réponds à ces questions. Note tes réponses : tu en auras besoin dans les deux étapes suivantes.

👉 [Ouvrir l'API dans le navigateur](https://data-api.binance.vision/api/v3/ticker/24hr?symbols=%5B%22BTCUSDT%22,%22ETHUSDT%22%5D)

1. La réponse est-elle un objet ou un tableau ?
2. Quels champs correspondent au **prix actuel** et à la **variation sur 24h en pourcentage** ?
3. De quel **type** sont ces deux valeurs dans le JSON ? Regarde bien les guillemets.
4. Où est le **nom** de la crypto (« Bitcoin ») ?
5. Le champ `symbol` contient-il la même chose que le `symbol` de notre classe `Crypto` ?
6. Combien de champs contient chaque élément ? De combien a-t-on besoin ?

<details>
<summary>💡 Les réponses</summary>

1. Un **tableau** d'objets, un par paire demandée.
2. `lastPrice` et `priceChangePercent`.
3. Ce sont des **chaînes de caractères** : `"lastPrice": "85613.50000000"`. Binance évite ainsi les erreurs d'arrondi des nombres à virgule.
4. **Nulle part.** L'API ne connaît que les symboles.
5. Non : l'API renvoie la paire (`BTCUSDT`), notre modèle attend le symbole seul (`BTC`).
6. Une vingtaine. On a besoin de trois : `symbol`, `lastPrice` et `priceChangePercent`.

</details>

## Deux classes pour une crypto : le DTO et le modèle

Tu viens de le constater : ce que l'API envoie **ne ressemble pas** à ce dont l'application a besoin.

| | Ce que l'API envoie | Ce que l'application veut |
|---|---|---|
| Symbole | `"BTCUSDT"` | `"BTC"` |
| Nom | absent | `"Bitcoin"` |
| Prix | `"85613.50000000"`, une `String` | `85613.5`, un `Double` |

On pourrait modifier notre classe `Crypto` pour qu'elle colle à l'API. Mais alors toute l'application, jusqu'à `CryptoRow`, dépendrait des choix de Binance : le jour où l'API change, ou si on change de fournisseur, il faudrait tout réécrire.

On utilise donc **deux classes** :

- un **DTO** (*Data Transfer Object*), qui est le **miroir exact du JSON**. Il ne sert qu'à lire la réponse ;
- le **modèle** `Crypto`, que tu as déjà, taillé pour l'application.

Entre les deux, une fonction de conversion : le **mapper**. C'est le repository qui fait cette conversion, et c'est ce qui protège le reste de l'application.

```
        JSON  ──▶  TickerDto  ──▶  Crypto  ──▶  ViewModel, interface
                   (API)   mapper  (modèle)
```

À l'étape suivante, on installe de quoi lire ce JSON, puis on écrit le DTO.

## 📚 Documentation

- [La documentation de l'API Binance : *24hr ticker price change statistics*](https://developers.binance.com/docs/binance-spot-api-docs/rest-api/market-data-endpoints)
- [Les recommandations d'architecture, pour replacer le DTO et le modèle dans l'ensemble](https://developer.android.com/topic/architecture)

---

[⬅️ Étape précédente](06-repository-et-etats.md) · [📚 Sommaire](README.md) · [Étape suivante : Ajouter les bibliothèques ➡️](08-bibliotheques.md)
