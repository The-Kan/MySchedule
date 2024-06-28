package com.my.schedule

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

                    var showModalBottomSheet by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        HeaderWithButton(onButtonClick = { showModalBottomSheet = true })
                        //NumberedList()
                        BottomSheet(showModalBottomSheet) { showModalBottomSheet = false }
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    private fun BottomSheet(showModalBottomSheet: Boolean, onButtonClick: () -> Unit) {
        val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)

        if(showModalBottomSheet){
            ModalBottomSheetLayout(
                sheetContent = {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentSize()
                    ) {
                        Text("스케줄 등록", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { onButtonClick() }) {
                            Text("닫기")
                        }
                    }
                },
                sheetState = sheetState,
                modifier = Modifier.wrapContentSize()
            ) {
                // Optional content behind the bottom sheet, if any
            }
        }

    }


    @Composable
    fun HeaderWithButton(onButtonClick: () -> Unit) {
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

            var expanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = { expanded = true
                        },
                    modifier = Modifier
                        .padding(0.dp)
                        .size(48.dp) // 아이콘 버튼 크기
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Favorite", tint = Color.Black)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("스케줄 등록") },
                        onClick = {
                            onButtonClick()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("환경 설정") },
                        onClick = { expanded = false }
                    )
                }
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


