package com.example.avo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialDialogs

class BottomSheet: BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.Theme_Avo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        const val TAG = "AddRecipientWalletSheet"
    }
}