package com.example.lalaveriedelame

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lalaveriedelame.model.MachineService
import com.example.lalaveriedelame.model.MachineViewModel
import com.example.lalaveriedelame.ui.theme.LalaverieDeLaMeTheme

class MachineActivity : ComponentActivity() {

    // Create an instance of MachineViewModel using viewModels delegate
    private val machineViewModel: MachineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the machineId and errorMessage passed via Intent
        val machineId = intent.getLongExtra("machine_id", -1L)
        val errorMessage = intent.getStringExtra("error_message")

        // Set the machine in the ViewModel if the ID is valid
        if (machineId != -1L) {
            val machine = MachineService.findById(machineId)
            machine?.let {
                machineViewModel.machine = it
            }
        }

        setContent {
            LalaverieDeLaMeTheme {
                Scaffold(
                    topBar = {
                        MachineTopAppBar(
                            title = "Détails de la machine",
                            onBackClick = { finish() },
                            onWebIconClick = { // Redirection vers le site web
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
                                startActivity(intent)
                            },
                            onRushHourClick = { // Redirection vers l'activité RushHourActivity
                                val intent = Intent(this, RushHourActivity::class.java)
                                startActivity(intent)
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Contenu de MachineActivity
                        if (!errorMessage.isNullOrEmpty()) {
                            ErrorScreen(message = errorMessage)
                        } else {
                            MachineDetailsScreen(machineViewModel = machineViewModel)
                        }
                    }
                }
            }
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MachineTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    onWebIconClick: () -> Unit,
    onRushHourClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface // Couleur du texte
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Retour",
                    tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône
                )
            }
        },
        actions = {
            // Icône pour rediriger vers le site web
            IconButton(onClick = onWebIconClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_action_name),
                    contentDescription = stringResource(R.string.web_icon_description),
                    tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône
                )
            }
            // Icône pour rediriger vers l'activité RushHourActivity
            IconButton(onClick = onRushHourClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_heuredepointe),
                    contentDescription = stringResource(R.string.heuredepointe),
                    tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface, // Fond de la TopAppBar
            titleContentColor = MaterialTheme.colorScheme.onSurface // Couleur du titre
        )
    )
}
@Composable
fun MachineDetailsScreen(machineViewModel: MachineViewModel) {
    // Obtenir les données de la machine à partir du ViewModel
    val machine = machineViewModel.machine

    // Vérification des données
    if (machine == null) {
        Text(
            text = "Données de la machine non disponibles",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground // Couleur du texte
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Machine : ${machine.id}",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground // Couleur du texte
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Boîte d'état de la machine
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentHeight()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "État de la Machine",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground, // Couleur du texte
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = if (machine.isUsed == true) {
                        "En cours d'utilisation"
                    } else {
                        "Libre"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (machine.isUsed == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Boîte pour afficher le temps restant
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentHeight()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Temps Restant",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground, // Couleur du texte
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = if (machine.isUsed == true && machine.tempsRestant != null) {
                        "${String.format("%.2f", machine.tempsRestant)} min"
                    } else {
                        "Aucun temps restant"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground // Couleur du texte
                )
            }
        }
    }
}


@Composable
fun ErrorScreen(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Erreur : $message",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}
