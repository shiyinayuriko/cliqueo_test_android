package tech.clique.android.test.ui.screen.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

sealed class ViewStatus() {
    data object StatusIdle : ViewStatus()
    data object StatusComplete : ViewStatus()
    data class StatusLoading(
        val step: Int = 0
    ) : ViewStatus() {}

    data class StatusError(val errorMsg: String) : ViewStatus() {}
}

@Composable
fun ViewStatusContainer(
    status: ViewStatus,
    content: @Composable () -> Unit,
) {
    when (status) {
        is ViewStatus.StatusLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp,
                )
            }
        }
        ViewStatus.StatusIdle,
        ViewStatus.StatusComplete -> {
            content()
        }

        is ViewStatus.StatusError -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    text = status.errorMsg,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}