package com.my.schedule

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.my.schedule.ui.theme.MyScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyScheduleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        HeaderWithButton()
                        NumberedList()
                    }
                }
            }
        }
    }

    @Composable
    fun HeaderWithButton() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Schedule",
                fontSize = 24.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { /* 버튼 클릭 시 동작 */ },
                modifier = Modifier
                    .padding(0.dp)
                    .size(48.dp) // 아이콘 버튼 크기
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Favorite", tint = Color.Black)
            }
        }
    }

    @Composable
    fun NumberedList() {
        val numbers = (1..100).toList() // 숫자 리스트 생성
        LazyColumn {
            items(numbers) { number ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = number.toString(),
                        fontSize = 24.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { /* 버튼 클릭 시 동작 */
                            Toast.makeText(
                                baseContext,
                                "${number.toString()} button Click",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(text = "Button")
                    }
                }
            }
        }
    }
}


