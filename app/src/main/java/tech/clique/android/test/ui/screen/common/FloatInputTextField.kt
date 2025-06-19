package tech.clique.android.test.ui.screen.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import tech.clique.android.test.ui.toFullString

@Composable
fun FloatInputTextField(
    sourceFloat: Float,
    currentFloat: MutableFloatState,
    labelText: String? = null,
    placeholderText: String? = null
) {
    var inputValue by remember(sourceFloat) { mutableStateOf(sourceFloat.toFullString()) }
    val floatRegex = remember { Regex("^\\d*\\.?\\d*$") }
    val keyboardController = LocalSoftwareKeyboardController.current


    fun onInputChange(newText: String) {
        if (newText.isEmpty()) {
            inputValue = "0"
            currentFloat.floatValue = 0f
            return
        }

        if (floatRegex.matches(newText)) {
            val float = newText.toFloat()
            currentFloat.floatValue = float
            inputValue = newText
        }
    }
    OutlinedTextField(
        value = inputValue,
        onValueChange = ::onInputChange,
        modifier = Modifier
            .fillMaxWidth()
//            .focusRequester(focusRequester)
            .onFocusChanged {
                inputValue = inputValue.toFloat().toFullString()
            },

        label = labelText?.run { { Text(this) } },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
        singleLine = true,
        placeholder = placeholderText?.run { { Text(this) } },
    )
}