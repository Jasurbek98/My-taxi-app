package uz.jsoft.mytaxiapp.presentation.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.jsoft.mytaxiapp.R
import uz.jsoft.mytaxiapp.databinding.SavedLayoutBinding

/**
 * Created by Jasurbek Kurganbayev 03/04/2022
 */
object SavedSearchBottomSheet {
    fun showSavedSearchBottomSheetDialog(
        context: Context
    ) {
        val dialog = BottomSheetDialog(context)
        val binding = SavedLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.apply {

        }
        dialog.show()
    }
}