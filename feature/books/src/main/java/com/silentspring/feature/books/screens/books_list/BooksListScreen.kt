package com.silentspring.feature.books.screens.books_list

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silentspring.core.common.extension.checkStoragePermissions
import com.silentspring.feature.books.screens.books_list.components.BooksList


@Composable
fun BooksListScreen() {

    val viewModel = hiltViewModel<BooksListViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value

    val context = LocalContext.current

    val multiplePermissionsResult = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        permissions.entries.forEach {
            Log.d("DEBUG", "${it.key} = ${it.value}")
        }
        val isReadPermissionGranted =
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] as Boolean
        val isWritePermissionGranted =
            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] as Boolean

        if (isReadPermissionGranted && isWritePermissionGranted) {
            viewModel.getFolders()
        } else {
            Toast.makeText(
                context,
                "Storage Permissions Denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val activityResult =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    viewModel.getFolders()
                } else {
                    Toast.makeText(
                        context,
                        "Storage Permissions Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    LaunchedEffect(Unit) {
        if (!context.checkStoragePermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val intent = Intent().apply {
                        action = ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    activityResult.launch(intent)
                } catch (e: Exception) {
                    val intent = Intent().apply {
                        action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    }
                    activityResult.launch(intent)
                }
            } else {
                multiplePermissionsResult.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        } else {
            viewModel.getFolders()
        }
    }

    BooksList(state = state, onStateChanged = viewModel::onStateChanged)
}