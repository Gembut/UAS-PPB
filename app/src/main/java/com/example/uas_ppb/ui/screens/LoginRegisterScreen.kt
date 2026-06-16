package com.example.uas_ppb.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_ppb.ui.viewmodel.CoffeeViewModel
import com.example.uas_ppb.ui.viewmodel.LoginResult
import com.example.uas_ppb.ui.viewmodel.RegisterResult
import kotlinx.coroutines.launch

@Composable
fun LoginRegisterScreen(
    viewModel: CoffeeViewModel,
    onLoginSuccess: (Int) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isLoginMode by remember { mutableStateOf(true) }
    var loginIdentifier by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9)), // Light green background
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Coffee Icon
            Icon(
                imageVector = Icons.Default.Coffee,
                contentDescription = null,
                tint = Color(0xFF1B5E20),
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Coffee Bliss",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )
            Text(
                text = if (isLoginMode) "Sign in to see your membership" else "Join membership card to get rewards",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Input Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isLoginMode) "LOGIN" else "REGISTER",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (isLoginMode) {
                        OutlinedTextField(
                            value = loginIdentifier,
                            onValueChange = {
                                loginIdentifier = it
                                errorMessage = null
                            },
                            label = { Text("Email atau No. Telepon") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF1B5E20)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1B5E20),
                                focusedLabelColor = Color(0xFF1B5E20)
                            )
                        )
                    } else {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                errorMessage = null
                            },
                            label = { Text("Nama Lengkap") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF1B5E20)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1B5E20),
                                focusedLabelColor = Color(0xFF1B5E20)
                            )
                        )
                    }

                    // Email Field (Register Only)
                    if (!isLoginMode) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                errorMessage = null
                            },
                            label = { Text("Email") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF1B5E20)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1B5E20),
                                focusedLabelColor = Color(0xFF1B5E20)
                            )
                        )

                        // Phone Field (Register Only)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = {
                                phone = it
                                errorMessage = null
                            },
                            label = { Text("No. Telepon") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF1B5E20)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1B5E20),
                                focusedLabelColor = Color(0xFF1B5E20)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF1B5E20)) },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(image, contentDescription = null, tint = Color(0xFF1B5E20))
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1B5E20),
                            focusedLabelColor = Color(0xFF1B5E20)
                        )
                    )

                    // Confirm Password Field (Register Mode Only)
                    if (!isLoginMode) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                errorMessage = null
                            },
                            label = { Text("Confirm Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF1B5E20)) },
                            singleLine = true,
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(image, contentDescription = null, tint = Color(0xFF1B5E20))
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1B5E20),
                                focusedLabelColor = Color(0xFF1B5E20)
                            )
                        )
                    }

                    // Error message print
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (isLoading) return@Button
                            errorMessage = null

                            if (isLoginMode && (loginIdentifier.isBlank() || password.isBlank())) {
                                errorMessage = "Email/no. telepon dan password harus diisi"
                                return@Button
                            }

                            if (!isLoginMode && (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() || confirmPassword.isBlank())) {
                                errorMessage = "Semua field registrasi harus diisi"
                                return@Button
                            }

                            if (!isLoginMode && password != confirmPassword) {
                                errorMessage = "Password konfirmasi tidak cocok"
                                return@Button
                            }

                            isLoading = true
                            coroutineScope.launch {
                                if (isLoginMode) {
                                    when (val result = viewModel.login(loginIdentifier.trim(), password)) {
                                        is LoginResult.Success -> {
                                            Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                                            onLoginSuccess(result.memberId)
                                        }
                                        LoginResult.InvalidCredentials -> {
                                            errorMessage = "Email/no. telepon atau password salah"
                                        }
                                        LoginResult.EmptyFields -> {
                                            errorMessage = "Email/no. telepon dan password tidak boleh kosong"
                                        }
                                    }
                                } else {
                                    when (viewModel.register(name.trim(), email.trim(), phone.trim(), password)) {
                                        RegisterResult.Success -> {
                                            Toast.makeText(context, "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_SHORT).show()
                                            isLoginMode = true
                                            loginIdentifier = ""
                                            name = ""
                                            email = ""
                                            phone = ""
                                            password = ""
                                            confirmPassword = ""
                                        }
                                        RegisterResult.MemberAlreadyExists -> {
                                            errorMessage = "Email atau no. telepon sudah terdaftar"
                                        }
                                        RegisterResult.EmptyFields -> {
                                            errorMessage = "Semua field harus diisi"
                                        }
                                    }
                                }
                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                    ) {
                        Text(
                            text = if (isLoading) "Loading..." else if (isLoginMode) "LOGIN" else "REGISTER",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Switch Mode Button
            TextButton(
                onClick = {
                    isLoginMode = !isLoginMode
                    errorMessage = null
                    loginIdentifier = ""
                    name = ""
                    email = ""
                    phone = ""
                    password = ""
                    confirmPassword = ""
                }
            ) {
                Text(
                    text = if (isLoginMode) "Belum punya akun? Registrasi di sini" else "Sudah punya akun? Login di sini",
                    color = Color(0xFF1B5E20),
                    fontSize = 14.sp
                )
            }
        }
    }
}
