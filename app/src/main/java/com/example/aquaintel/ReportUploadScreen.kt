package com.example.aquaintel

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaintel.*
import com.example.aquaintel.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportUploadScreen(
    user: User,
    onReportAnalyzed: (User) -> Unit,
    onBack: () -> Unit
) {
    var reportText by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var analysisResult by remember { mutableStateOf<AnalysisResult?>(null) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                fileName = it.path?.substringAfterLast("/") ?: "Selected Report"
                reportText = "File Selected: $fileName\n\n[Analysing this file...]"
            }
        }
    )

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopAppBar(
                title = { Text("AI Report Analysis", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (analysisResult == null) {
                Icon(
                    Icons.Default.CloudUpload,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = ForestGreen
                )

                Text(
                    "Upload report or paste text to update your health profile.",
                    fontSize = 14.sp,
                    color = GrayMid,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { filePickerLauncher.launch(arrayOf("application/pdf", "image/*")) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.AttachFile, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Pick File", fontSize = 12.sp)
                    }
                    
                    Button(
                        onClick = {
                            reportText = "Patient Age: 28. Heart Rate: 72 bpm. BP: 120/80 mmHg. Oxygen: 99%. Weight: 65kg. Height: 170cm."
                            fileName = null
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SageGreen)
                    ) {
                        Text("Use Sample", fontSize = 12.sp)
                    }
                }

                if (fileName != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MintGreen.copy(alpha = 0.2f))
                    ) {
                        Text(fileName!!, modifier = Modifier.padding(12.dp), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }

                OutlinedTextField(
                    value = reportText,
                    onValueChange = { reportText = it },
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    placeholder = { Text("Paste report content...") },
                    shape = RoundedCornerShape(12.dp)
                )
                
                Button(
                    onClick = {
                        isAnalyzing = true
                        scope.launch {
                            val result = GeminiUtils.analyzeReport(context, reportText)
                            isAnalyzing = false
                            analysisResult = result
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    enabled = !isAnalyzing && reportText.isNotBlank()
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Analyze with AI", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Show Analysis Results
                Text("Analysis Results", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Charcoal)
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ResultRow("Heart Rate", analysisResult!!.updatedUser.heartRate + " bpm")
                        ResultRow("Blood Pressure", analysisResult!!.updatedUser.bloodPressure + " mmHg")
                        ResultRow("Oxygen Level", analysisResult!!.updatedUser.oxygenLevel + "%")
                        ResultRow("Health Score", analysisResult!!.updatedUser.healthScore.toString() + "/100")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SageGreen.copy(alpha = 0.1f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("AI Suggestions", fontWeight = FontWeight.Bold, color = ForestGreen)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(analysisResult!!.suggestions, fontSize = 13.sp, color = Charcoal)
                    }
                }

                Button(
                    onClick = {
                        val finalUser = user.copy(
                            age = analysisResult!!.updatedUser.age,
                            weight = analysisResult!!.updatedUser.weight,
                            height = analysisResult!!.updatedUser.height,
                            heartRate = analysisResult!!.updatedUser.heartRate,
                            bloodPressure = analysisResult!!.updatedUser.bloodPressure,
                            oxygenLevel = analysisResult!!.updatedUser.oxygenLevel,
                            healthScore = analysisResult!!.updatedUser.healthScore
                        )
                        onReportAnalyzed(finalUser)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Apply Changes to Profile", fontWeight = FontWeight.Bold)
                }

                TextButton(onClick = { analysisResult = null }) {
                    Text("Re-analyze another report", color = GrayMid)
                }
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = GrayMid, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, color = Charcoal, fontSize = 14.sp)
    }
}
