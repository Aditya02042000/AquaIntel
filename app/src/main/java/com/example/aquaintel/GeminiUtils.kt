package com.example.aquaintel

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class AnalysisResult(
    val updatedUser: User,
    val suggestions: String
)

object GeminiUtils {
    
    private fun getModel(): GenerativeModel {
        val apiKey = BuildConfig.GEMINI_API_KEY
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )
    }

    suspend fun analyzeReport(context: Context, reportText: String): AnalysisResult? = withContext(Dispatchers.IO) {
        val model = getModel()
        val systemPrompt = """
            You are AquaIntel AI, a professional medical report analyzer. 
            Analyze the following medical report text and extract health metrics.
            Also, provide personalized health suggestions (nutrition, diet, exercise) in Hinglish (a mix of Hindi and English) based on the findings and age.
            
            Return the data in a valid JSON format that matches the following structure exactly:
            {
                "heartRate": "value",
                "bloodPressure": "systolic/diastolic",
                "oxygenLevel": "percentage",
                "sleepHours": "value",
                "healthScore": 1-100,
                "age": "value",
                "weight": "value",
                "height": "value",
                "suggestions": "Detailed advice in Hinglish about diet, exercise, and next steps."
            }
            If a metric value is not found, use "0" or "0/0" for bloodPressure.
            
            Report Text:
            $reportText
        """.trimIndent()

        try {
            val response = model.generateContent(systemPrompt)
            val jsonText = response.text?.trim()?.removeSurrounding("```json", "```")?.trim()
            if (jsonText != null) {
                val heartRate = extractValue(jsonText, "heartRate")
                val bp = extractValue(jsonText, "bloodPressure")
                val oxygen = extractValue(jsonText, "oxygenLevel")
                val sleep = extractValue(jsonText, "sleepHours")
                val score = extractValue(jsonText, "healthScore").toIntOrNull() ?: 50
                val age = extractValue(jsonText, "age")
                val weight = extractValue(jsonText, "weight")
                val height = extractValue(jsonText, "height")
                val suggestions = extractValue(jsonText, "suggestions")

                val user = User(
                    heartRate = heartRate,
                    bloodPressure = bp,
                    oxygenLevel = oxygen,
                    sleepHours = sleep,
                    healthScore = score,
                    age = age,
                    weight = weight,
                    height = height
                )
                return@withContext AnalysisResult(user, suggestions)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }

    suspend fun getChatResponse(context: Context, history: List<ChatMessage>, userInput: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val model = GenerativeModel(modelName = "gemini-1.5-flash", apiKey = apiKey)
        
        val systemContext = "You are AquaIntel AI, a helpful health assistant. You understand Hindi, English, and Hinglish. Always provide helpful health, nutrition, and wellness advice. If the user talks in Hindi or Hinglish, reply in Hinglish to be friendly and clear."

        try {
            val chat = model.startChat(
                history = history.map { 
                    content(role = if (it.isUser) "user" else "model") { text(it.text) }
                }
            )
            val response = chat.sendMessage(content { 
                text(systemContext)
                text(userInput)
            })
            response.text ?: "I'm sorry, I couldn't process that."
        } catch (e: Exception) {
            val errorMsg = e.localizedMessage ?: ""
            when {
                errorMsg.contains("404") || errorMsg.contains("not found") -> {
                    "Error: The AI model 'gemini-1.5-flash' was not found. Please ensure your API key is valid for this model in Google AI Studio."
                }
                errorMsg.contains("Serialization") || errorMsg.contains("Field 'details'") -> {
                    "Error: Unexpected response format from AI service. Please check your internet or try again later."
                }
                else -> "Error: ${e.localizedMessage ?: "Unknown error occurred"}"
            }
        }
    }

    private fun extractValue(json: String, key: String): String {
        // More robust extraction for JSON values
        val quotedPattern = "\"$key\":\\s*\"(.*?)\"".toRegex(RegexOption.DOT_MATCHES_ALL)
        val unquotedPattern = "\"$key\":\\s*([^,}]+)".toRegex()
        
        return quotedPattern.find(json)?.groupValues?.get(1) 
            ?: unquotedPattern.find(json)?.groupValues?.get(1)?.trim() 
            ?: "0"
    }
}
