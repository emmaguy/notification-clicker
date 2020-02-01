package com.emmaguy.notificationclicker.installedapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.lifecycle.Observer
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp

class InstalledAppsActivity : AppCompatActivity() {
    private val viewModel: InstalledAppListViewModel by lazy {
        InstalledAppListViewModel(InstalledAppsRetriever(this))
    }
    private val model = InstalledAppListModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewState.observe(this, Observer { state ->
            model.apps.apply {
                clear()
                addAll(state.packages)

                // TODO: Compose should update without doing this again 🤔
                setContent {
                    MaterialTheme {
                        renderInstalledAppList(model)
                    }
                }
            }
        })

        setContent {
            MaterialTheme {
                renderInstalledAppList(model)
            }
        }
    }
}

@Model
data class InstalledAppListModel(val apps: ArrayList<App> = arrayListOf())

@Composable
fun renderInstalledAppList(model: InstalledAppListModel) {
    MaterialTheme {
        Column {
            TopAppBar(title = { Text("Apps") })
            VerticalScroller {
                Column {
                    Spacer(modifier = LayoutHeight(height = 8.dp))
                    model.apps.forEachIndexed { index, app ->
                        renderApp(app, drawDivider = index != model.apps.size - 1)
                    }
                }
            }
        }
    }
}

@Composable
fun renderApp(
    app: App,
    drawDivider: Boolean
) {
    // TODO: Less padding more centering?
    Column(modifier = LayoutWidth.Fill) {
        Spacer(modifier = LayoutHeight(height = 8.dp))
        Row {
            Spacer(modifier = LayoutWidth(16.dp))
            // TODO: Draw actual Drawable
            Surface(color = Color.Green) {
                Container(width = 40.dp, height = 40.dp) {}
            }
            Spacer(modifier = LayoutWidth(16.dp))
            Column {
                Spacer(modifier = LayoutHeight(height = 8.dp))
                Text(text = app.name)
            }
        }
        Spacer(modifier = LayoutHeight(height = 8.dp))
        if (drawDivider) {
            Divider(height = 1.dp, color = Color.LightGray, indent = 72.dp)
        }
    }
}

