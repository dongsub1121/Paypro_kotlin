package kr.nicepayment.paypro.ui

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import kr.nicepayment.paypro.R

abstract class AbstractContentFragment  : Fragment() {

        protected lateinit var titleTextView: TextView
        protected lateinit var descriptionTextView: TextView
        protected lateinit var mCardView: CardView
        protected lateinit var mLayout: LinearLayout

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_abstract_content, container, false)

        // Initialize views
        titleTextView = view.findViewById(R.id.text_title)
        descriptionTextView = view.findViewById(R.id.text_description)
        mCardView = view.findViewById(R.id.cardView_parent)
            mLayout = view.findViewById(R.id.linearLayoutView_parent)

        // Setup fragment
        setupFragment()

        return view
    }

    /**
     * Abstract method to setup the fragment. This must be implemented by child classes.
     */
    protected abstract fun setupFragment()

    protected fun setCardViewHeightInDp(heightInDp: Float) {
        val layoutParams = mCardView.layoutParams
        layoutParams.height = convertDpToPixel(heightInDp, requireContext())
        mCardView.layoutParams = layoutParams
    }

    private fun convertDpToPixel(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}