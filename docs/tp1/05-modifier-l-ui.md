# 5. Modifier l'UI

> **Bloc 1** · Étapes 4 à 7 : composables, `Modifier`, `Row` / `Column`

Commençons par définir une couleur d'arrière-plan différente pour `Greeting`. Pour cela, encapsule le composable `Text` dans une `Surface`. `Surface` accepte une couleur : utilise `MaterialTheme.colorScheme.primary`.

> [!NOTE]
> `Surface` et `MaterialTheme` sont des concepts liés à **Material Design**, un système de conception créé par Google pour t'aider à concevoir des interfaces et des expériences utilisateur.

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Text(
            text = "Hello $name!",
            modifier = modifier,
        )
    }
}
```

Les composants imbriqués dans `Surface` s'affichent par-dessus cette couleur d'arrière-plan.

> [!NOTE]
> Quand tu modifies le code, l'aperçu doit s'actualiser automatiquement. Si ce n'est pas le cas, un bouton **Build & Refresh** apparaît en haut de l'aperçu : clique dessus ou compile le projet pour voir les modifications.

Tu peux voir les modifications dans l'aperçu ou sur Desktop :

![Texte sur fond coloré](img/google-c88121ec49bde8c7.png)

Tu as peut-être raté un détail important : **la couleur du texte a changé**. Comment est-ce possible ?

Tu n'as pourtant rien fait. Les composants Material Design, comme `androidx.compose.material3.Surface`, sont conçus pour te faciliter la tâche en gérant les fonctionnalités courantes dont une application a presque toujours besoin, comme choisir une couleur de texte lisible. On dit que Material Design est « opinionated » (catégorique) : il choisit des valeurs par défaut et des modèles pratiques, communs à la plupart des applications. Les composants Material de Compose reposent sur des composants de base (dans `androidx.compose.foundation`), que tu peux aussi utiliser directement si tu as besoin de plus de flexibilité.

Ici, `Surface` comprend que, lorsque l'arrière-plan est de couleur `primary`, tout texte posé dessus doit utiliser la couleur `onPrimary`, elle aussi définie dans le thème. On y reviendra à l'[étape 13](13-style-et-theme.md).

> [!TIP]
> Pour parcourir tous les composants Material Design disponibles, consulte la [documentation Material 3 pour Compose](https://developer.android.com/develop/ui/compose/designsystems/material3).

## Les modificateurs

La plupart des éléments d'interface de Compose, comme `Surface` et `Text`, acceptent un paramètre facultatif `modifier`. **Les modificateurs indiquent à un élément comment s'afficher ou se comporter dans son parent.** Tu as peut-être remarqué que le composable `Greeting` a déjà un paramètre `modifier`, qu'il transmet ensuite à `Text`.

Par exemple, le modificateur `padding` ajoute de l'espace autour de l'élément qu'il décore. On crée un modificateur de marge intérieure avec `Modifier.padding()`. On peut aussi enchaîner plusieurs modificateurs : ici, on ajoute la marge au modificateur reçu en paramètre, avec `modifier.padding(24.dp)`.

Ajoute une marge intérieure à ton `Text` :

```kotlin
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
// ...

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(24.dp),
        )
    }
}
```

![Texte avec une marge intérieure](img/google-ef14f7c54ae7edf.png)

> [!NOTE]
> **`dp`** (*density-independent pixel*) est l'unité de taille de Compose. 24 dp font à peu près la même taille physique sur tous les écrans, quelle que soit leur densité de pixels.

Il existe des dizaines de modificateurs pour aligner, animer, mettre en page ou transformer des éléments, ou encore pour les rendre cliquables ou les faire défiler. La liste complète est dans la [liste des modificateurs Compose](https://developer.android.com/develop/ui/compose/modifiers-list). Tu en utiliseras plusieurs dans les prochaines étapes.

---

[⬅️ Étape précédente](04-premiers-pas.md) · [📚 Sommaire](README.md) · [Étape suivante : Réutiliser des composables ➡️](06-reutiliser-des-composables.md)
