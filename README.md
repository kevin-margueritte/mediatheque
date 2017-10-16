# Epic 1 - Mediathèque

## Contexte

L'objectif de ce projet de simuler une médiathèque en ligne, dans laquelle un utilisateur va pouvoir y ajouter des films par API.
Une version est en production  [ici](https://mediatheque-mrgueritte.herokuapp.com/)

## Technologies

La liste des technologies utilisées est :

  - Play 2 : 2.6
  - Slick : 3.2
  - Scala : 2.12.3

## Scénarios de test

Disponible sous forme de projet postman dans le dossier test

## Deploiement
### Base de données

Le script de génération du schéma se trouve dans le dossier /conf

#### Génération de clé secrète

Pour mettre en production l'application, il faut renseigner une clé secrète
```sh
$ sbt
$ playUpdateSecret
```