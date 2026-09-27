# 1. Avant de commencer

Jetpack Compose est un kit d'outils moderne conçu pour simplifier le développement des interfaces utilisateur. Il allie un modèle de programmation réactif à la concision et à la facilité d'utilisation du langage Kotlin. Il est entièrement **déclaratif** : tu décris ton interface en appelant une série de fonctions qui transforment des données en hiérarchie d'interface. Lorsque les données sont modifiées, le framework réexécute automatiquement ces fonctions et met à jour l'interface pour toi.

Une application Compose est constituée de **fonctions composables** : de simples fonctions annotées avec `@Composable`, qui peuvent appeler d'autres fonctions composables. Tu n'as besoin de rien d'autre qu'une fonction pour créer un élément d'interface. L'annotation indique à Compose d'ajouter une prise en charge spéciale pour la fonction, afin de mettre à jour et de gérer l'interface au fil du temps. Compose te permet ainsi de structurer ton code en petits fragments. Les fonctions composables sont souvent appelées « composables ».

Créer de petits composables réutilisables permet de construire facilement une bibliothèque d'éléments d'interface pour ton application. Chaque composable est responsable d'une partie de l'écran et peut être modifié indépendamment.

> [!NOTE]
> Dans cet atelier, les termes « éléments d'UI », « fonctions composables » et « composables » désignent le même concept et sont utilisés de façon interchangeable.

### Et le « Multiplatform » ?

Compose a été créé par Google pour Android. JetBrains, l'entreprise qui a créé Kotlin, l'a rendu disponible sur **iOS, Desktop et Web** : c'est **Compose Multiplatform**. Le code d'interface que tu vas écrire dans cet atelier est exactement le même que sur Android, mais il tournera aussi sur ton ordinateur et sur iPhone.

## Prérequis

- Connaître la syntaxe du langage Kotlin, y compris les **lambdas**.

> [!TIP]
> Un doute sur une notion de Kotlin ? Teste-la directement dans ton navigateur sur le [Kotlin Playground](https://play.kotlinlang.org/).

## Objectifs de l'atelier

Cet atelier traite des points suivants :

- Présentation de Kotlin Multiplatform et de Compose Multiplatform
- Création d'interfaces utilisateur avec Compose
- Gestion de l'état dans les fonctions composables
- Création d'une liste performante
- Ajout d'animations
- Application d'un style et d'un thème à une application

Tu vas créer une application avec un écran d'accueil ainsi qu'une liste d'éléments déroulants animés, qui fonctionne sur Android, iOS et Desktop :

![Résultat final de l'atelier](img/google-8d24a786bfe1a8f2.gif)

## Ce dont tu as besoin

- **Android Studio** 2026.1 ou plus récent
- Le **plugin Kotlin Multiplatform** pour Android Studio (on l'installe à l'étape 2)
- Un **émulateur Android** ou un téléphone Android (on le configure à l'étape 3)
- **Sur Mac uniquement, optionnel :** Xcode, pour lancer l'application sur iOS

## Si tu es perdu

Chaque fin de bloc correspond à un **tag Git**. Si tu es bloqué ou en retard, tu peux repartir de la correction :

```bash
git stash              # met ton travail de côté
git checkout <tag>     # ex : git checkout tp1-start
```

La liste des tags et les zips à télécharger sont dans le [README du dépôt](../../README.md).

---

[📚 Sommaire](README.md) · [Étape suivante : Kotlin Multiplatform et le projet ➡️](02-kotlin-multiplatform.md)
