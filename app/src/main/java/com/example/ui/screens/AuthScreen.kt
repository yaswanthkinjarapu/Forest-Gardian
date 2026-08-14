package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.UserRole

@Composable
fun AuthScreen(
    onLoginSuccess: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPhoneMode by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("rajesh.kumar@forest.gov.in") }
    var password by remember { mutableStateOf("••••••••") }
    var phone by remember { mutableStateOf("+91 98765 43210") }
    var otpCode by remember { mutableStateOf("") }
    var showOtpDialog by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.FOREST_OFFICER) }
    var biometricEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F15))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Emblem Icon
            Surface(
                shape = CircleShape,
                color = Color(0xFF1E4620),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF2ECC71))),
                modifier = Modifier.size(96.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Forest Guardian",
                        tint = Color(0xFF2ECC71),
                        modifier = Modifier.size(54.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "FOREST GUARDIAN",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "AI Forest Surveillance & Incident Portal",
                fontSize = 13.sp,
                color = Color(0xFFA5D6A7)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isPhoneMode) "Phone OTP Login" else "Official Email Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (!isPhoneMode) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Government Email ID") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF2ECC71)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2ECC71),
                                unfocusedBorderColor = Color(0xFF294E38),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF2ECC71)) },
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2ECC71),
                                unfocusedBorderColor = Color(0xFF294E38),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Registered Officer Mobile Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF2ECC71)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2ECC71),
                                unfocusedBorderColor = Color(0xFF294E38),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Role Selector Tabs
                    Text("Select Initial Role:", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        UserRole.values().forEach { role ->
                            FilterChip(
                                selected = selectedRole == role,
                                onClick = { selectedRole = role },
                                label = {
                                    Text(
                                        when (role) {
                                            UserRole.FOREST_OFFICER -> "Officer"
                                            UserRole.RANGE_OFFICER -> "Ranger"
                                            UserRole.ADMIN -> "Admin"
                                        },
                                        fontSize = 11.sp
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Biometric App-Lock option
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = Color(0xFF2ECC71))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Enable Biometric App-Lock", fontSize = 13.sp, color = Color.White)
                        }
                        Switch(
                            checked = biometricEnabled,
                            onCheckedChange = { biometricEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2ECC71))
                        )
                    }

                    // Action Buttons
                    Button(
                        onClick = {
                            if (isPhoneMode) {
                                showOtpDialog = true
                            } else {
                                onLoginSuccess(selectedRole)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = if (isPhoneMode) "Send OTP SMS" else "Authenticate & Sign In",
                            color = Color(0xFF0D1F15),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    // Google Sign-In Option
                    OutlinedButton(
                        onClick = { onLoginSuccess(selectedRole) },
                        border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF2ECC71))),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color(0xFF2ECC71))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign In with Google Identity", color = Color.White)
                    }

                    TextButton(
                        onClick = { isPhoneMode = !isPhoneMode },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = if (isPhoneMode) "Use Email/Password instead" else "Use Phone OTP Verification",
                            color = Color(0xFF81C784),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Phone OTP Dialog Simulation
        if (showOtpDialog) {
            AlertDialog(
                onDismissRequest = { showOtpDialog = false },
                title = { Text("Enter 6-Digit SMS OTP") },
                text = {
                    Column {
                        Text("Verification code sent to $phone")
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = otpCode,
                            onValueChange = { if (it.length <= 6) otpCode = it },
                            label = { Text("6-Digit Code (Enter 123456)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        showOtpDialog = false
                        onLoginSuccess(selectedRole)
                    }) {
                        Text("Verify & Continue")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showOtpDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
