
# Washing-MEchine

## Introduction

Notre projet a pour objectif de déterminer de créer une inteface web et mobile afin que l'on puisse savoir quelle machine sont active   ou non à la ME avant d'aller à la laverie.

### Embarqué

Ainsi, initialement nous avions prévu d'installer des capteurs de tensions sur les machines de la ME afin de détecter une différence de voltage pour déterminer si la machine était en cours de fonctionnement ou non. Seulement par manque de moyens matériel nous avons simplifié le modèle embarqué. Nous avons choisi de prendre un moteur pour simuler la machine à laver ainsi qu'un capteur de tension limité à des voltages plus bas pour capter l'activité du moteur.

Le but de notre système embarqué est de détecter l'activité de notre moteur que nous actionnerons manuellement à l'aide d'un interrupteur et déclencher un décompte pour le temps restant avant que la machine soit libre. Notre microcontrôleur enverra par la suite les informations reçues à notre API.

Nous avons rencontrés quelques problèmes pour nous connecter au WiFi car ce WiFi requiert une authentification depuis une page web, à laquelle on n'a pas réussi à accéder depuis l'environnement Arduino. Nous aurions aimé transférer les données sur la disponibilité et le temps restant d'une machine depuis le système embarqué afin de mettre à jour notre base de données, mais nous n'avons pas pu mettre en fonctionnement l'interaction entre les systèmes à cause de ce problème.

### Backend
Notre backend a pour but d'être la base de données qui nous permettra l'accès aux informations comme l'activité de la machine ou le temps restant de lavage. 
Nous avions pour objectif de déployer ces données sur le cloud, mais nous avons rencontré plusieurs problèmes comme des droits d'accès de git. On a ensuite essayé de déployer l'application en utilisant le répertoire github héberger par quelqu'un d'autre mais nous n'avons tout de même pas réussi à déployer l'application.

L'objectif était de mettre ces données en ligne afin qu'elles puissent être échangées entre l'application et le web à partir des données fournies par notre système embarqué.
On a donc essayé de contraindre le problème à des bases de données locales faites pour le web et l'application.

### Frontend web/mobile

Notre application et site web seront là pour afficher les informations recueillies grâce à notre système embarqué. Nous pourrons afficher le temps restant, quelles machines sont actives ainsi que les plages horaires avec le plus de machines disponibles.
Sur la page web, il est normal de ne voir aucune machine car la partie embarqué n'étant pas connecter au WiFi et donc au backend, la base de données reste vierge.

## Faire marcher le projet

### Lancement du backend
1. Aller sur le dossier `backend/`
2. Utilisez la commande

    ```
    ./gradlew --continous bootRun
    ```

3. Connectez-vous au serveur `http://localhost:8080`

### Lancement du serveur frontend
1. Aller sur le dossier `frontend/`
2. Utilisez les commande

    ```
    npm install
    npm run dev
    ```
3. Le site est disponible par défaut à l'URL `http://localhost:5173`
4. Vous pouvez modifier l'addresse du backend dans le ficher frontend/src/config.js


### Branchement de l'embarqué

Composants utilisés :
- Potentiomètre : permet de faire varier le courant afin d'allumer ou d'éteindre notre moteur représentant notre machine à laver. **Remarque : l'ordre des broches a été choisi en se plaçant face aux écritures sur le côté du capteur.** Borne 1 et 3 : reliées à la résistance interne, broche 3 : reliée au curseur qui peut se déplacer pour modifier la valeur de la résistance.
- Moteur : représente notre machine à laver. Lorsqu'il est en route la machine est occupée, et libre sinon
- Alimentation : 3.3 V d'alimentation
- Capteur d'intensité : 5 Bornes : IN+ entrée du courant à mesurer, OUT- sortie du courant à mesurer, GND : masse, +5V : Alimentation, OUTDATA : signal de sortie analogique proportionnel au courant mesuré.
- Carte ESP32 Feather : Nous allons utiliser les ports GND (masse), USB (pour alimenter notre capteur), A0 (pour recevoir les données du capteur).

Schéma explicatif des branchements :
* Alimentation + ---> Broche 3 du potentiomètre
* Alimentation - ---> Masse (GND)

* Potentiomètre Broche 1 ---> Masse (GND)
* Potentiomètre Broche 2 ---> IN+ du capteur de courant
* Potentiomètre Broche 3 ---> + du générateur

* Capteur IN+ ---> Broche 2 du potentiomètre
* Capteur IN- ---> Borne + du moteur
* Capteur 5V ---> + Alimentation
* Capteur GND ---> Masse (GND)

* Moteur + ---> OUT du capteur
* Moteur - ---> Masse (GND)

* Carte ESP32 A10/27 ---> OUTPUT DATA du capteur
* Carte ESP32 GND ---> Masse (GND)

Nous avons deux fichiers de code à disposition :
- `embedded.ino` fichier avec l'entiereté du code du système embarqué
- `embedded_commented.ino` qui ne comprend pas les fonctionnalités WiFi

### Application

Comme nous avons fait le choix de lancer le backend et le frontend localement, le téléphone émuler n'a pas accès à ces 2 entités. Il y a donc 2 version de l'appli, la première sans les API et avec une URL d'exemple pour simuler la page web, ceci permet de montrer comment l'appli fonctionne si elle avait accès à une base de données depuis l'API. Une seconde avec l'appel des APIs de codé pour utilisé le backend mais qui ne peut être testé à cause du problème expliqué précédemment.

