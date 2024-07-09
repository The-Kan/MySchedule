package com.my.schedule.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.my.schedule.R
import com.my.schedule.ui.api.Repository
import com.my.schedule.ui.data.todo.Todo
import com.my.schedule.ui.data.todo.TodoDatabase
import com.my.schedule.ui.data.todo.TodoRepository
import com.my.schedule.ui.utils.DateTime.Companion.DATE_FORMATTER
import com.my.schedule.ui.utils.DateTime.Companion.TIME_FORMATTER
import com.my.schedule.ui.notification.TodoNotificationWorker
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.BOTTOM_PADDING
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.BOTTOM_SHEET_HEIGHT
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.BOTTOM_WEIGHT
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.HEADER_PADDING
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.HEADER_WEIGHT
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.LIST_PADDING
import com.my.schedule.ui.preference.MainActivityPrefer.Companion.LIST_WEIGHT
import com.my.schedule.ui.theme.MyScheduleTheme
import com.my.schedule.ui.utils.TAG
import com.my.schedule.ui.utils.debug
import com.my.schedule.ui.utils.info
import com.my.schedule.ui.viewmodel.CounterViewModel
import com.my.schedule.ui.viewmodel.TodoViewModel
import com.my.schedule.ui.viewmodel.TodoViewModelFactory
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var counterViewModel: CounterViewModel
    private lateinit var apiRepository: Repository
    private var disposableIncrement: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        debug(TAG, "On Create")

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
        apiRepository = Repository()
        counterViewModel = CounterViewModel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }
    }

    private fun initRoom() {
        // Room 데이터 베이스 초기화
        val database = TodoDatabase.getDatabase(this)
        val repository = TodoRepository(database.todoDao())
        val viewModelFactory = TodoViewModelFactory(repository)
        todoViewModel = ViewModelProvider(this, viewModelFactory).get(TodoViewModel::class.java)

    }

    @OptIn(ExperimentalMaterialApi::class, DelicateCoroutinesApi::class)
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
            info(TAG, sheetState.targetValue.toString())
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

                    var todo = Todo()

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
                                        todo.todo = inputText.text
                                        todo.notificationWorkId = reserveNotification(
                                            todo = todo.todo,
                                            date = todo.date,
                                            time = todo.time
                                        )
                                        todoViewModel.insert(todo)
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
                            .weight(1f)
                            .padding(10.dp)
                    ) {
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            label = { Text(stringResource(R.string.todo)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(6f)
                            .padding(10.dp)
                    ) {
                        DateTimePickerScreen(todo)
                    }

                }
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight()
        ) {

        }


    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
    }

    private fun reserveNotification(todo: String, date: String, time: String): String {
        val localDate = LocalDate.parse(date, DATE_FORMATTER)
        val localTime = LocalTime.parse(time, TIME_FORMATTER)
        val notificationTime = LocalDateTime.of(localDate, localTime)
        val delay = notificationTime.atZone(ZoneId.systemDefault())
            .toEpochSecond() - System.currentTimeMillis() / 1000
        val workRequest = OneTimeWorkRequestBuilder<TodoNotificationWorker>()
            .setInputData(Data.Builder().putString("todo", todo).build())
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        return workRequest.id.toString()
    }

    private fun cancelNotification(workRequestIdString: String) {
        val uuid = UUID.fromString(workRequestIdString)
        WorkManager.getInstance(applicationContext).cancelWorkById(uuid)
    }

    @Composable
    fun DateTimePickerScreen(todo: Todo) {
        var selectedDate by remember { mutableStateOf(LocalDate.now().plusDays(1)) }
        var selectedTime by remember { mutableStateOf(LocalTime.now()) }

        todo.date = selectedDate.format(DATE_FORMATTER)
        todo.time = selectedTime.format(TIME_FORMATTER)

        val calendar = Calendar.getInstance()
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(id = R.string.date)} : ${
                        selectedDate.format(
                            DATE_FORMATTER
                        )
                    }"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                            todo.date = selectedDate.format(DATE_FORMATTER)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Text(text = stringResource(id = R.string.date_setting))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "${stringResource(id = R.string.time)} : ${
                        selectedTime.format(
                            TIME_FORMATTER
                        )
                    }"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            selectedTime = LocalTime.of(hourOfDay, minute)
                            todo.date = selectedTime.format(TIME_FORMATTER)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false
                    ).show()
                }) {
                    Text(text = stringResource(id = R.string.time_setting))
                }
            }


        }
    }


    @Composable
    fun HeaderWithButton() {
        val coroutineScope = rememberCoroutineScope()
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
                            coroutineScope.launch(Dispatchers.Default) {
                                apiRepository.post()
                            }
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("delete Test") },
                        onClick = {
                            coroutineScope.launch {
                                todoViewModel.deleteAll()
                                delay(500)
                            }

                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.completed_todo)) },
                        onClick = {
                            goToCompletedActivity()
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


    private fun goToCompletedActivity() {
        val intent = Intent(this, CompletedActivity::class.java)
        startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
        disposableIncrement?.dispose()
    }

    @Composable
    fun NumberedList() {
        val todos by todoViewModel.items.observeAsState(emptyList())

        LazyColumn(modifier = Modifier.wrapContentSize())
        {

            items(todos) { todo ->

                if (todo.completed) {
                    return@items
                }

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
                        onClick = {
                            // When updating, Room DB notices the change and updates LiveData<List<Todo>>.
                            // However, LiveData does not notice changes in "the field of List element objects".
                            // There appears to be a problem with checking the equality of "the field of List element objects", which can be resolved by copying todo object.
                            todoViewModel.update(todo.copy(completed = true))
                            cancelNotification(todo.notificationWorkId)
                        },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.completed))
                    }
                }
            }
        }
    }
}


