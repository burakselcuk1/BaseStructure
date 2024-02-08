package com.speakwithai.basestructure.premium

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.AnalyticsHelper
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

class PremiumFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_premium, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsHelper.logScreenView("PremiumFragment","PremiumFragment",requireContext())

        val gifImageView = view.findViewById<GifImageView>(R.id.imageView5)
        val gifImageViewTwo = view.findViewById<GifImageView>(R.id.imageView7)
        val gifImageViewThree = view.findViewById<GifImageView>(R.id.imageView8)
        val price = view.findViewById<TextView>(R.id.price)
        val gifDrawable = gifImageView.drawable as GifDrawable
        val gifDrawableTwo = gifImageViewTwo.drawable as GifDrawable
        val gifDrawableThree = gifImageViewThree.drawable as GifDrawable

        val handler = Handler()
        handler.postDelayed({
            if (gifDrawable.isRunning) {
                gifDrawable.stop()
                gifDrawableTwo.stop()
                gifDrawableThree.stop()
            }
        }, 3000)
        val handler1 = Handler()
        handler1.postDelayed({
            if (gifDrawable.isRunning) {
                price.visibility = View.VISIBLE
            }
        }, 1500)
    }
}