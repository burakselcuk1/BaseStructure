package com.example.basestructure.ui.PremiumRequiredDialogFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.basestructure.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PremiumRequiredDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val coordinatorLayout = container?.parent as? CoordinatorLayout
        coordinatorLayout?.setBackgroundResource(R.drawable.rounded_corner_left)
        return inflater.inflate(R.layout.dialog_premium_required, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dialog içindeki görünümlere erişim ve event handling (isterseniz)

    }

    override fun onStart() {
        super.onStart()
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
