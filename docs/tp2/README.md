# TP 2 — FollowMyCrypto : la liste des cryptos, de l'écran à l'API

Dans ce TP, on construit le premier écran de FollowMyCrypto : la liste des cryptos, avec leur prix et leur variation sur 24h. On part d'un écran vide et on termine avec les vrais cours, récupérés chez Binance.

| # | Étape | Bloc | Durée indicative |
|---|---|---|---|
| 1 | [Avant de commencer](01-avant-de-commencer.md) | Démarrage | 10 min |
| 2 | [L'écran et sa barre de titre](02-scaffold.md) | 1 | 15 min |
| 3 | [Une ligne de crypto](03-crypto-row.md) | 1 | 30 min |
| 4 | [La liste](04-liste.md) | 1 | 15 min |
| 5 | [⭐ Le ViewModel](05-viewmodel.md) | 2 | 40 min |
| 6 | [⭐ Le repository et les états de l'écran](06-repository-et-etats.md) | 2 | 45 min |
| 7 | Explorer l'API Binance *(à venir)* | 3 | 20 min |
| 8 | Ajouter les bibliothèques *(à venir)* | 3 | 25 min |
| 9 | L'appel réseau *(à venir)* | 3 | 40 min |
| 10 | Navigation et écran de détail *(à venir)* | 🚀 Bonus | – |
| 11 | Récapitulatif *(à venir)* | – | – |

On avance **par blocs**, avec un point en commun entre chaque bloc. Si tu as fini un bloc avant les autres, continue directement sur le suivant.

## 🔎 Ce qui change par rapport au TP 1

Le TP 1 te donnait le code à écrire. Ici, **c'est toi qui cherches**. Chaque étape te donne :

- 🎯 **le résultat attendu** : une capture d'écran ou un comportement ;
- 📏 **les contraintes** à respecter ;
- 📦 **ce qui est fourni** : à copier tel quel ;
- 📚 **la documentation** officielle des notions de l'étape ;
- 💡 **des indices**, repliés, du plus vague au plus précis. Le dernier est la solution.

Essaie d'abord sans indice. Tu as trois sources, à utiliser dans cet ordre :

1. **la documentation** liée dans chaque étape : c'est là que sont les explications et les exemples ;
2. **l'autocomplétion** d'Android Studio (`Ctrl+Espace`), pour découvrir les paramètres d'un composable ;
3. **le code source** : un `Ctrl+clic` (`Cmd+clic` sur Mac) sur n'importe quelle fonction ouvre sa définition et son commentaire de documentation.

Savoir chercher dans une documentation est une compétence à part entière : c'est ce que tu feras tous les jours en entreprise.
 Ouvre un indice quand tu bloques depuis plus de cinq minutes, pas avant.

Tu n'es pas obligé d'obtenir le même code que la solution : si le résultat est le même et que les contraintes sont respectées, c'est bon.

## 🧱 Le fil rouge : les couches de l'application

À la fin du TP, afficher la liste fera intervenir quatre couches. On les ajoute **une par une**, et le bandeau en haut de chaque page indique celle sur laquelle tu travailles :

> 🧱 Composable → ViewModel → Repository → API

| Couche | Son rôle | Elle ne sait pas… |
|---|---|---|
| **Composable** | Afficher un état, remonter les actions de l'utilisateur | d'où viennent les données |
| **ViewModel** | Garder l'état de l'écran et décider comment il évolue | comment l'écran est dessiné |
| **Repository** | Fournir les données au reste de l'application | qui les affiche |
| **API** | Parler au serveur | ce que l'application fait des réponses |
