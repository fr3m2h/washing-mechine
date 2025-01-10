
# Washing-MEchine

## Introduction

Notre projet a pour objectif de déterminer de créer une inteface web et mobile afin que l'on puisse savoir quelle machine sont active   ou non à la ME avant d'aller à la laverie.

### Embarqué

Ainsi initialement nous avions prévu d'installer des capteurs de tensions sur les machines de la ME afin de détecter une différence de voltage pour déterminer si la machine était en cours de fonctionnement ou non. Seulement par manque de moyen matériel nous avons simplifier le modèle embarqué. Nous avons choisi de prendre un moteur pour simuler la machine à laver ainsi qu'un capteur de tension limités à des voltages plus bas pour capter l'activité du moteur.

Le but de notre système embarqué est de détecter l'acitivité de notre moteur que nous actionneront manuellement à l'aide d'un interrupteur. Notre microcontrolleur enverra par la suite les informations reçues à notre api.
### Backend
Notre backend a pour but d'être la base de donné qui nous permettra l'accès aux informations comme l'acitivité de la machine ou le temps restant de lavage. Nous avons pour objectif d'ensuite traîter ces données pour en faire un graphe qui donnera les heures avec le plus de machines actives et déterminer les meilleurs plages horaires pour aller faire sa machine.

### Frontend web/mobile

Notre applications et site web seront là pour afficher les informations recueillies grâce à notre système embarqué. Nous pourront afficher le temps restant, quelles machines sont actives ainsi que les plages horaires avec le plus de machines disponibles.

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
3. Le site est disponible par défaut à l'URL http://localhost:5173
4. Vous pouvez modifier l'addresse du backend dans le ficher frontend/src/config.js


### Branchement de l'embarqué

Composants utilisés :
- Potentiomètre : permet de faire varier le courant afin d'allumer ou d'éteindre notre moteur représentant notre machine à laver. **Remarque : l'ordre des broches a été choisi en se plaçant face aux écritures sur le côté du capteur.** Borne 1 et 3 : reliées à la résistance interne, broche 3 : reliée au curseur qui peut se déplacer pour modifier la valeur de la résistance.
- Moteur : représente notre machine à laver. Lorsqu'il est en route la machine est occupée, et libre sinon
- Alimentation : 3.3 V d'alimentation
- Capteur d'intensité : 5 Bornes : IN+ entrée du courant à mesurer, OUT- sortie du courant à mesurer, GND : masse, +5V : Alimentation, OUTDATA : signal de sortie analogique proportionnel au courant mesuré.
- Carte ESP32 Feather : Nous allons utilisé les ports GND (masse), USB (pour alimenter notre capteur), A0 (pour recevoir les données du capteur).

Schéma explicatif des branchements :
* Alimentation + ---> Broche 3 du potentiomètre
* Alimentation - ---> Masse (GND)

* Potentiomètre Broche 1 ---> Masse (GND)
* Potentiomètre Broche 2 ---> IN+ du capteur de courant
* Potentiomètre Broche 3 ---> + du générateur

* Capteur IN+ ---> Broche 2 du potentiomètre
* Capteur OUT- ---> Borne + du moteur
* Capteur GND ---> Masse (GND)

* Moteur + ---> OUT du capteur
* Moteur - ---> Masse (GND)

* Carte ESP32 A0 ---> OUTPUT DATA du capteur
* Carte ESP32 USB ---> 5V du capteur
* Carte ESP32 GND ---> Masse (GND)


### Branchement de l'embarqué

Comme nous avons fait le choix de lancer le backend et le frontend localement, le téléphone éumler n'a pas accès a ces 2 entités. Il y a donc 2 version de l'appli, la première sans les API et avec une URL d'example pour simuler la page web, ceci permet de montrer comment l'appli fonctionne si elle avait accès a une base de donner depuis l'API. Une seconde avec coder pour utiliser le backend mais qui ne peut ëtre tester a cause du problème expliquer précédemment.De plus cette version est au format .zip car certains dossier ne pouvait être transféré car il avait un nom trop long...

