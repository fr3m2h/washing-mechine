package com.example.lalaveriedelame

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lalaveriedelame.model.MachineDto
import com.example.lalaveriedelame.model.MachineService
import com.example.lalaveriedelame.ui.theme.LalaverieDeLaMeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Générer une liste de machines pour cet exemple
        val machines = MachineService.generateRandomMachines(10)

        setContent {
            LalaverieDeLaMeTheme {
                MainScreen(
                    machines = machines,
                    onMachineSelected = { machineId ->
                        val intent = Intent(this, MachineActivity::class.java)
                        intent.putExtra("machine_id", machineId)
                        startActivity(intent)
                    },
                    onWebIconClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
                        startActivity(intent)
                    },
                    onRushHourClick = { // Redirection vers RushHourActivity
                        val intent = Intent(this, RushHourActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    machines: List<MachineDto>,
    onMachineSelected: (Long) -> Unit,
    onWebIconClick: () -> Unit,
    onRushHourClick: () -> Unit // Nouveau paramètre pour gérer le clic vers RushHourActivity
) {
    var showMachineList by remember { mutableStateOf(false) }
    var machineQuery by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current // Contexte pour Toast

    Scaffold(
        topBar = {
            LalaverieTopAppBar(
                title = stringResource(R.string.app_name),
                onWebIconClick = onWebIconClick,
                onShowMachineListToggle = { showMachineList = !showMachineList },
                onRushHourClick = onRushHourClick
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showMachineList) {
                MachineListView(
                    machines = machines,
                    onMachineSelected = {
                        showMachineList = false
                        onMachineSelected(it)
                    }
                )
            } else {
                AppLogo(Modifier.padding(top = 16.dp, bottom = 16.dp))
                WelcomeMessage(Modifier.padding(horizontal = 16.dp))
                OutlinedTextField(
                    value = machineQuery,
                    onValueChange = { machineQuery = it },
                    label = { Text(text = stringResource(R.string.search_placeholder)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colorScheme.primary, // Couleur du label lorsqu'il est sélectionné
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground, // Couleur du label lorsqu'il n'est pas sélectionné
                        focusedBorderColor = MaterialTheme.colorScheme.primary, // Couleur de la bordure lorsqu'elle est sélectionnée
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary, // Couleur de la bordure lorsqu'elle n'est pas sélectionnée
                        cursorColor = MaterialTheme.colorScheme.primary, // Couleur du curseur
                        containerColor = MaterialTheme.colorScheme.background // Couleur de fond du champ
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val queriedMachine = machines.find { it.id.toString() == machineQuery.text }
                        val intent = Intent(context, MachineActivity::class.java)
                        if (queriedMachine != null) {
                            intent.putExtra("machine_id", queriedMachine.id)
                            intent.putExtra("error_message", "")
                        } else {
                            intent.putExtra("machine_id", -1L) // Valeur invalide pour ID
                            intent.putExtra("error_message", "Machine introuvable : ${machineQuery.text}")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.search_button_label))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}




@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LalaverieTopAppBar(
    title: String,
    onWebIconClick: () -> Unit,
    onShowMachineListToggle: () -> Unit,
    onRushHourClick: () -> Unit // Paramètre ajouté pour gérer le clic sur RushHourActivity
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface, // Fond
        titleContentColor = MaterialTheme.colorScheme.onSurface // Couleur du texte
    )

    MediumTopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface // Couleur du titre
            )
        },
        colors = colors,
        actions = {
            // Icône pour redirection web
            IconButton(onClick = onWebIconClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_action_name),
                    contentDescription = stringResource(R.string.web_icon_description),
                    tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône
                )
            }
            // Icône pour afficher la liste des machines
            IconButton(onClick = onShowMachineListToggle) {
                Icon(
                    painter = painterResource(R.drawable.ic_washing_list),
                    contentDescription = stringResource(R.string.machine_list_icon_description),
                    tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône
                )
            }
            // Icône pour rediriger vers RushHourActivity
            IconButton(onClick = onRushHourClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_heuredepointe),
                    contentDescription = stringResource(R.string.heuredepointe),
                    tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône
                )
            }
        }
    )
}


@Composable
fun MachineListView(
    machines: List<MachineDto>,
    onMachineSelected: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(machines) { machine ->
            MachineCard(machine = machine, onClick = { onMachineSelected(machine.id) })
        }
    }
}

@Composable
fun MachineCard(
    machine: MachineDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Machine ${machine.id}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = if (machine.isUsed == true) {
                    "Statut : Occupée"
                } else {
                    "Statut : Disponible"
                },
                style = MaterialTheme.typography.bodyMedium
            )
            if (machine.isUsed == true && machine.tempsRestant != null) {
                Text(
                    text = "Temps restant : ${String.format("%.2f", machine.tempsRestant)} min",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}




@Composable
fun AppLogo(modifier: Modifier) {
    Image(
        painter = painterResource(R.drawable.logo_machine_new),
        contentDescription = stringResource(R.string.app_logo_description),
        modifier = modifier
    )
}

@Composable
fun WelcomeMessage(modifier: Modifier) {
    Text(
        text = stringResource(R.string.act_main_welcome),
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 21.sp),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
