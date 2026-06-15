package com.example.uas_ppb.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_ppb.data.Member
import com.example.uas_ppb.data.Transaction
import com.example.uas_ppb.ui.viewmodel.CoffeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(
    memberId: Int,
    viewModel: CoffeeViewModel,
    onBack: () -> Unit,
    onAddTransaction: () -> Unit,
    onRedeemReward: () -> Unit
) {
    val member by viewModel.getMember(memberId).collectAsState(initial = null)
    val transactions by viewModel.getTransactions(memberId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Member Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                    DigitalMemberCard(member = m)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = onAddTransaction,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                        ) {
                            Text("Add Transaction")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
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
fun DigitalMemberCard(member: Member) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CardMembership, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("COFFEE BLISS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = member.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = member.memberId, color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column {
                    Text(text = "POINTS", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(text = member.points.toString(), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "LEVEL", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(text = member.level, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
                Icon(Icons.Default.QrCode2, contentDescription = "QR Code", tint = Color.White, modifier = Modifier.size(64.dp))
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val isRedeem = transaction.pointEarned < 0
    val pointColor = if (isRedeem) Color.Red else Color(0xFF1B5E20)
    val icon = if (isRedeem) Icons.Default.Redeem else Icons.Default.ShoppingCart
    val pointPrefix = if (transaction.pointEarned > 0) "+" else ""

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isRedeem) Color(0xFFE65100) else Color(0xFF1B5E20))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = transaction.title, fontWeight = FontWeight.Bold)
                Text(text = transaction.date, fontSize = 12.sp, color = Color.Gray)
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
