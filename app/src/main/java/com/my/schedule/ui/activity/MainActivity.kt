package com.my.schedule.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.my.schedule.R
import com.my.schedule.ui.log.getTAG
import com.my.schedule.ui.preference.MainActivityRatio.Companion.BOTTOM_SHEET_HEIGHT
import com.my.schedule.ui.preference.MainActivityRatio.Companion.BOTTOM_WEIGHT
import com.my.schedule.ui.preference.MainActivityRatio.Companion.HEADER_WEIGHT
import com.my.schedule.ui.preference.MainActivityRatio.Companion.LIST_WEIGHT
import com.my.schedule.ui.theme.MyScheduleTheme


class MainActivity : ComponentActivity() {
    private val TAG = getTAG("MainActivity")

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

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(HEADER_WEIGHT)
                                    .padding(10.dp)
                            ) {
                                HeaderWithButton()
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(LIST_WEIGHT)
                                    .padding(10.dp)
                            ) {
                                NumberedList()
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(BOTTOM_WEIGHT)
                                    .padding(10.dp)
                            ) {
                                BottomWithButton(onButtonClick = { showModalBottomSheet = true })
                            }
                        }
                        BottomSheet(showModalBottomSheet) { showModalBottomSheet = false }
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BottomSheet(showModalBottomSheet: Boolean, onButtonClick: () -> Unit) {
        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )

        LaunchedEffect(showModalBottomSheet) {
            if (showModalBottomSheet) {
                sheetState.show()
            } else {
                sheetState.hide()
            }
        }

        LaunchedEffect(sheetState.targetValue) {
            Log.i(TAG, sheetState.targetValue.toString())
            if (sheetState.targetValue == ModalBottomSheetValue.Hidden) {
                onButtonClick()
            }
        }

        ModalBottomSheetLayout(
            sheetContent = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxHeight(BOTTOM_SHEET_HEIGHT)
                ) {
                    Text(
                        stringResource(id = R.string.add_todo),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = { onButtonClick() }) {
                        Text(stringResource(id = R.string.close))
                    }
                }
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight()
        ) {

        }


    }

    @Composable
    fun HeaderWithButton() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 24.sp,
                modifier = Modifier.weight(1f)
            )

            var expanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = {
                        expanded = true
                    },
                    modifier = Modifier
                        .padding(0.dp)
                        .size(48.dp) // 아이콘 버튼 크기
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "MoreVert", tint = Color.Black)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Temp") },
                        onClick = {
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.settings)) },
                        onClick = { expanded = false }
                    )
                }
            }
        }
    }


    @Composable
    fun BottomWithButton(onButtonClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    onButtonClick()
                },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
                // 아이콘 버튼 크기
            ) {
                Row(
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.Black)
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(R.string.add_todo),
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    @Composable
    fun NumberedList() {
        val numbers = (1..20).toList() // 숫자 리스트 생성
        LazyColumn(modifier = Modifier.wrapContentSize())
        {

            items(numbers) { number ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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


