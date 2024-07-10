package com.my.schedule.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.my.schedule.R
import com.my.schedule.ui.preference.MainActivityPrefer
import com.my.schedule.ui.theme.MyScheduleTheme
import com.my.schedule.ui.viewmodel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedActivity : ComponentActivity() {

    private val todoViewModel : TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyScheduleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(MainActivityPrefer.HEADER_WEIGHT)
                                    .padding(MainActivityPrefer.HEADER_PADDING.dp)
                            ) {
                                Header()
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(MainActivityPrefer.LIST_WEIGHT)
                                    .padding(MainActivityPrefer.LIST_PADDING.dp)
                            ) {
                                CompletedTodoList()
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(MainActivityPrefer.BOTTOM_WEIGHT)
                                    .padding(MainActivityPrefer.BOTTOM_PADDING.dp)
                            ) {

                            }
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun Header() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.completed_todo),
                fontSize = 24.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }

    @Composable
    fun CompletedTodoList() {
        val todos by todoViewModel.completedTodos.observeAsState(emptyList())

        LazyColumn(modifier = Modifier.wrapContentSize())
        {

            items(todos) { todo ->


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = todo.todo,
                        fontSize = 24.sp,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {

                        },
                        modifier = Modifier
                            .padding(0.dp)
                            .size(48.dp) // 아이콘 버튼 크기
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Check", tint = Color.Black)
                    }
                }
            }
        }
    }
}
