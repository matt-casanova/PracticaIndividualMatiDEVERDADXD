package com.example.practicaindividualmati.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicaindividualmati.Greeting
import com.example.practicaindividualmati.R
import com.example.practicaindividualmati.ViewModel
import com.example.practicaindividualmati.models.KanyeQuoteResumen

// Paleta base
val backgroundColor = Color(0xFF121212)
val surfaceColor = Color(0xFF1E1E1E)
val textColor = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChuckNorrisApp(
    viewModel: ViewModel = viewModel()
) {
    // Estado para saber qué tab está seleccionado (0 = Gemini, 1 = Stoic, 2 = Kanye)
    var selectedTab by remember { mutableStateOf(0) }
    var showingKanyeFavorites by remember { mutableStateOf(false) }

    // Colores dinámicos dependiendo del tab
    val primaryColor = when (selectedTab) {
        0 -> Color(0xFFFF5722) // Naranja intenso para Chuck
        1 -> Color(0xFF6200EA) // Púrpura profundo para Stoic
        else -> Color(0xFFE91E63) // Rosa para la 3era pantalla (Kanye)
    }

    val appBarTitle = when (selectedTab) {
        0 -> "Gemini Api"
        1 -> "Stoic Quotes"
        else -> if (showingKanyeFavorites) "Favoritos de Kanye" else "Kanye API"
    }

    val favoritosIds by viewModel.favoritosIds.collectAsState()
    val favoritosList by viewModel.favoritosList.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = appBarTitle,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = primaryColor,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    // Si estamos en la pestaña de Kanye (tab 2), mostramos el botón de favoritos
                    if (selectedTab == 2) {
                        IconButton(onClick = { showingKanyeFavorites = !showingKanyeFavorites }) {
                            Icon(
                                imageVector = if (showingKanyeFavorites) Icons.Default.Info else Icons.Default.Favorite,
                                contentDescription = "Ver favoritos de Kanye",
                                tint = primaryColor
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                ),
                modifier = Modifier.shadow(8.dp)
            )
        },
        bottomBar = {
            if (!showingKanyeFavorites) {
                NavigationBar(
                    containerColor = surfaceColor,
                    contentColor = textColor
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0; showingKanyeFavorites = false },
                        icon = { Icon(Icons.Default.Star, contentDescription = "Gemini") },
                        label = { Text("Gemini") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = primaryColor,
                            indicatorColor = primaryColor,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1; showingKanyeFavorites = false },
                        icon = { Icon(Icons.Default.Face, contentDescription = "Stoic") },
                        label = { Text("Stoic") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = primaryColor,
                            indicatorColor = primaryColor,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.Info, contentDescription = "Kanye") },
                        label = { Text("Kanye") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = primaryColor,
                            indicatorColor = primaryColor,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        if (selectedTab == 2 && showingKanyeFavorites) {
            KanyeFavoritesContent(
                favoritosList = favoritosList,
                onQuitar = { quoteText -> viewModel.alternarFavoritoKanye(quoteText) },
                primaryColor = primaryColor,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            DynamicScreenContent(
                selectedTab = selectedTab,
                viewModel = viewModel,
                primaryColor = primaryColor,
                favoritosIds = favoritosIds,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun DynamicScreenContent(
    selectedTab: Int,
    viewModel: ViewModel,
    primaryColor: Color,
    favoritosIds: Set<Int>,
    modifier: Modifier = Modifier
) {
    // Observamos los estados de nuestro ViewModel
    val value by viewModel.valueState.collectAsState()
    val kanyeQuote by viewModel.kanyeState.collectAsState()
    val geminiAnswer by viewModel.geminiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorState.collectAsState()

    // Configuraciones específicas por tab
    val greetingName = when (selectedTab) {
        0 -> "GEMINI"
        1 -> "MARCO AURELIO"
        else -> "KANYE"
    }

    val imageRes = when (selectedTab) {
        0 -> R.drawable.gemini
        1 -> R.drawable.stoicportait
        else -> R.drawable.kanye
    }

    // Estado local para el campo de texto de Gemini
    var promptText by remember { mutableStateOf("") }

    // Al clickear el botón, ejecutamos el request específico
    val buttonAction: () -> Unit = {
        when (selectedTab) {
            0 -> viewModel.fetchGeminiAnswer(promptText) 
            1 -> viewModel.fetchStoicQuote()
            else -> viewModel.fetchKanyeQuote()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Greeting(
            name = greetingName,
            textColor = textColor
        )

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = greetingName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(3.dp, primaryColor, RoundedCornerShape(16.dp))
                .shadow(12.dp, RoundedCornerShape(16.dp))
        )

        // Si estamos en la pestaña 0 (Gemini), mostramos un TextField
        if (selectedTab == 0) {
            TextField(
                value = promptText,
                onValueChange = { promptText = it },
                placeholder = { Text("Escribe tu pregunta para Gemini...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        // Si estamos en Kanye y hay una cita, mostramos el botón de favorito (Corazón)
        if (selectedTab == 2 && kanyeQuote != null) {
            val esFavorito = kanyeQuote!!.quote.hashCode() in favoritosIds
            IconButton(
                onClick = { viewModel.alternarFavoritoKanye(kanyeQuote!!.quote) },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = if (esFavorito) androidx.compose.material.icons.Icons.Default.Favorite else androidx.compose.material.icons.Icons.Default.FavoriteBorder,
                    contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (esFavorito) Color(0xFFE91E63) else Color.Gray,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Qué texto mostramos en el cuadro central
        val textToShow = when {
            isLoading -> "Buscando información..."
            error != null -> error!!
            geminiAnswer != null && selectedTab == 0 -> geminiAnswer!!.answer
            value != null && selectedTab == 1 -> "\"${value!!.qoute}\" \n\n- ${value!!.author}"
            kanyeQuote != null && selectedTab == 2 -> kanyeQuote!!.quote
            else -> if (selectedTab == 0) "Escribe algo arriba y presiona enviar" else "Presiona Random para obtener data"
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
                .background(surfaceColor, RoundedCornerShape(12.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = textToShow,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = if (selectedTab == 0 && geminiAnswer != null) TextAlign.Start else TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = buttonAction,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading && (selectedTab != 0 || promptText.isNotBlank()),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp))
        ) {
            Text(
                text = if (selectedTab == 0) "ENVIAR" else "RANDOM",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun KanyeFavoritesContent(
    favoritosList: List<KanyeQuoteResumen>,
    onQuitar: (String) -> Unit,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mis Citas Favoritas de Kanye",
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favoritosList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aún no tienes citas de Kanye guardadas como favoritas.\nToca el ícono de corazón al generar una cita.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoritosList.size) { index ->
                    val favorito = favoritosList[index]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = surfaceColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "\"${favorito.quote}\"",
                                color = textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                IconButton(onClick = { onQuitar(favorito.quote) }) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Quitar de favoritos",
                                        tint = Color(0xFFE91E63)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}