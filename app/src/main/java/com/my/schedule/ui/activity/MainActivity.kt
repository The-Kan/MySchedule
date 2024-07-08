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
import androidx.compose.material.TextField
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.my.schedule.R
import com.my.schedule.ui.data.todo.Todo
import com.my.schedule.ui.data.todo.TodoDatabase
import com.my.schedule.ui.data.todo.TodoRepository
import com.my.schedule.ui.http.retrofit.RetrofitClient
import com.my.schedule.ui.log.LogManager
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.BOTTOM_PADDING
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.BOTTOM_SHEET_HEIGHT
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.BOTTOM_WEIGHT
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.HEADER_PADDING
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.HEADER_WEIGHT
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.LIST_PADDING
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.LIST_WEIGHT
import com.my.schedule.ui.theme.MyScheduleTheme
import com.my.schedule.ui.viewmodel.CounterViewModel
import com.my.schedule.ui.viewmodel.TodoViewModel
import com.my.schedule.ui.viewmodel.TodoViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private val tag = LogManager.getPrefix("MainActivity")

class MainActivity : ComponentActivity() {
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var counterViewModel: CounterViewModel
    private var disposableIncrement: Disposable? = null
    private var disposableRetrofit: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // PR 테스트 2
        init()

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
                                    .padding(HEADER_PADDING.dp)
                            ) {
                                HeaderWithButton()
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(LIST_WEIGHT)
                                    .padding(LIST_PADDING.dp)
                            ) {
                                NumberedList()
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(BOTTOM_WEIGHT)
                                    .padding(BOTTOM_PADDING.dp)
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

    private fun init() {
        initRoom()
        counterViewModel = CounterViewModel()
    }

    private fun initRoom() {
        // Room 데이터 베이스 초기화
        val database = TodoDatabase.getDatabase(this)
        val repository = TodoRepository(database.todoDao())
        val viewModelFactory = TodoViewModelFactory(repository)
        todoViewModel = ViewModelProvider(this, viewModelFactory).get(TodoViewModel::class.java)

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
            Log.i(tag, sheetState.targetValue.toString())
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

                    var inputText by remember { mutableStateOf(TextFieldValue("")) }
                    val context = LocalContext.current

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Button(
                                onClick = { onButtonClick() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    stringResource(id = R.string.close), color = Color.Blue,
                                    fontWeight = FontWeight.Bold, fontSize = 15.sp
                                )
                            }
                            Text(
                                stringResource(id = R.string.add_todo),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Button(
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "Entered text: ${inputText.text}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    GlobalScope.launch {
                                        todoViewModel.insert(Todo(todo = inputText.text))
                                    }
                                    onButtonClick()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    stringResource(id = R.string.add), color = Color.Blue,
                                    fontWeight = FontWeight.Bold, fontSize = 15.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(8f)
                            .padding(10.dp)
                    ) {
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            label = { Text("Enter text") },
                            modifier = Modifier.fillMaxWidth()
                        )
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
                        text = { Text("Retrofit Test") },
                        onClick = {
                            fetchPosts()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("delete Test") },
                        onClick = {
                            GlobalScope.launch {
                                todoViewModel.deleteAll()
                                delay(500)
                            }

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
            horizontalArrangement = Arrangement.SpaceBetween,
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

            Row(verticalAlignment = Alignment.CenterVertically) {

                Button(modifier = Modifier
                    .wrapContentSize()
                    .padding(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { disposableIncrement = counterViewModel.incrementCount() }) {
                    Text(
                        fontSize = 14.sp,
                        color = Color.Black,
                        text = "Timer Start"
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 14.sp,
                    color = Color.Black,
                    text = "${counterViewModel.count}"
                )
            }
        }
    }

    fun fetchPosts() {
        val apiService = RetrofitClient.instance

        disposableRetrofit = apiService.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                posts ->
                for(post in posts){
                    Log.i(tag, "Title : ${post.title}")
                }
            }, {
                error ->
                    Log.i(tag, "${error.message}")
            })

    }

    override fun onDestroy() {
        super.onDestroy()
        disposableIncrement?.dispose()
        disposableRetrofit?.dispose()
    }

    @Composable
    fun NumberedList() {
        val todos by todoViewModel.items.observeAsState(emptyList())

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
                    Button(
                        onClick = { /* 버튼 클릭 시 동작 */
                            todoViewModel.delete(todo)
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


