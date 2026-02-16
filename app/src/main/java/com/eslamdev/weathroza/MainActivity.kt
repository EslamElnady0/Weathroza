package com.eslamdev.weathroza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.eslamdev.weathroza.presentaion.routes.App
import com.eslamdev.weathroza.ui.theme.WeathrozaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeathrozaTheme(darkTheme = true) {
                App()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeathrozaTheme {
        //  Greeting("Android")
    }
}