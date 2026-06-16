package com.example.uas_ppb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_ppb.ui.viewmodel.CoffeeViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    memberId: Int,
    viewModel: CoffeeViewModel,
    onBack: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    val member by viewModel.getMember(memberId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            AppTopBar(
                title = "New Transaction",
                subtitle = "Coffee Bliss",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            member?.let { m ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF17362C),
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Transaction for",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.76f)
                            )
                            Text(
                                text = m.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = m.memberId,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null,
                            tint = Color(0xFFFFCC80),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { input -> 
                        if (input.all { it.isDigit() }) {
                            amount = input 
                        }
                    },
                    label = { Text("Purchase Amount") },
                    prefix = { Text("Rp. ") }, // Menambahkan titik setelah Rp
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    visualTransformation = ThousandSeparatorTransformation(), // Otomatis beri titik pemisah
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val pointsPreview = if (amount.isNotBlank()) (amount.toDoubleOrNull() ?: 0.0) / 10000 else 0.0
                Surface(
                    color = Color(0xFF17362C),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Points to be earned:",
                            color = Color.White.copy(alpha = 0.78f)
                        )
                        Text(
                            text = "${pointsPreview.toInt()} pts",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    onClick = {
                        val amt = amount.toDoubleOrNull()
                        if (amt != null && amt > 0) {
                            viewModel.addTransaction(memberId, amt)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = amount.isNotBlank()
                ) {
                    Text("Save Transaction")
                }
            }
        }
    }
}

/**
 * VisualTransformation untuk menambahkan titik pemisah ribuan secara otomatis saat mengetik.
 */
class ThousandSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formattedText = NumberFormat.getInstance(Locale("id", "ID"))
            .format(originalText.toLongOrNull() ?: 0L)
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val originalSub = originalText.substring(0, offset)
                val transformedSub = NumberFormat.getInstance(Locale("id", "ID"))
                    .format(originalSub.toLongOrNull() ?: 0L)
                return transformedSub.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                val transformedSub = formattedText.substring(0, offset)
                return transformedSub.count { it.isDigit() }
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}
