# 2. L'évaluation

Le projet est noté sur 20. Si vous travaillez à deux, la note est celle du binôme.

La grille est la même pour tous. Seul le **nombre** d'évolutions attendu tient compte du fait que vous êtes un ou deux.

## 📊 La grille

| Critère | Points |
|---|---|
| Au moins une évolution 🧱 fonctionne | 4 |
| Les évolutions au choix | 8 |
| Le respect de l'architecture | 4 |
| La démonstration | 4 |

### Au moins une évolution 🧱 (4 points)

Les évolutions marquées 🧱 dans [la liste](01-evolutions.md) font voyager une donnée de la couche données jusqu'à l'interface. Il vous en faut au moins une qui fonctionne : c'est elle qui montre que vous savez faire, seuls, le trajet vu en séance.

### Les évolutions au choix (8 points)

Sont pris en compte :

- **le nombre** d'évolutions terminées ;
- **leur difficulté** : une ⭐⭐⭐ compte davantage qu'une ⭐ ;
- **leur finition** : une évolution est terminée quand elle fonctionne pendant la démonstration **et** qu'elle respecte ses contraintes.

Mieux vaut deux évolutions terminées que quatre à moitié faites.

### Le respect de l'architecture (4 points)

C'est ce que vous avez appris au TP 2, appliqué à votre code :

- Chaque chose est dans **sa couche** : pas d'appel réseau dans un ViewModel, pas de tri ni de filtre dans un composable.
- L'écran est décrit par un **état de l'interface**, exposé en lecture seule par le ViewModel.
- Les composables reçoivent des **données** et remontent des **événements** ; ceux qui affichent du contenu ont un aperçu.
- Les erreurs sont **gérées** : l'application ne plante pas sans réseau.
- L'application **se lance sur Android et sur Desktop**.

### La démonstration (4 points)

Voir ci-dessous.

## 🎤 La démonstration

Ce n'est **pas une présentation devant la classe**. Pendant la deuxième moitié de la dernière séance, vous venez me voir à mon bureau, un par un ou binôme par binôme, avec votre ordinateur. Les autres continuent à travailler pendant ce temps.

La durée de chaque passage sera annoncée en séance : elle dépend du nombre de projets.

Ce qui est attendu :

- Vous me montrez l'application, puis le code d'une évolution de votre choix.
- Je peux vous interroger sur **n'importe quelle partie** du code.
- En binôme, les **deux** parlent, et chacun doit pouvoir expliquer le code écrit par l'autre.
- Vous dites ce qui **ne marche pas** ou ce que vous n'avez pas eu le temps de finir : c'est mieux vu qu'une démonstration qui évite le problème.

Quelques questions que vous pouvez préparer :

- Quelles couches cette évolution a-t-elle touchées, et pourquoi pas les autres ?
- Qu'est-ce qui a été le plus difficile ? Comment avez-vous trouvé la solution ?
- Si l'API changeait demain, que faudrait-il modifier ?
- Qu'auriez-vous fait avec deux heures de plus ?

## 📦 Le rendu

À rendre **à la fin de la dernière séance**, de l'une des deux façons suivantes.

### Avec une pull request

Si vous avez travaillé avec Git (option A du [point de départ](README.md#-le-point-de-départ)) :

1. Poussez votre branche sur votre fork.
2. Sur GitHub, ouvrez une **pull request** de votre branche vers la branche `main` du dépôt [bvic-dev/kmp-from-zero](https://github.com/bvic-dev/kmp-from-zero).
3. Donnez-lui pour titre `Projet – Dupont`, ou `Projet – Dupont & Martin` en binôme.

La pull request ne sera pas fusionnée : elle sert à rendre votre travail, et à le relire.

### Avec un zip

Sinon, envoyez le dossier du projet compressé. Supprimez d'abord les dossiers `build` et `.gradle`, qui sont très volumineux et ne servent à rien.

### Dans les deux cas : la description

Dans la description de la pull request, ou dans un fichier `RENDU.md` à la racine du zip :

- votre nom, ou les deux noms du binôme ;
- la liste des évolutions réalisées, avec leur état : terminée, partielle ou abandonnée ;
- ce qui ne fonctionne pas, si vous le savez.

---

[⬅️ Les évolutions](01-evolutions.md) · [📚 Sommaire](README.md)
