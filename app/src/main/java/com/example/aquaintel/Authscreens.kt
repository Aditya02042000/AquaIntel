package com.example.aquaintel

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider

import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.CoralRed
import com.example.aquaintel.ui.theme.Cream
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.GrayMid
import com.example.aquaintel.ui.theme.LeafGreen
import com.example.aquaintel.ui.theme.White

private val GoogleRed = Color(0xFFDB4437)
private val FacebookBlue = Color(0xFF1877F2)
private val TwitterBlack = Color(0xFF000000)

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val auth = remember { FirebaseAuth.getInstance() }

    val googleSignInClient: GoogleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken("YOUR_WEB_CLIENT_ID_HERE")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val googleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isLoading = false
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnSuccessListener { onLogin() }
                .addOnFailureListener { e ->
                    errorMessage = e.localizedMessage ?: "Google Sign In Failed"
                }
        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Google Sign In Error"
        }
    }

    val callbackManager = remember { CallbackManager.Factory.create() }

    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    isLoading = true
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    auth.signInWithCredential(credential)
                        .addOnSuccessListener {
                            isLoading = false
                            onLogin()
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            errorMessage = e.localizedMessage ?: "Facebook Login Failed"
                        }
                }
                override fun onCancel() { isLoading = false }
                override fun onError(error: FacebookException) {
                    isLoading = false
                    errorMessage = error.localizedMessage ?: "Facebook Error"
                }
            }
        )
        onDispose { LoginManager.getInstance().unregisterCallback(callbackManager) }
    }

    fun signInWithTwitter() {
        if (activity == null) return
        isLoading = true
        val provider = OAuthProvider.newBuilder("twitter.com")
        auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener {
                isLoading = false
                onLogin()
            }
            .addOnFailureListener { e ->
                isLoading = false
                errorMessage = e.localizedMessage ?: "Twitter Login Failed"
            }
    }

    Box(modifier = Modifier.fillMaxSize().background(Cream)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(Brush.verticalGradient(listOf(ForestGreen, LeafGreen)))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(0f, size.height * 0.75f)
                    cubicTo(
                        size.width * 0.25f, size.height,
                        size.width * 0.75f, size.height * 0.55f,
                        size.width, size.height * 0.8f
                    )
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(path, Cream)
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(72.dp).background(White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(44.dp)) {
                        val cx = size.width / 2
                        val cy = size.height / 2
                        val r = size.width * 0.38f
                        drawRoundRect(
                            color = White,
                            topLeft = Offset(cx - r * 0.28f, cy - r),
                            size = Size(r * 0.56f, r * 2f),
                            cornerRadius = CornerRadius(r * 0.14f)
                        )
                        drawRoundRect(
                            color = White,
                            topLeft = Offset(cx - r, cy - r * 0.28f),
                            size = Size(r * 2f, r * 0.56f),
                            cornerRadius = CornerRadius(r * 0.14f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "AquaIntel", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = White)
                Text(text = "Sign in to continue", fontSize = 13.sp, color = White.copy(alpha = 0.75f))
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "Welcome back 👋", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Charcoal)

                AppTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    leadingIcon = Icons.Default.Email
                )

                AppTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = !showPass,
                    trailingIcon = {
                        IconButton(onClick = { showPass = !showPass }) {
                            Icon(
                                imageVector = if (showPass) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )

                AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                    Card(colors = CardDefaults.cardColors(containerColor = CoralRed.copy(alpha = 0.1f))) {
                        Text(text = errorMessage, color = CoralRed, modifier = Modifier.padding(12.dp))
                    }
                }

                Button(
                    onClick = {
                        errorMessage = ""
                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Please fill all fields"
                        } else {
                            isLoading = true
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener {
                                    onLogin() // onLogin now handles loading the user data in MainApp
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    errorMessage = e.localizedMessage ?: "Login Failed"
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = White, strokeWidth = 2.dp)
                    } else {
                        Text(text = "Sign In", fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SocialLoginButton(
                        modifier = Modifier.weight(1f),
                        label = "Google",
                        borderColor = GrayMid.copy(alpha = 0.2f),
                        enabled = !isLoading,
                        icon = { Icon(painter = painterResource(R.drawable.google), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(20.dp)) },
                        onClick = { googleLauncher.launch(googleSignInClient.signInIntent) }
                    )
                    SocialLoginButton(
                        modifier = Modifier.weight(1f),
                        label = "Facebook",
                        borderColor = GrayMid.copy(alpha = 0.2f),
                        enabled = !isLoading,
                        icon = { Icon(painter = painterResource(R.drawable.facebook), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(20.dp)) },
                        onClick = {
                            activity?.let {
                                LoginManager.getInstance().logInWithReadPermissions(it, callbackManager, listOf("email", "public_profile"))
                            }
                        }
                    )
                    SocialLoginButton(
                        modifier = Modifier.weight(1f),
                        label = "Twitter",
                        borderColor = GrayMid.copy(alpha = 0.2f),
                        enabled = !isLoading,
                        icon = { Icon(painter = painterResource(R.drawable.twitter), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(24.dp)) },
                        onClick = { signInWithTwitter() }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Don't have an account?")
                    TextButton(onClick = onSignUp) {
                        Text(text = "Sign Up", color = LeafGreen)
                    }
                }
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    modifier: Modifier = Modifier,
    label: String,
    borderColor: Color,
    enabled: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = borderColor),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = White),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon()
            Text(text = label, fontSize = 10.sp, color = GrayMid, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SignUpScreen(
    onSignUp: (User) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val auth = remember { FirebaseAuth.getInstance() }

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopBarWithBack(
                title = "Create Account",
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Join AquaIntel",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )

            Text(
                text = "Start tracking your health today",
                fontSize = 14.sp,
                color = GrayMid
            )

            Spacer(modifier = Modifier.height(20.dp))

            AppTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = "Full Name",
                leadingIcon = Icons.Default.Person
            )

            AppTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                leadingIcon = Icons.Default.Email
            )

            AppTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )

            AppTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )

            if (errorMessage.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CoralRed.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = errorMessage, color = CoralRed, modifier = Modifier.padding(12.dp), fontSize = 12.sp)
                }
            }

            Button(
                onClick = {
                    errorMessage = ""
                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill all fields"
                        return@Button
                    }
                    if (password != confirmPassword) {
                        errorMessage = "Passwords do not match"
                        return@Button
                    }

                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            val newUser = User(
                                name = name,
                                email = email,
                                joinDate = java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                                healthScore = 75 // Starting score
                            )
                            onSignUp(newUser)
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            errorMessage = e.localizedMessage ?: "Sign up failed"
                        }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Create Account", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
