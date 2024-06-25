package com.my.schedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.schedule.ui.theme.MyScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyScheduleTheme {
                // A surface container using the 'background' color from the theme
                Column (modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Surface(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
                        Greeting("Android")
                    }

                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        color = Color.LightGray
                    ) {
                        Button(
                            onClick = { /* 첫 번째 Surface의 버튼 클릭 시 동작 */ },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = "Button in Surface 1")
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        color = Color.Black
                    ) {
                        Button(
                            onClick = { /* 두 번째 Surface의 버튼 클릭 시 동작 */ },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = "Button in Surface 2")
                        }
                    }
                }


            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyScheduleTheme {
        Greeting("Android")
    }
}