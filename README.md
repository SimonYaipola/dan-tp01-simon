Mini Gestionnaire de Tâches

Application Java native développée avec Swing et JDBC, permettant la gestion complète de tâches avec persistance en base de données.

Projet réalisé dans le cadre du cours Développement d’applications natives.

Fonctionnalités

Ajouter une tâche

Modifier une tâche

Supprimer une tâche

Afficher les tâches dans un tableau

Gestion de la priorité (LOW, MEDIUM, HIGH)

Gestion du statut (TODO, IN_PROGRESS, DONE)

Gestion d’une date limite

Modes de base de données

L’application permet de choisir au démarrage :

Mode Online : connexion à MariaDB

Mode Local : base H2 en mode fichier

Structure du projet
src/main/java/
  ├── model/
  ├── dao/
  ├── ui/
  ├── config/
  └── Main.java

model : objets métier

dao : accès aux données

ui : interface Swing

config : gestion connexion et configuration BD

Technologies utilisées

Java

Swing

JDBC

MariaDB

H2

Maven

Exécution

Compilation :

mvn clean install

Exécution :

mvn exec:java
Auteur

Simon-Pier Larouche
Technique de l’informatique
Cégep de la Gaspésie et des Îles
