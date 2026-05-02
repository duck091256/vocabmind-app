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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voicemind.R
import com.example.voicemind.ui.theme.VocabMindTheme

private val PrimaryPurple = Color(0xFF5E27FD)
private val BackgroundStart = Color(0xFFF1F5F9)
private val BackgroundEnd = Color(0xFFE2E8F0)
private val TextFieldBgGray = Color(0xFFF1F5F9)
private val TextDark = Color(0xFF0F172A)
private val TextGray = Color(0xFF64748B)
private val CardShadowColor = Color(0x0D000000)

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    RegisterContent(
        fullName = fullName,
        onFullNameChange = { fullName = it },
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        passwordVisible = passwordVisible,
        onPasswordVisibilityChange = { passwordVisible = it },
        agreeToTerms = agreeToTerms,
        onAgreeToTermsChange = { agreeToTerms = it },
        onSignUpClick = { viewModel.signUpWithEmail(email, password) },
        onGoogleSignUpClick = {},
        onAppleSignUpClick = {},
        onLoginClick = onNavigateToLogin,
        isLoading = authState is AuthState.Loading,
        errorMessage = (authState as? AuthState.Error)?.message
    )
}

@Composable
private fun RegisterContent(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    agreeToTerms: Boolean,
    onAgreeToTermsChange: (Boolean) -> Unit,
    onSignUpClick: () -> Unit,
    onGoogleSignUpClick: () -> Unit,
    onAppleSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BackgroundStart, BackgroundEnd)
                )
            )
    ) {
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

            Spacer(modifier = Modifier.height(40.dp))

            // Register Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardShadowColor, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.create_account),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.register_subtitle),
                        fontSize = 15.sp,
                        color = TextGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Full Name Field
                    FieldLabel(text = stringResource(id = R.string.full_name))
                    AuthTextField(
                        value = fullName,
                        onValueChange = onFullNameChange,
                        placeholder = stringResource(id = R.string.full_name_hint),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.user),
                                contentDescription = null,
                                tint = TextGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Email Field
                    FieldLabel(text = stringResource(id = R.string.email_address))
                    AuthTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        placeholder = stringResource(id = R.string.scholar_email_hint),
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

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Field
                    FieldLabel(text = stringResource(id = R.string.password))
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Terms and Conditions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Checkbox(
                            checked = agreeToTerms,
                            onCheckedChange = onAgreeToTermsChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = PrimaryPurple,
                                uncheckedColor = Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = TextGray)) {
                                    append(stringResource(id = R.string.agree_to_terms))
                                }
                                withStyle(style = SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.Bold)) {
                                    append(stringResource(id = R.string.terms_of_service))
                                }
                                withStyle(style = SpanStyle(color = TextGray)) {
                                    append(stringResource(id = R.string.and))
                                }
                                withStyle(style = SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.Bold)) {
                                    append(stringResource(id = R.string.privacy_policy))
                                }
                            },
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }

                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign Up Button
                    Button(
                        onClick = onSignUpClick,
                        enabled = !isLoading && agreeToTerms,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
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
                                    text = stringResource(id = R.string.sign_up),
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Divider
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE2E8F0)
                        )
                        Text(
                            text = stringResource(id = R.string.or_continue_with),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextGray,
                            modifier = Modifier.padding(horizontal = 12.dp),
                            letterSpacing = 0.5.sp
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE2E8F0)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Social Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SocialButton(
                            text = stringResource(id = R.string.google),
                            icon = R.drawable.ic_google,
                            onClick = onGoogleSignUpClick,
                            modifier = Modifier.weight(1f)
                        )
                        SocialButton(
                            text = stringResource(id = R.string.apple),
                            icon = R.drawable.ic_apple,
                            onClick = onAppleSignUpClick,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Login Link
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = TextGray)) {
                                append(stringResource(id = R.string.already_have_account))
                            }
                            withStyle(style = SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = R.string.log_in))
                            }
                        },
                        modifier = Modifier.clickable { onLoginClick() },
                        fontSize = 15.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Bottom Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9).copy(alpha = 0.8f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PrimaryPurple,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.sparkle),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TextDark)) {
                                    append(stringResource(id = R.string.unlock_ai_mastery))
                                }
                                withStyle(style = SpanStyle(color = TextGray)) {
                                    append(stringResource(id = R.string.unlock_ai_mastery_desc))
                                }
                            },
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }

        // Bottom Navigation Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(80.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    icon = R.drawable.log_out,
                    label = stringResource(id = R.string.sign_in),
                    selected = false,
                    onClick = onLoginClick,
                    modifier = Modifier.weight(1f)
                )
                NavItem(
                    icon = R.drawable.user,
                    label = stringResource(id = R.string.join),
                    selected = true,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        letterSpacing = 0.5.sp
    )
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
        placeholder = { Text(text = placeholder, color = Color(0xFF94A3B8), fontSize = 15.sp) },
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
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}

@Composable
private fun SocialButton(
    text: String,
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = modifier
            .height(56.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFE2E8F0),
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
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Surface(
                color = PrimaryPurple.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
            ) {}
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = if (selected) PrimaryPurple else TextGray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) PrimaryPurple else TextGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    VocabMindTheme {
        RegisterContent(
            fullName = "",
            onFullNameChange = {},
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            passwordVisible = false,
            onPasswordVisibilityChange = {},
            agreeToTerms = true,
            onAgreeToTermsChange = {},
            onSignUpClick = {},
            onGoogleSignUpClick = {},
            onAppleSignUpClick = {},
            onLoginClick = {}
        )
    }
}