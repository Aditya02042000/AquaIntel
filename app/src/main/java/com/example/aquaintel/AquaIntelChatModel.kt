package com.example.aquaintel

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AquaIntelChatModel {
    // API Key from Google AI Studio
    private val apiKey = "AIzaSyDxUyj98UkDe_OdVEof0HJTC8fZS_xDtf0"

    private val safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE)
    )

    // Primary model: gemini-1.5-flash
    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey,
        systemInstruction = content { 
            text("You are a highly helpful and versatile AI assistant for the AquaIntel app. " +
                 "Your goal is to answer EVERY question asked by the user clearly and accurately, " +
                 "whether it is about water, tech, or general knowledge.")
        },
        safetySettings = safetySettings
    )

    private var chat = model.startChat()

    suspend fun getChatResponse(userPrompt: String): String? = withContext(Dispatchers.IO) {
        try {
            val response = chat.sendMessage(userPrompt)
            response.text
        } catch (e: Exception) {
            val errorMsg = e.localizedMessage ?: "Unknown error"
            
            // Handle specific error cases to provide better feedback
            when {
                errorMsg.contains("404") || errorMsg.contains("not found") -> {
                    "Error: The model 'gemini-1.5-flash' was not found. Please verify your API key access in Google AI Studio and ensure you are in a supported region."
                }
                errorMsg.contains("Serialization") || errorMsg.contains("Field 'details'") -> {
                    "Error: Received an unexpected response from the AI server. This usually happens during service outages or region restrictions."
                }
                else -> "Error: $errorMsg"
            }
        }
    }
}
