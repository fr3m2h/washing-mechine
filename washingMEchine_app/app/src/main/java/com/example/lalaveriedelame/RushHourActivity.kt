
package com.example.lalaveriedelame

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lalaveriedelame.model.JourDto
import com.example.lalaveriedelame.model.MachineDto
import com.example.lalaveriedelame.model.MachineService
import com.example.lalaveriedelame.service.RushHourService
import com.example.lalaveriedelame.ui.theme.LalaverieDeLaMeTheme

class RushHourActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Générer les données des jours de la semaine
        val jours = RushHourService.genererDonneesSemaine()
        val machines = MachineService.generateRandomMachines(10)
        setContent {
            LalaverieDeLaMeTheme {
                RushHourScreen(
                    jours = jours,
                    machines=machines   ,
                    onBackClick = { finish() },
                    onWebIconClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun RushHourScreen(
    jours: List<JourDto>,
    machines: List<MachineDto>, // Liste des machines
    onBackClick: () -> Unit,
    onWebIconClick: () -> Unit
) {
    var selectedDay by remember { mutableStateOf<JourDto?>(null) } // État pour le jour sélectionné
    var showMachineList by remember { mutableStateOf(false) } // État pour afficher ou masquer la liste des machines

    val context = LocalContext.current // Contexte pour la navigation

    Scaffold(
        topBar = {
            RushHourTopAppBar(
                onBackClick = onBackClick,
                onWebIconClick = onWebIconClick,
                onShowMachineListToggle = {
                    showMachineList = !showMachineList // Affiche ou masque la liste des machines
                    selectedDay = null // Désélectionne le jour si la liste des machines est affichée
                }
            )
        },
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                when {
                    showMachineList -> {
                        // Affiche la liste des machines
                        MachineListView(
                            machines = machines,
                            onMachineSelected = { machineId ->
                                // Redirige vers MachineActivity avec l'ID de la machine
                                val intent = Intent(context, MachineActivity::class.java).apply {
                                    putExtra("machine_id", machineId)
                                }
                                context.startActivity(intent)
                                showMachineList = false
                            }
                        )
                    }
                    selectedDay != null -> {
                        // Affiche les détails pour le jour sélectionné
                        DayDetailsView(
                            jour = selectedDay!!,
                            onBackToListClick = { selectedDay = null }
                        )
                    }
                    else -> {
                        // Affiche la liste des jours
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(jours) { jour ->
                                RushHourDayItem(
                                    jour = jour,
                                    onClick = { selectedDay = jour }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}



@Composable
fun RushHourDayItem(jour: JourDto, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = "${jour.jour} - ${jour.totalUtilisations} utilisations",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun DayDetailsView(jour: JourDto, onBackToListClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Tout le contenu devient défilable
            .padding(16.dp)
    ) {
        // Titre
        Text(
            text = "Détails pour ${jour.jour}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Total des utilisations
        Text(
            text = "Total des utilisations : ${jour.totalUtilisations}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Graphique défilable
        BarChart(
            data = jour.utilisationsParHeure.mapIndexed { index, value ->
                "${index}h" to value
            }.toMap(),
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp) // Hauteur suffisante pour s'assurer que tout est visible
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton "Retour à la liste"
        Button(
            onClick = onBackToListClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Retour à la liste")
        }
    }
}





@Composable
fun BarChart(data: Map<String, Int>, modifier: Modifier = Modifier) {
    val maxData = 8 // Limite de l'axe des abscisses à 8
    val barHeight = 23.dp
    val barSpacing = 3.dp
    val days = data.keys.toList()

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val xAxisPadding = 60.dp.toPx()
        val contentWidth = canvasWidth - xAxisPadding
        val stepX = contentWidth / maxData // Échelle pour l'axe des X, basé sur le maximum (8)

        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK // Couleur du texte
            textSize = 24f
            textAlign = android.graphics.Paint.Align.LEFT
        }

        days.forEachIndexed { index, day ->
            val value = data[day]?.coerceAtMost(maxData) ?: 0 // Limite les barres au maximum de 8
            val barLength = value * stepX
            val barY = xAxisPadding + index * (barHeight.toPx() + barSpacing.toPx())

            // Dessin des barres
            drawRect(
                color = Color(0xFF32CD32), // Vert clair
                topLeft = androidx.compose.ui.geometry.Offset(
                    x = 0f,
                    y = barY
                ),
                size = Size(barLength, barHeight.toPx())
            )

            // Texte pour les noms des jours
            drawContext.canvas.nativeCanvas.drawText(
                day,
                xAxisPadding / 2,
                barY + barHeight.toPx() / 2 + 10.dp.toPx(),
                textPaint
            )
        }

        // Dessin des labels pour l'axe des X
        for (i in 0..maxData) {
            val xPosition = i * stepX
            drawContext.canvas.nativeCanvas.drawText(
                i.toString(),
                xPosition,
                xAxisPadding / 2,
                textPaint
            )
        }

        // Dessin des axes
        drawLine(
            color = Color.Black,
            start = androidx.compose.ui.geometry.Offset(0f, xAxisPadding),
            end = androidx.compose.ui.geometry.Offset(canvasWidth, xAxisPadding),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.Black,
            start = androidx.compose.ui.geometry.Offset(0f, xAxisPadding),
            end = androidx.compose.ui.geometry.Offset(0f, canvasHeight),
            strokeWidth = 2.dp.toPx()
        )
    }
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RushHourTopAppBar(
    onBackClick: () -> Unit,
    onWebIconClick: () -> Unit,
    onShowMachineListToggle: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Horaires d'affluence",
                color = MaterialTheme.colorScheme.onSurface // Couleur du titre (blanc ou clair)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Retour",
                    tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône (blanc ou clair)
                )
            }
        },
        actions = {
            // Icône pour redirection web
            IconButton(onClick = onWebIconClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_action_name),
                    contentDescription = "Redirection web",
                    tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône (blanc ou clair)
                )
            }
            // Icône pour afficher la liste des jours
            IconButton(onClick = onShowMachineListToggle) {
                Icon(
                    painter = painterResource(R.drawable.ic_washing_list),
                    contentDescription = "Liste des jours",
                    tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône (blanc ou clair)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface, // Fond de la barre (gris défini)
            titleContentColor = MaterialTheme.colorScheme.onSurface // Couleur du texte (blanc ou clair)
        )
    )
}
