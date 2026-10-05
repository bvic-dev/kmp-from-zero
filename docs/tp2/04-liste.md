# 4. La liste

> **Bloc 1** · Étapes 2 à 4 : construire l'interface, avec de fausses données
>
> 🧱 **Composable** → ViewModel → Repository → API

Tu as une ligne. Il reste à en afficher une par crypto.

## 🎯 Résultat attendu

![La liste des cryptos](img/03-crypto-list.png)

## 📦 Ce qui est fourni

Comme à l'étape précédente, le branchement dans l'écran est donné. Dans `CryptoListScreen`, remplace le contenu du `Scaffold` :

```kotlin
    ) { innerPadding ->
        CryptoList(cryptos = mockCryptos, modifier = Modifier.padding(innerPadding))
    }
```

`CryptoList` est souligné en rouge tant que tu ne l'as pas écrit.

## 📏 Contraintes

- Un composable `CryptoList`, qui reçoit une `List<Crypto>` et un `modifier`.
- La liste est **performante** : seules les lignes visibles sont composées.
- Chaque ligne est identifiée par une **clé** stable : le symbole de la crypto.
- Les lignes sont séparées par un trait fin.

## 📚 Documentation

- [Les listes et les grilles, dont les clés d'éléments](https://developer.android.com/develop/ui/compose/lists)
- [Les séparateurs (*dividers*)](https://developer.android.com/develop/ui/compose/components/divider)

## 💡 Indices

<details>
<summary>Indice 1 : quel composable ?</summary>

Tu l'as utilisé au TP 1, étape 10 : `LazyColumn` et sa fonction `items`.

Pour le trait de séparation, cherche « Divider » dans l'autocomplétion.

</details>

<details>
<summary>Indice 2 : une clé, pour quoi faire ?</summary>

`items` a un paramètre `key`. Sans clé, Compose identifie une ligne par sa **position**. Si l'ordre de la liste change (un tri, par exemple), il croit que toutes les lignes ont changé de contenu.

Avec une clé, il sait que « la ligne BTC » s'est déplacée : il garde son état et peut même animer le déplacement.

</details>

<details>
<summary>Indice 3 : la solution</summary>

```kotlin
@Composable
fun CryptoList(cryptos: List<Crypto>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = cryptos, key = { it.symbol }) { crypto ->
            CryptoRow(crypto = crypto)
            HorizontalDivider()
        }
    }
}
```

</details>

---

## 🏁 Fin du bloc 1

Tu as un écran complet, découpé en trois composables qui ont chacun un seul rôle :

| Composable | Son rôle |
|---|---|
| `CryptoListScreen` | La structure de l'écran : barre de titre et contenu |
| `CryptoList` | Afficher une liste de cryptos |
| `CryptoRow` | Afficher une crypto |

Mais les prix sont faux, et ils sont **écrits dans le fichier de l'interface**. C'est ce qu'on va corriger, couche par couche.

Si tu es en retard ou bloqué, récupère la correction du bloc :

```bash
git stash
git checkout tp2-bloc1
```

---

[⬅️ Étape précédente](03-crypto-row.md) · [📚 Sommaire](README.md) · [Étape suivante : Le ViewModel ➡️](05-viewmodel.md)
