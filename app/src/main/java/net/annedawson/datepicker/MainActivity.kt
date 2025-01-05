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
import java.util.Locale
import java.util.TimeZone
import java.util.Calendar

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
                        if (dateState.selectedDateMillis != null) {
                            // fix the offset issue (one day off correct date)
                            selectedDate =
                                epochToLocalTimeZoneConvertor(dateState.selectedDateMillis!!)
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
            //text = "Selected Date: ${convertLongToDate(selectedDate)}",
            // NOTE - none of these convert functions worked correctly -
            // I always got the day before the date I selected - UNTIL...
            // UNTIL I used epochToLocalTimeZoneConvertor - SEE ABOVE
            text = "Selected Date: ${convertMillisToDateVersion3(selectedDate)}",
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


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault() // Set to local time zone
    }
    return formatter.format(Date(millis))
}

/*fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}*/

fun convertMillisToDateVersion2(millis: Long): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
        timeZone = TimeZone.getDefault() // Set to local time zone
    }
    val month = calendar.get(Calendar.MONTH) + 1 // Month is 0-indexed
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val year = calendar.get(Calendar.YEAR)
    return "$month/$day/$year"
}

fun convertMillisToDateVersion3(millis: Long): String {
    if (millis == 0L) return "Not selected"
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
        timeZone = TimeZone.getTimeZone("UTC") // Set to UTC
        add(
            Calendar.MILLISECOND,
            TimeZone.getDefault().getOffset(millis)
        ) // Adjust for local time zone
    }
    val month = calendar.get(Calendar.MONTH) + 1 // Month is 0-indexed
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val year = calendar.get(Calendar.YEAR)
    return "$month/$day/$year"
}

fun convertMillisToDateVersion4(millis: Long): String {
    val calendar =
        Calendar.getInstance(TimeZone.getDefault()).apply { // Use default time zone directly
            timeInMillis = millis
        }
    val month = calendar.get(Calendar.MONTH) + 1 // Month is 0-indexed
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val year = calendar.get(Calendar.YEAR)
    return "$month/$day/$year"
}

fun epochToLocalTimeZoneConvertor(epoch: Long): Long {
    val epochCalendar = Calendar.getInstance()
    epochCalendar.timeZone = TimeZone.getTimeZone("UTC")
    epochCalendar.timeInMillis = epoch
    val converterCalendar = Calendar.getInstance()
    converterCalendar.set(
        epochCalendar.get(Calendar.YEAR),
        epochCalendar.get(Calendar.MONTH),
        epochCalendar.get(Calendar.DATE),
        epochCalendar.get(Calendar.HOUR_OF_DAY),
        epochCalendar.get(Calendar.MINUTE),
    )
    converterCalendar.timeZone = TimeZone.getDefault()
    return converterCalendar.timeInMillis
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DatePickerTheme {
        Greeting("Android")
    }
}