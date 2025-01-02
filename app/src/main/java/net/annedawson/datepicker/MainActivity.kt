package net.annedawson.datepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.annedawson.datepicker.ui.theme.DatePickerTheme
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DatePickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    var dateDialogController by remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState()
    var selectedDate by remember { mutableLongStateOf(0L) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { dateDialogController = true }) {
            Text(text = "Pick Date")
        }

        if (dateDialogController) {
            DatePickerDialog(
                onDismissRequest = { dateDialogController = false },
                confirmButton = {
                    TextButton(onClick = {
                        if(dateState.selectedDateMillis != null){
                            selectedDate = dateState.selectedDateMillis?.plus(86400000)!!
                            // add the number of milliseconds in a day to the selected date
                            // to correct the date being one day off
                        }
                        dateDialogController = false
                    }) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        dateDialogController = false
                    }) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(state = dateState)
            }
        }


        Text(
            text = "Selected Date: ${convertLongToDate(selectedDate)}",
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 24.sp
        )

    }

}

fun convertLongToDate(time: Long): String {
    if (time == 0L) return "Not selected"
    val date = Date(time)
    val format = SimpleDateFormat.getDateInstance()
    return format.format(date)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DatePickerTheme {
        Greeting("Android")
    }
}