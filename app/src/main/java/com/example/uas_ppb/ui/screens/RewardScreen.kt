package com.example.uas_ppb.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_ppb.ui.viewmodel.CoffeeViewModel

private enum class RewardCategory(
    val title: String,
    val icon: ImageVector,
    val accentColor: Color
) {
    DRINK("Minuman", Icons.Default.Coffee, Color(0xFF2E7D32)),
    FOOD("Makanan", Icons.Default.Fastfood, Color(0xFFE65100)),
    MERCH("Merch", Icons.Default.WorkspacePremium, Color(0xFF1565C0))
}

private data class Reward(
    val name: String,
    val points: Int,
    val category: RewardCategory
)

private val rewardList = listOf(
    Reward("Espresso", 50, RewardCategory.DRINK),
    Reward("Americano", 80, RewardCategory.DRINK),
    Reward("Croissant Butter", 90, RewardCategory.FOOD),
    Reward("Cappuccino Signature", 120, RewardCategory.DRINK),
    Reward("Cheese Danish", 140, RewardCategory.FOOD),
    Reward("Latte Premium", 180, RewardCategory.DRINK),
    Reward("Tumbler Coffee Bliss", 220, RewardCategory.MERCH),
    Reward("Brunch Platter", 260, RewardCategory.FOOD),
    Reward("Exclusive Tote Bag", 320, RewardCategory.MERCH)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(
    memberId: Int,
    viewModel: CoffeeViewModel,
    onBack: () -> Unit
) {
    val member by viewModel.getMember(memberId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Reward Collection",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1B5E20))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            member?.let { m ->
                val rewardsByCategory = rewardList.groupBy { it.category }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Your Current Points",
                                    fontSize = 14.sp,
                                    color = Color(0xFF546E7A)
                                )
                                Text(
                                    text = "${m.points} pts",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                MembershipLevelChip(label = "${m.level} Member", color = levelColor(m.level))
                            }
                            MembershipTierIcon(
                                color = levelColor(m.level),
                                label = m.level
                            )
                        }
                    }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Available Rewards", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    RewardCategory.entries.forEach { category ->
                        val rewards = rewardsByCategory[category].orEmpty()
                        if (rewards.isNotEmpty()) {
                            item {
                                RewardCategoryHeader(category = category)
                            }
                            items(rewards) { reward ->
                                val canRedeem = m.points >= reward.points
                                RewardItem(
                                    reward = reward,
                                    canRedeem = canRedeem,
                                    onRedeem = {
                                        viewModel.redeemReward(memberId, reward.name, reward.points)
                                        onBack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RewardCategoryHeader(category: RewardCategory) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = null,
            tint = category.accentColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = category.title,
            fontWeight = FontWeight.SemiBold,
            color = category.accentColor
        )
    }
}

@Composable
private fun MembershipLevelChip(label: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.14f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun MembershipTierIcon(color: Color, label: String) {
    Surface(
        color = color.copy(alpha = 0.14f),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = label,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun RewardItem(reward: Reward, canRedeem: Boolean, onRedeem: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .alpha(if (canRedeem) 1f else 0.48f),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = reward.category.icon,
                contentDescription = null,
                tint = if (canRedeem) reward.category.accentColor else Color.Gray
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = reward.name, fontWeight = FontWeight.Bold)
                Text(text = "${reward.points} pts", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onRedeem,
                enabled = canRedeem,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
            ) {
                Text("Redeem", fontSize = 12.sp)
            }
        }
    }
}

private fun levelColor(level: String): Color = when (level) {
    "Gold" -> Color(0xFFF9A825)
    "Silver" -> Color(0xFF78909C)
    else -> Color(0xFF8D6E63)
}
