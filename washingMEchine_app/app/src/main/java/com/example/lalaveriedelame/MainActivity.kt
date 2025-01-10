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
    import androidx.activity.viewModels
    import androidx.compose.runtime.Composable
    import com.example.lalaveriedelame.model.MachineViewModel

    class MainActivity : ComponentActivity() {
        private val machineViewModel: MachineViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            
            machineViewModel.loadMachines()
            val url = getString(R.string.url)
            setContent {
                LalaverieDeLaMeTheme {
                    MainScreen(
                        machines = machineViewModel.machines.value,
                        isLoading = machineViewModel.isLoading.value,
                        errorMessage = machineViewModel.errorMessage.value,
                        onMachineSelected = { machineId ->
                            val intent = Intent(this, MachineActivity::class.java)
                            intent.putExtra("machine_id", machineId)
                            startActivity(intent)
                        },
                        onWebIconClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        },
                        onRushHourClick = {
                            val intent = Intent(this, RushHourActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun MainScreen(
        machines: List<MachineDto>,
        isLoading: Boolean,
        errorMessage: String?,
        onMachineSelected: (Long) -> Unit,
        onWebIconClick: () -> Unit,
        onRushHourClick: () -> Unit
    ) {
        var showMachineList by remember { mutableStateOf(false) }
        var machineQuery by remember { mutableStateOf("") }
        val context = LocalContext.current

        Scaffold(
            topBar = {
                LalaverieTopAppBar(
                    title = stringResource(R.string.app_name),
                    onWebIconClick = onWebIconClick,
                    onShowMachineListToggle = { showMachineList = !showMachineList },
                    onRushHourClick = onRushHourClick
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Affichage en cas de chargement
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                // Affichage des erreurs si elles existent
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

               
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
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val queriedMachine = machines.find { it.id.toString() == machineQuery }
                            val intent = Intent(context, MachineActivity::class.java)
                            if (queriedMachine != null) {
                                intent.putExtra("machine_id", queriedMachine.id)
                            } else {
                                intent.putExtra("machine_id", -1L)
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
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
                
                IconButton(onClick = onWebIconClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_name),
                        contentDescription = stringResource(R.string.web_icon_description),
                        tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône
                    )
                }
                
                IconButton(onClick = onShowMachineListToggle) {
                    Icon(
                        painter = painterResource(R.drawable.ic_washing_list),
                        contentDescription = stringResource(R.string.machine_list_icon_description),
                        tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône
                    )
                }
                
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
