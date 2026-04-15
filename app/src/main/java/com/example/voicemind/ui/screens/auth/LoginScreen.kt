package com.example.voicemind.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voicemind.R
import com.example.voicemind.ui.theme.VocabMindTheme
import com.example.voicemind.ui.viewmodel.AuthState
import com.example.voicemind.ui.viewmodel.AuthViewModel

private val PrimaryPurple = Color(0xFF5E27FD)
private val BackgroundGray = Color(0xFFF7F8FA)
private val TextFieldBgGray = Color(0xFFF9FAFB)
private val TextDark = Color(0xFF1F2937)
private val TextGray = Color(0xFF6B7280)
private val CardShadowColor = Color(0x0D000000)

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onNavigateToHome()
            viewModel.resetState()
        }
    }

    LoginContent(
        email = email,
        onEmailChange = { email = it },
        password = password,
        passwordVisible = passwordVisible,
        onPasswordChange = { password = it },
        onPasswordVisibilityChange = { passwordVisible = it },
        onSignInClick = { viewModel.signInWithEmail(email, password) },
        onGoogleSignInClick = {},
        onAppleSignInClick = {},
        onForgotPasswordClick = {},
        onJoinClick = onNavigateToRegister,
        isLoading = authState is AuthState.Loading,
        errorMessage = (authState as? AuthState.Error)?.message
    )
}

@Composable
private fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    passwordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onSignInClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onAppleSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onJoinClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // Decorative background elements
        FloatingLabel(
            text = "Ephemeral",
            rotation = -15f,
            modifier = Modifier
                .offset(x = 20.dp, y = 100.dp)
        )
        FloatingLabel(
            text = "Celestial",
            rotation = 10f,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-10).dp, y = 150.dp)
        )
        FloatingLabel(
            text = "E",
            rotation = -10f,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-10).dp, y = 100.dp)
        )

        // Bottom background image
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(400.dp)
                .offset(x = 100.dp, y = 100.dp)
                .clip(CircleShape)
                .background(Color(0xFFE5E7EB).copy(alpha = 0.5f))
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.man),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .offset(x = (-50).dp, y = 20.dp)
//            )
            Text(
                text = "Eloquence",
                color = TextGray.copy(alpha = 0.5f),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 40.dp, x = (-30).dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryPurple,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.book),
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Main Title
            Text(
                text = stringResource(id = R.string.welcome_back_title),
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.login_subtitle),
                fontSize = 16.sp,
                color = TextGray,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Login Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardShadowColor, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Email Field
                    Text(
                        text = stringResource(id = R.string.email_address),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    AuthTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        placeholder = stringResource(id = R.string.email_hint),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.mail),
                                contentDescription = null,
                                tint = TextGray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Password Field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.password),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = stringResource(id = R.string.forgot_password),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPurple,
                            modifier = Modifier.clickable { onForgotPasswordClick() }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    AuthTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        placeholder = "••••••••",
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityChange = onPasswordVisibilityChange,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.lock_password),
                                contentDescription = null,
                                tint = TextGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Sign In Button
                    Button(
                        onClick = onSignInClick,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.sign_in),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Divider
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE5E7EB)
                        )
                        Text(
                            text = stringResource(id = R.string.or_continue_with),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextGray,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            letterSpacing = 0.5.sp
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE5E7EB)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Social Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SocialButton(
                            text = stringResource(id = R.string.google),
                            icon = R.drawable.ic_google,
                            onClick = onGoogleSignInClick,
                            containerColor = Color.White,
                            contentColor = TextDark,
                            modifier = Modifier.weight(1f)
                        )
                        SocialButton(
                            text = stringResource(id = R.string.apple),
                            icon = R.drawable.ic_apple_white,
                            onClick = onAppleSignInClick,
                            containerColor = Color(0xFF111827),
                            contentColor = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Join VocabMind Link
            val annotatedText = buildAnnotatedString {
                append(stringResource(id = R.string.dont_have_account))

                pushStringAnnotation(
                    tag = "JOIN",
                    annotation = "join"
                )

                withStyle(
                    style = SpanStyle(
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(id = R.string.join_vocabmind))
                }

                pop()
            }

            ClickableText(
                text = annotatedText,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 32.dp),
                onClick = { offset ->
                    annotatedText
                        .getStringAnnotations("JOIN", offset, offset)
                        .firstOrNull()
                        ?.let {
                            onJoinClick()
                        }
                }
            )
        }
    }
}

@Composable
private fun FloatingLabel(
    text: String,
    rotation: Float,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.6f),
        modifier = modifier.rotate(rotation)
    ) {
        Text(
            text = text,
            color = PrimaryPurple.copy(alpha = 0.3f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: ((Boolean) -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color(0xFF9CA3AF)) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = TextFieldBgGray,
            unfocusedContainerColor = TextFieldBgGray,
            disabledContainerColor = TextFieldBgGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = leadingIcon,
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onPasswordVisibilityChange?.invoke(!passwordVisible) }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility",
                        tint = TextGray
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(16.dp))
    )
}

@Composable
private fun SocialButton(
    text: String,
    icon: Int,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        modifier = modifier
            .height(56.dp)
            .border(
                width = if (containerColor == Color.White) 1.dp else 0.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    VocabMindTheme {
        LoginContent(
            email = "",
            onEmailChange = {},
            password = "",
            passwordVisible = false,
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onSignInClick = {},
            onGoogleSignInClick = {},
            onAppleSignInClick = {},
            onForgotPasswordClick = {},
            onJoinClick = {}
        )
    }
}