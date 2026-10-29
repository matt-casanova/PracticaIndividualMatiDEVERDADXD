package com.example.practicaindividualmati

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaindividualmati.data.ai.AiAnswer
import com.example.practicaindividualmati.data.ai.AiRepository
import com.example.practicaindividualmati.data.kanye.KanyeQuoteDto
import com.example.practicaindividualmati.data.kanye.KanyeQuoteRepository
import com.example.practicaindividualmati.data.stoic.StoicRepository
import com.example.practicaindividualmati.models.StoicQuote
import com.example.practicaindividualmati.models.Joke
import com.example.practicaindividualmati.models.KanyeQuoteResumen
import com.example.practicaindividualmati.data.local.FavoritosRepository
import com.example.practicaindividualmati.data.local.KanyeWestDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel: Es el cerebro de la pantalla. 
 * Conecta el Repositorio (datos) con la Vista (Compose).
 * Su ventaja es que los datos no se pierden al girar la pantalla del celular.
 */
class ViewModel(application: Application) : AndroidViewModel(application) {

    // Instanciamos los repositorios de las API's y de la base de datos (Room)
    private val stoicRepository = StoicRepository()
    private val kanyeRepository = KanyeQuoteRepository()
    private val geminiRepository = AiRepository()
    private val favoritosRepository = FavoritosRepository(KanyeWestDatabase.obtener(application).favoritoDao())

    // Observamos los IDs de los favoritos desde Room
    val favoritosIds: StateFlow<Set<Int>> = favoritosRepository.observarIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    // Observamos la lista completa de favoritos desde Room
    val favoritosList: StateFlow<List<KanyeQuoteResumen>> = favoritosRepository.observarFavoritos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Función para alternar el estado de favorito de una cita de Kanye
    fun alternarFavoritoKanye(quoteText: String) {
        val id = quoteText.hashCode()
        val esFavorito = id in favoritosIds.value
        viewModelScope.launch {
            if (esFavorito) {
                favoritosRepository.quitar(id)
            } else {
                favoritosRepository.agregar(KanyeQuoteResumen(quote = quoteText))
            }
        }
    }

    // _valueState es para la pantalla Estoica
    private val _valueState = MutableStateFlow<StoicQuote?>(null)
    val valueState: StateFlow<StoicQuote?> = _valueState.asStateFlow()

    // Creamos un estado exclusivo para la pantalla de Chuck Norris
    private val _jokeState = MutableStateFlow<Joke?>(null)
    val jokeState: StateFlow<Joke?> = _jokeState.asStateFlow()

    // Creamos un estado exclusivo para la pantalla de Kanye West
    private val _kanyeState = MutableStateFlow<KanyeQuoteDto?>(null)
    val kanyeState: StateFlow<KanyeQuoteDto?> = _kanyeState.asStateFlow()

    // Creamos un estado exclusivo para la pantalla de Gemini
    private val _geminiState = MutableStateFlow<AiAnswer?>(null)
    val geminiState: StateFlow<AiAnswer?> = _geminiState.asStateFlow()





    // Guardamos si hay un error
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    // Guardamos si está cargando
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Al iniciar
    }

    fun fetchStoicQuote(){
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            try {
                val quote = stoicRepository.obtenerCitaAleatoria()
                _valueState.value = quote
            } catch (e: Exception) {
                _errorState.value = "Hubo un error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchKanyeQuote(){
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            try {
                val quote = kanyeRepository.obtenerCItaKanyeAleatoria()
                _kanyeState.value = quote
            } catch (e: Exception) {
                _errorState.value = "Hubo un error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Esta función llama al Repositorio para pedir un chiste nuevo de Chuck Norris.
     */

    fun fetchGeminiAnswer(prompt: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null
            _geminiState.value = null
            try {
                val geminiAnswer = geminiRepository.obtenerRespuestaGemini(prompt)
                _geminiState.value = geminiAnswer
            } catch (e: retrofit2.HttpException) {
                when (e.code()) {
                    429 -> _errorState.value = "Excediste el límite de peticiones de la API gratuita de Gemini (429). Espera 15-30 segundos e intenta de nuevo."
                    503 -> _errorState.value = "El servicio de Gemini está ocupado temporalmente (503). Por favor reintenta en unos segundos."
                    else -> _errorState.value = "Hubo un error: ${e.localizedMessage}"
                }
            } catch (e: Exception) {
                _errorState.value = "Hubo un error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}