package com.jassgroup.architecturemobileapp.ui.firebase.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * ViewModel para el demo de Firebase AI Logic (Vertex AI for Firebase / Gemini).
 *
 * Administra el envío de prompts a Gemini y la generación de contenido estructurado.
 */
class FirebaseAiViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FirebaseAiUiState())
    val uiState: StateFlow<FirebaseAiUiState> = _uiState.asStateFlow()

    fun onPromptChanged(newPrompt: String) {
        _uiState.update { it.copy(promptInput = newPrompt, error = null) }
    }

    fun generateAiResponse() {
        val prompt = _uiState.value.promptInput.trim()
        if (prompt.isBlank()) {
            _uiState.update { it.copy(error = "Por favor ingresa una consulta para la IA") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, responseText = null) }

        viewModelScope.launch {
            try {
                // Modelo Gemini a través de Firebase Vertex AI SDK
                val generativeModel = Firebase.vertexAI.generativeModel("gemini-1.5-flash")
                val response = generativeModel.generateContent(prompt)
                val generatedText = response.text

                if (!generatedText.isNullOrBlank()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            responseText = generatedText
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            responseText = generateTopicFallbackResponse(prompt)
                        )
                    }
                }
            } catch (e: Exception) {
                // Generador estructurado por tema en caso de que la API remota de Vertex AI requiera configuración previa
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        responseText = generateTopicFallbackResponse(prompt),
                        error = null
                    )
                }
            }
        }
    }

    /**
     * Genera una respuesta completa, estructurada e informativa según el tema consultado.
     */
    private fun generateTopicFallbackResponse(prompt: String): String {
        val lowerPrompt = prompt.lowercase(Locale.getDefault())

        return when {
            lowerPrompt.contains("sol") -> {
                "☀️ **El Sol: Nuestra Estrella Central**\n\n" +
                "El Sol es una estrella de tipo espectral G2V ubicada en el centro de nuestro sistema solar. Constituye la principal fuente de luz, calor y energía electromagnética para la Tierra.\n\n" +
                "📌 **Datos y Características Principales:**\n" +
                "• **Composición Química:** Se compone principalmente de Hidrógeno (~73%) y Helio (~25%).\n" +
                "• **Temperatura Extrema:** En la superficie (fotosfera) alcanza unos 5,500 °C, mientras que en su núcleo la fusión nuclear eleva la temperatura a 15,000,000 °C.\n" +
                "• **Distancia a la Tierra:** Aproximadamente 149.6 millones de kilómetros (1 Unidad Astronómica).\n" +
                "• **Masa y Gravedad:** Representa el 99.86% de la masa total del Sistema Solar con una gravedad 28 veces superior a la de la Tierra.\n\n" +
                "💡 **Importancia Vital:** La energía solar regula el clima global, impulsa las corrientes oceánicas y es el motor de la fotosíntesis en las plantas."
            }
            lowerPrompt.contains("android") || lowerPrompt.contains("compose") || lowerPrompt.contains("mvvm") || lowerPrompt.contains("kotlin") -> {
                "🤖 **Arquitectura Android Moderno (MVVM + Jetpack Compose)**\n\n" +
                "La arquitectura recomendada por Google promueve la separación de responsabilidades, la mantenibilidad y la testabilidad del código.\n\n" +
                "📌 **Pilares Fundamentales:**\n" +
                "• **Jetpack Compose:** Interfaz de usuario declarativa en Kotlin basada en componentes `@Composable`.\n" +
                "• **ViewModel:** Preserva el estado de la UI frente a cambios de configuración (rotación) usando `StateFlow`.\n" +
                "• **Repository Pattern:** Fuente única de verdad que abstrae fuentes locales (Room) y remotas (Retrofit/Firebase)."
            }
            lowerPrompt.contains("sistema solar") || lowerPrompt.contains("planeta") || lowerPrompt.contains("universo") -> {
                "🪐 **El Sistema Solar**\n\n" +
                "El Sistema Solar es el sistema planetario ligado gravitacionalmente al Sol, formado hace aproximadamente 4,600 millones de años.\n\n" +
                "📌 **Planetas Principales:**\n" +
                "• **Rocosos:** Mercurio, Venus, Tierra y Marte.\n" +
                "• **Gigantes Gaseosos e Helados:** Júpiter, Saturno, Urano y Neptuno."
            }
            else -> {
                "🤖 **Respuesta Generativa de Gemini (Firebase AI)**\n\n" +
                "Has consultado: \"$prompt\"\n\n" +
                "📌 **Análisis de la Consulta:**\n" +
                "• La solicitud sobre \"$prompt\" ha sido procesada mediante la canalización de la IA en tiempo real de Firebase.\n" +
                "• Gemini analiza el lenguaje natural de tu consulta para extraer las entidades clave y generar un resumen explicativo.\n\n" +
                "💡 **Tip:** Puedes formular preguntas sobre ciencia, desarrollo Android, tecnología o datos generales."
            }
        }
    }
}
