package com.emmaguy.notificationclicker.installedapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.state
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.graphics.asImageAsset
import androidx.ui.layout.Column
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.preferredHeight
import androidx.ui.material.*
import androidx.ui.unit.dp

class InstalledAppsActivity : AppCompatActivity() {
    private val viewModel: InstalledAppListViewModel by lazy {
        InstalledAppListViewModel(InstalledAppsRetriever(this), ActionStorage(this))
    }
    private val model = InstalledAppListModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            installedAppList(model)
        }

        viewModel.state.observe(this, Observer { state ->
            model.rows = state.packages
        })
    }
}

@Composable
fun installedAppList(model: InstalledAppListModel) {
    MaterialTheme(colors = lightThemeColors) {
        Column {
            TopAppBar(title = { Text("Apps") })
            VerticalScroller {
                Column {
                    Spacer(modifier = Modifier.preferredHeight(height = 8.dp))
                    model.rows.forEachIndexed { index, row ->
                        appRow(row, model, index)
                    }
                }
            }
        }
    }

    val editingRow = model.editingRow
    if (editingRow != null) {
        editNotificationActionsDialog(editingRow, model)
    }
}

@Composable
fun appRow(row: AppRow, model: InstalledAppListModel, index: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            text = row.app.name,
            secondaryText = row.actions.joinToString(", ").ifBlank { null },
            icon = row.app.icon.toBitmap(width = 64.dp.value.toInt(), height = 64.dp.value.toInt()).asImageAsset(),
            onClick = { model.editingRow = row }
        )
        if (index != model.rows.size - 1) {
            Divider(modifier = Modifier.preferredHeight(1.dp), color = Color.LightGray, startIndent = 72.dp)
        }
    }
}

@Composable
fun editNotificationActionsDialog(editingRow: AppRow, model: InstalledAppListModel) {
    val state = state { editingRow.actions.joinToString(separator = ", ") }
    AlertDialog(
        onCloseRequest = { model.editingRow = null },
        text = {
            val appName = editingRow.app.name
            Column {
                Text(text = "Enter notification actions you want to be automatically clicked on for $appName, e.g. 'Skip Intro'")
                TextField(
                    value = TextFieldValue(text = state.value),
                    onValueChange = { state.value = it.text },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                editingRow.actions.apply {
                    clear()
                    add(state.value)
                }
                editingRow.onActionsChanged?.invoke(editingRow.actions)
                model.editingRow = null
            }) {
                Text(text = "Confirm")
            }
        }
    )
}

@Model
data class InstalledAppListModel(
    var rows: List<AppRow> = emptyList(),
    var editingRow: AppRow? = null
)
