# 15. 🎉 Félicitations

Bravo ! Tu as appris les principes de base de Compose, et ton application fonctionne sur **Android, iOS et Desktop** avec un seul code.

## 🧠 À retenir

**Kotlin Multiplatform et Compose Multiplatform**

| Notion | En une phrase |
|---|---|
| `commonMain` | Le code partagé par toutes les plateformes : logique **et** interface avec CMP |
| `androidMain` / `iosMain` / `jvmMain` | Le code Kotlin propre à une plateforme, qui a accès à ses API |
| `expect` / `actual` | Déclarer une fonction dans `commonMain`, l'implémenter pour chaque plateforme |
| `androidApp` / `iosApp` / `desktopApp` | Des coquilles qui affichent la même fonction `App()` |
| `composeResources` | Les ressources partagées (textes, images), accessibles via la classe `Res` |

**Compose**

| Notion | En une phrase |
|---|---|
| `@Composable` | Une fonction qui décrit un morceau d'interface |
| `@Preview` | Voir le rendu d'un composable sans lancer l'app |
| `Modifier` | Taille, marges, fond, clic… d'un composable. **L'ordre compte !** |
| `Column` / `Row` / `Box` | Empiler verticalement / horizontalement / superposer |
| `mutableStateOf` | Une valeur observée par Compose : quand elle change, l'interface se **recompose** |
| `remember` | Garde une valeur entre deux recompositions |
| `rememberSaveable` | Garde aussi la valeur si l'écran est recréé (rotation, arrêt du processus) |
| Hissage d'état | L'état **descend** en paramètre, les événements **remontent** par des callbacks |
| `LazyColumn` | Une liste performante : seuls les éléments visibles sont affichés |
| `animate*AsState` | Anime une valeur vers une cible |
| `MaterialTheme` | Couleurs, typographie et formes, transmises en cascade à tous les enfants |

## 🏷️ La correction

Le code final de ce TP est sur le tag `tp1-end` :

```bash
git stash
git checkout tp1-end
```

## Et maintenant ?

Lors de la prochaine séance, on démarre **FollowMyCrypto** : une liste de cryptos, un écran de détail et la navigation entre les deux.

## Pour aller plus loin

- [Le modèle mental de Compose](https://developer.android.com/develop/ui/compose/mental-model) : comprendre la recomposition en profondeur
- [L'état dans Compose](https://developer.android.com/develop/ui/compose/state)
- [La documentation Compose Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform.html)
- [Les autres ateliers Compose de Google](https://developer.android.com/courses/jetpack-compose/course) : mises en page, état, thèmes…

---

[⬅️ Étape précédente](14-touches-finales.md) · [📚 Sommaire](README.md)
