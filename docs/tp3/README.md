# TP 3 — FollowMyCrypto : à toi de jouer

Les deux dernières séances sont un **projet en binôme** : vous partez de l'application du TP 2 et vous la faites évoluer. C'est ce travail qui est noté.

| # | Page |
|---|---|
| 1 | [Les évolutions](01-evolutions.md) : ce que vous pouvez ajouter à l'application |
| 2 | [L'évaluation](02-evaluation.md) : la grille de notation et la présentation |

## 🗓️ Le déroulé

**Séance 3.** Je présente la fin du TP 2, des étapes 7 à 10 : [l'API Binance](../tp2/07-explorer-l-api.md), [les bibliothèques](../tp2/08-bibliotheques.md), [l'appel réseau](../tp2/09-appel-reseau.md), puis [la navigation et l'écran de détail](../tp2/10-navigation-et-detail.md). Ensuite, les projets commencent.

**Séance 4.** Vous continuez le projet, puis chaque binôme présente son travail.

## 🏁 Le point de départ

Tout le monde repart du même code : la fin du TP 2, avec la liste, l'appel à l'API Binance, la navigation et l'écran de détail. C'est le contenu de la branche `main` du dépôt.

Vous avez le choix entre deux façons de travailler. La première est celle qu'on utilise en entreprise ; la seconde convient très bien si vous n'êtes pas à l'aise avec Git.

### Option A : avec Git et GitHub

1. **Un seul** des deux crée une copie du dépôt sur son compte GitHub : le bouton **Fork**, en haut à droite de [la page du dépôt](https://github.com/bvic-dev/kmp-from-zero).
2. Il y invite son binôme : dans le fork, `Settings > Collaborators > Add people`.
3. Chacun récupère le fork sur sa machine et se place sur la branche du projet, nommée avec vos deux noms :

   ```bash
   git clone https://github.com/<votre-compte>/kmp-from-zero.git
   cd kmp-from-zero
   git switch -c projet-dupont-martin      # le premier la crée et la pousse
   git push -u origin projet-dupont-martin
   ```

   Le second la récupère avec `git switch projet-dupont-martin` après son `git clone`.

Vous pouvez alors travailler **en parallèle**, chacun sur son poste. Pensez à `git pull` avant de commencer une évolution, et à `git push` dès qu'elle fonctionne.

### Option B : sans Git

Téléchargez [le zip de la branche `main`](https://github.com/bvic-dev/kmp-from-zero/archive/refs/heads/main.zip), décompressez-le et ouvrez le dossier dans Android Studio. Vous travaillez à deux sur le même poste.

### Dans les deux cas

✅ Vous êtes prêts si l'application affiche les vrais cours et qu'un clic sur une crypto ouvre son détail.

Le rendu, à la fin de la dernière séance, est décrit dans [l'évaluation](02-evaluation.md#-le-rendu).

## 🧭 Avant d'écrire du code

Prenez dix minutes pour faire le tour de ce que vous avez entre les mains. Pour chaque fichier, vous devez pouvoir dire à quelle couche il appartient :

```
shared/src/commonMain/kotlin/com/bvic/followmycrypto/
├── App.kt                         la navigation
├── model/Crypto.kt                le modèle
├── data/
│   ├── CryptoRepository.kt        le contrat du repository
│   ├── RemoteCryptoRepository.kt  l'implémentation réseau, et le mapper
│   ├── FakeCryptoRepository.kt    l'implémentation de test
│   ├── AppContainer.kt            l'injection de dépendances
│   └── remote/                    l'API et le DTO
└── ui/
    ├── list/                      l'écran liste : composables, ViewModel, état
    ├── detail/                    l'écran de détail : composables, ViewModel, état
    ├── components/                les composables partagés
    └── format/                    l'affichage des nombres
```

Pour chaque évolution, posez-vous la même question avant de commencer : **quelles couches faut-il modifier ?**

## 🤝 Travailler à deux

- **Découpez** : une évolution qui touche la liste et une qui touche le détail peuvent avancer en parallèle. Deux évolutions qui modifient le même fichier, beaucoup moins.
- **Commitez souvent**, une évolution à la fois. Un commit qui marche vaut mieux qu'une évolution à moitié finie le jour de la présentation.
- **Chacun doit pouvoir expliquer tout le code**, y compris celui écrit par l'autre : c'est vérifié à la présentation.

---

[Les évolutions ➡️](01-evolutions.md)
