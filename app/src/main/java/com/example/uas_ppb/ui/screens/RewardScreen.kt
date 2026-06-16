package com.example.uas_ppb.ui.screens

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
    val icon: ImageVector
) {
    DRINK("Minuman", Icons.Default.Coffee),
    FOOD("Makanan", Icons.Default.Fastfood),
    MERCH("Merch", Icons.Default.WorkspacePremium)
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
                    Text(
                        text = "Reward Collection",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
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
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
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
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${m.points} pts",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            MembershipLevelChip(
                                label = "${m.level} Member", 
                                color = getLevelColor(m.level)
                            )
                        }
                        MembershipTierIcon(
                            color = getLevelColor(m.level),
                            label = m.level
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Available Rewards", 
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
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
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = category.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun MembershipLevelChip(label: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun MembershipTierIcon(color: Color, label: String) {
    Surface(
        color = color.copy(alpha = 0.2f),
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
                style = MaterialTheme.typography.labelSmall,
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
            .alpha(if (canRedeem) 1f else 0.6f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = reward.category.icon,
                contentDescription = null,
                tint = if (canRedeem) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = reward.name, 
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${reward.points} pts", 
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onRedeem,
                enabled = canRedeem,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Redeem", fontSize = 12.sp)
            }
        }
    }
}

private fun getLevelColor(level: String): Color = when (level) {
    "Gold" -> Color(0xFFFFD54F)
    "Silver" -> Color(0xFFB0BEC5)
    else -> Color(0xFFD7B899)
}
