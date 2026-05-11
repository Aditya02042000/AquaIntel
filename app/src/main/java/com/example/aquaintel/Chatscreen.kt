package com.example.aquaintel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.graphics.Color
import com.example.aquaintel.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(onBack: () -> Unit = {}) {
    val scope = rememberCoroutineScope()

    val msgs = remember {
        mutableStateListOf(
            ChatMessage(
                "👋 Hi! I'm your AquaIntel AI. Ask me anything about your health, nutrition, or wellness!",
                false,
                "9:30 AM"
            )
        )
    }

    var input by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }

    val suggestions = listOf(
        "Analyze my report 📄",
        "Nutrition advice 🥗",
        "Exercise tips 🏃",
        "How's my BP? 🩸"
    )

    val chatModel = remember { AquaIntelChatModel() }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        msgs.add(ChatMessage(text, true, "Now"))
        input = ""
        isTyping = true
        
        scope.launch {
            val response = chatModel.getChatResponse(text) ?: "I'm sorry, I couldn't process that."
            isTyping = false
            msgs.add(ChatMessage(response, false, "Now"))
        }
    }

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(34.dp).background(MintGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text("🤖") }

                        Spacer(Modifier.width(8.dp))

                        Column {
                            Text("AI Health Assistant", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(if (isTyping) "Typing..." else "Online", fontSize = 10.sp, color = LeafGreen)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(msgs) { msg ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            Modifier
                                .background(
                                    if (msg.isUser) ForestGreen else White,
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(10.dp)
                        ) {
                            Text(
                                msg.text,
                                color = if (msg.isUser) White else Charcoal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            LazyRow(
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(suggestions) { s ->
                    Box(
                        Modifier
                            .background(White, RoundedCornerShape(20.dp))
                            .border(1.dp, CreamDark, RoundedCornerShape(20.dp))
                            .clickable { sendMessage(s) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(s, fontSize = 12.sp)
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask something...") },
                    singleLine = true,
                    enabled = !isTyping
                )

                Spacer(Modifier.width(8.dp))

                IconButton(
                    onClick = { sendMessage(input) },
                    enabled = !isTyping && input.isNotBlank()
                ) {
                    if (isTyping) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}