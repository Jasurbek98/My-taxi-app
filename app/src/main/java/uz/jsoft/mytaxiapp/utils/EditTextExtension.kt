package uz.jsoft.mytaxiapp.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import uz.jsoft.mytaxiapp.utils.`typealias`.EmptyBlock

/**
 * Created by Jasurbek Kurganbayev 09/04/2022
 */

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if ((charSequence.toString().trim().isNotEmpty() || charSequence.toString().trim()
                    .isNotBlank())
            ) {
                afterTextChanged.invoke(charSequence.toString())

            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    })
}