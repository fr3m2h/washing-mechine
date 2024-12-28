<body>
<h1 align="center">Washing-MEchine</h1>

<p>
  Notre projet a pour objectif de déterminer de créer une inteface web et mobile afin que l'on puisse savoir quelle machine sont active   ou non à la ME avant d'aller à la laverie.
  <h3 align="left">Embarqué</h3>

  Ainsi initialement nous avions prévu d'installer des capteurs de tensions sur les machines de la ME afin de détecter une différence de voltage pour déterminer si la machine était en cours de fonctionnement ou non. Seulement par manque de moyen matériel nous avons simplifier le modèle embarqué. Nous avons choisi de prendre un moteur pour simuler la machine à laver ainsi qu'un capteur de tension limités à des voltages plus bas pour capter l'activité du moteur.

  Le but de notre système embarqué est de détecter l'acitivité de notre moteur que nous actionneront manuellement à l'aide d'un interrupteur. Notre microcontrolleur enverra par la suite les informations reçues à notre api.

  <h3 align="left">Backend</h3>
 Notre backend a pour but d'être la base de donné qui nous permettra l'accès aux informations comme l'acitivité de la machine ou le temps restant de lavage. Nous avons pour objectif d'ensuite traîter ces données pour en faire un graphe qui donnera les heures avec le plus de machines actives et déterminer les meilleurs plages horaires pour aller faire sa machine.

 <h3 align="left">Frontend web/mobile </h3>
 Notre applications et site web seront là pour afficher les informations recueillies grâce à notre système embarqué. Nous pourront afficher le temps restant, quelles machines sont actives ainsi que les plages horaires avec le plus de machines disponibles.
</p>
</body>

