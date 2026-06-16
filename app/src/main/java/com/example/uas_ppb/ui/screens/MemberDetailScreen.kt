package com.example.uas_ppb.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_ppb.data.Member
import com.example.uas_ppb.data.Transaction
import com.example.uas_ppb.ui.viewmodel.CoffeeViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(
    memberId: Int,
    viewModel: CoffeeViewModel,
    onProfileClick: () -> Unit,
    onAddTransaction: () -> Unit,
    onRedeemReward: () -> Unit
) {
    val member by viewModel.getMember(memberId).collectAsState(initial = null)
    val transactions by viewModel.getTransactions(memberId).collectAsState(initial = emptyList())
    var showQrDialog by remember { mutableStateOf(false) }

    if (showQrDialog && member != null) {
        QrMemberDialog(
            member = member!!,
            onDismiss = { showQrDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Membership Profile",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp
                    )
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.ManageAccounts,
                            contentDescription = "Profile Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1B5E20))
            )
        }
    ) { padding ->
        member?.let { m ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                item {
                    DigitalMemberCard(
                        member = m,
                        onQrClick = { showQrDialog = true }
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = onAddTransaction,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                        ) {
                            Text("Add Transaction")
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(
                            onClick = onRedeemReward,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                        ) {
                            Text("Redeem Reward")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Points & Rewards History", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (transactions.isEmpty()) {
                    item {
                        Text("No history yet.", color = Color.Gray, modifier = Modifier.padding(vertical = 16.dp))
                    }
                } else {
                    items(transactions) { tx ->
                        TransactionItem(transaction = tx)
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF1B5E20))
        }
    }
}

@Composable
fun DigitalMemberCard(member: Member, onQrClick: () -> Unit) {
    val levelColor = when (member.level) {
        "Gold" -> Color(0xFFFFD54F)
        "Silver" -> Color(0xFFCFD8DC)
        else -> Color(0xFFD7B899)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CardMembership, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Column {
                    Text("COFFEE BLISS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(
                        text = "Member Privileges",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 12.sp,
                        letterSpacing = 0.8.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = member.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = member.memberId, color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(text = "POINTS", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(text = member.points.toString(), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    if (member.nextLevel != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${member.pointsToNextLevel} pts to ${member.nextLevel}",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Top membership tier unlocked",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "LEVEL", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = levelColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = member.level,
                            color = levelColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Icon(
                    Icons.Default.QrCode2,
                    contentDescription = "Show QR Code",
                    tint = Color.White,
                    modifier = Modifier
                        .size(64.dp)
                        .clickable(onClick = onQrClick)
                )
            }
        }
    }
}

@Composable
private fun QrMemberDialog(member: Member, onDismiss: () -> Unit) {
    val qrContent = "memberId=${member.memberId};name=${member.name};email=${member.email};phone=${member.phone};level=${member.level}"
    val qrBitmap = remember(qrContent) { generateQrBitmap(qrContent, 720) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Member QR Pass", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF1B5E20))
                Spacer(modifier = Modifier.height(8.dp))
                Text(member.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text(member.memberId, color = Color.Gray)
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    bitmap = qrBitmap.asImageBitmap(),
                    contentDescription = "Member QR Code",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                MemberInfoRow("Member ID", member.memberId)
                MemberInfoRow("Name", member.name)
//                MemberInfoRow("Email", member.email)
//                MemberInfoRow("Phone", member.phone)
                MemberInfoRow("Level", member.level)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
private fun MemberInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        Text(value, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val isRedeem = transaction.pointEarned < 0
    val pointColor = if (isRedeem) Color.Red else Color(0xFF1B5E20)
    val icon = if (isRedeem) Icons.Default.Redeem else Icons.Default.ShoppingCart
    val pointPrefix = if (transaction.pointEarned > 0) "+" else ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isRedeem) Color(0xFFE65100) else Color(0xFF1B5E20))
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(text = transaction.title, fontWeight = FontWeight.Bold)
                Text(text = transaction.displayDate, fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                if (transaction.amount > 0) {
                    Text(text = "Rp ${transaction.amount.toInt()}", fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "$pointPrefix${transaction.pointEarned} pts",
                    color = pointColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun generateQrBitmap(content: String, size: Int): Bitmap {
    val hints = mapOf(EncodeHintType.MARGIN to 1)
    val matrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return bitmap
}
