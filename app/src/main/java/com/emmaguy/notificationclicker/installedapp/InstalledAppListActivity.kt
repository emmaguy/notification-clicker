package com.emmaguy.notificationclicker.installedapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.state
import androidx.lifecycle.Observer
import androidx.ui.core.Text
import androidx.ui.core.TextField
import androidx.ui.core.setContent
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.LayoutHeight
import androidx.ui.layout.LayoutWidth
import androidx.ui.layout.Spacer
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

        viewModel.viewState.observe(this, Observer { state ->
            model.rows = state.packages
        })
    }
}

@Composable
fun installedAppList(model: InstalledAppListModel) {
    MaterialTheme {
        Column {
            TopAppBar(title = { Text("Apps") })
            VerticalScroller {
                Column {
                    Spacer(modifier = LayoutHeight(height = 8.dp))
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
    Column(modifier = LayoutWidth.Fill) {
        ListItem(
            text = row.app.name,
            secondaryText = row.actions.joinToString(", ").ifBlank { null },
            icon = AndroidImage(
                bitmap = row.app.icon.drawableToBitmap(
                    width = 64.dp.value.toInt(),
                    height = 64.dp.value.toInt(),
                    name = row.app.name
                )
            ),
            onClick = { model.editingRow = row }
        )
        if (index != model.rows.size - 1) {
            Divider(height = 1.dp, color = Color.LightGray, indent = 72.dp)
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
                    value = state.value,
                    onValueChange = { state.value = it })
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
