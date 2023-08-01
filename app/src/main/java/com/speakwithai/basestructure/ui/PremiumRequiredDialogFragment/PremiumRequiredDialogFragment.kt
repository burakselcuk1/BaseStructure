package com.speakwithai.basestructure.ui.PremiumRequiredDialogFragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.speakwithai.basestructure.R

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

        val bottomSheet = (requireView().parent as View)
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)


        view.findViewById<View>(R.id.premium_cancel_button).setOnClickListener {
            dismiss() // dialogu kapatır
        }

        val constraintLayout1 = view.findViewById<ConstraintLayout>(R.id.constraintLayout3)
        val constraintLayout2 = view.findViewById<ConstraintLayout>(R.id.bozo)
        val textView1InLayout1 = view.findViewById<TextView>(R.id.textView9)
        val textView2InLayout2 = view.findViewById<TextView>(R.id.textview10)
        val textView1InLayout3 = view.findViewById<TextView>(R.id.textView91)
        val textView2InLayout4 = view.findViewById<TextView>(R.id.asdf)
        val continuee = view.findViewById<TextView>(R.id.continuee)



        constraintLayout1.setOnClickListener {
            it.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_green)
            constraintLayout2.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border) // default renk
            textView2InLayout2.setTextColor(Color.WHITE)
            textView1InLayout1.setTextColor(Color.WHITE)
            textView1InLayout3.setTextColor(Color.BLACK)
            textView2InLayout4.setTextColor(Color.BLACK)

        }

        constraintLayout2.setOnClickListener {
            it.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_green)
            constraintLayout1.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border) // default renk
            textView1InLayout3.setTextColor(Color.WHITE)
            textView2InLayout4.setTextColor(Color.WHITE)
            textView2InLayout2.setTextColor(Color.BLACK)
            textView1InLayout1.setTextColor(Color.BLACK)

        }

        continuee.setOnClickListener {
            findNavController().navigate(R.id.action_premiumRequiredDialogFragment_to_signInFragment)
        }

    }

    override fun onStart() {
        super.onStart()
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
