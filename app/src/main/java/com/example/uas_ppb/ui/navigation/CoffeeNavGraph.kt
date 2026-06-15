package com.example.uas_ppb.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uas_ppb.ui.screens.AddMemberScreen
import com.example.uas_ppb.ui.screens.HomeScreen
import com.example.uas_ppb.ui.screens.MemberDetailScreen
import com.example.uas_ppb.ui.screens.RewardScreen
import com.example.uas_ppb.ui.screens.SplashScreen
import com.example.uas_ppb.ui.screens.TransactionScreen
import com.example.uas_ppb.ui.viewmodel.CoffeeViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object AddMember : Screen("add_member")
    object MemberDetail : Screen("member_detail/{memberId}") {
        fun createRoute(memberId: Int) = "member_detail/$memberId"
    }
    object Transaction : Screen("transaction/{memberId}") {
        fun createRoute(memberId: Int) = "transaction/$memberId"
    }
    object Reward : Screen("reward/{memberId}") {
        fun createRoute(memberId: Int) = "reward/$memberId"
    }
}

@Composable
fun CoffeeNavGraph(
    navController: NavHostController,
    viewModel: CoffeeViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddMemberClick = { navController.navigate(Screen.AddMember.route) },
                onMemberClick = { memberId -> 
                    navController.navigate(Screen.MemberDetail.createRoute(memberId))
                }
            )
        }
        composable(Screen.AddMember.route) {
            AddMemberScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.MemberDetail.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            MemberDetailScreen(
                memberId = memberId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onAddTransaction = { navController.navigate(Screen.Transaction.createRoute(memberId)) },
                onRedeemReward = { navController.navigate(Screen.Reward.createRoute(memberId)) }
            )
        }
        composable(
            route = Screen.Transaction.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            TransactionScreen(
                memberId = memberId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Reward.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            RewardScreen(
                memberId = memberId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
