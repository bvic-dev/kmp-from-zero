# 📱 Kotlin Multiplatform from zero

Cours de développement mobile multiplateforme avec **Kotlin Multiplatform (KMP)** et **Compose Multiplatform (CMP)**.

Au fil des séances, on construit ensemble **FollowMyCrypto** : une application **Android, iOS et Desktop** qui affiche le cours des cryptomonnaies, avec un **seul code Kotlin partagé**, interface comprise.

<!-- 📸 TODO : capture de l'app finale FollowMyCrypto (Android + iOS + Desktop côte à côte) -->

---

## 👋 Le cours

<!-- TODO : 2-3 lignes sur toi (poste, entreprise, expérience mobile) -->

**Format :** 4 séances (3 × 4h + 1 × 3h).

**Aucun prérequis en développement mobile.** Des bases de programmation suffisent.

---

## 📚 Sommaire

| Séance | Sujet | Support |
|---|---|---|
| 1 | Découverte de KMP/CMP, les bases de Compose | [TP 1](docs/tp1/README.md) |
| 2 | FollowMyCrypto : liste, état et navigation | *à venir* |
| 3 | Appels réseau et gestion des états | *à venir* |
| 4 | Pour aller plus loin, et évaluation | *à venir* |

---

## 🛠️ Installation

1. Installe [Android Studio](https://developer.android.com/studio) (version 2026.1 ou plus récente).
2. Installe le [plugin Kotlin Multiplatform](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform) : dans Android Studio, ouvre `Settings > Plugins`, onglet **Marketplace**, cherche **Kotlin Multiplatform** puis **Install**.
3. Clone ce dépôt et ouvre-le dans Android Studio (`File > Open`) :

   ```bash
   git clone https://github.com/bvic-dev/kmp-from-zero.git
   ```

4. Attends la fin de la première synchronisation Gradle.
5. Crée un émulateur Android (voir [TP 1, étape 3](docs/tp1/03-lancer-l-application.md)).
6. **Sur Mac uniquement :** installe Xcode depuis l'App Store et ouvre-le une fois pour accepter la licence.

✅ Tu es prêt si l'application se lance sur Desktop (configuration **desktopApp**).

---

## 🏷️ Les étapes (tags)

Chaque étape du cours est marquée par un **tag Git**. Si tu es perdu ou en retard, tu peux repartir de l'étape suivante :

```bash
git stash              # met ton travail de côté
git checkout <tag>     # ex : git checkout tp1-start
```

Pas à l'aise avec Git ? Télécharge directement le zip de l'étape.

| Tag | Contenu | Code | Zip |
|---|---|---|---|
| `tp1-start` | Projet généré par le wizard KMP | [voir](https://github.com/bvic-dev/kmp-from-zero/tree/tp1-start) | [⬇️](https://github.com/bvic-dev/kmp-from-zero/archive/refs/tags/tp1-start.zip) |

*D'autres tags seront ajoutés au fil des séances.*

---

## 🚀 Lancer l'application

Depuis la barre d'outils d'Android Studio, choisis une configuration puis clique sur ▶️ :

| Plateforme | Configuration | En ligne de commande |
|---|---|---|
| 🖥️ Desktop | `desktopApp [hot]` | `./gradlew :desktopApp:hotRun --auto` |
| 🤖 Android | `androidApp` | `./gradlew :androidApp:installDebug` |
| 🍏 iOS (Mac) | `iosApp` | – |

---

## 🔗 Ressources

- [Documentation Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Documentation Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)
- [Kotlin Playground](https://play.kotlinlang.org/) : tester du Kotlin dans le navigateur
- [Codelab Google : Les bases de Jetpack Compose](https://developer.android.com/codelabs/jetpack-compose-basics?hl=fr)
