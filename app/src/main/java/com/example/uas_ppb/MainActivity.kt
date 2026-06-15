package com.example.uas_ppb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.uas_ppb.ui.navigation.CoffeeNavGraph
import com.example.uas_ppb.ui.theme.UAS_PPBTheme
import com.example.uas_ppb.ui.viewmodel.CoffeeViewModel
import com.example.uas_ppb.ui.viewmodel.CoffeeViewModelFactory

class MainActivity : ComponentActivity() {
    
    private val viewModel: CoffeeViewModel by viewModels {
        CoffeeViewModelFactory((application as CoffeeBlissApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UAS_PPBTheme {
                val navController = rememberNavController()
                // Kita tidak butuh Scaffold luar karena tiap Screen sudah punya Scaffold sendiri
                CoffeeNavGraph(
                    navController = navController,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
