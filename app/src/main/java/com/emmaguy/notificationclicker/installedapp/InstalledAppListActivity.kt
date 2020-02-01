package com.emmaguy.notificationclicker.installedapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.lifecycle.Observer
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.graphics.Image
import androidx.ui.layout.Column
import androidx.ui.layout.LayoutHeight
import androidx.ui.layout.LayoutWidth
import androidx.ui.layout.Spacer
import androidx.ui.material.Divider
import androidx.ui.material.ListItem
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.unit.dp

class InstalledAppsActivity : AppCompatActivity() {
    private val viewModel: InstalledAppListViewModel by lazy {
        InstalledAppListViewModel(InstalledAppsRetriever(this))
    }
    private val model = InstalledAppListModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                renderInstalledAppList(model)
            }
        }

        viewModel.viewState.observe(this, Observer { state ->
            model.rows = state.packages
        })
    }
}

@Model
data class InstalledAppListModel(var rows: List<AppRow> = emptyList())

@Composable
fun renderInstalledAppList(model: InstalledAppListModel) {
    MaterialTheme {
        Column {
            TopAppBar(title = { Text("Apps") })
            VerticalScroller {
                Column {
                    Spacer(modifier = LayoutHeight(height = 8.dp))
                    model.rows.forEachIndexed { index, row ->
                        renderAppRow(
                            row = row,
                            drawDivider = index != model.rows.size - 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun renderAppRow(row: AppRow, drawDivider: Boolean) {
    Column(modifier = LayoutWidth.Fill) {
        ListItem(text = row.app.name, icon = row.app.icon.toImage(), onClick = row.onClick)
        if (drawDivider) {
            Divider(height = 1.dp, color = Color.LightGray, indent = 72.dp)
        }
    }
}

// TODO: Figure out how to convert this
private fun Drawable.toImage(): Image? {
    return Image(40.dp.value.toInt(), 40.dp.value.toInt())
}

