package co.mobiwise.sample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.mobiwise.materialintro.MaterialIntroView

import co.mobiwise.materialintro.MaterialIntroListener
import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.sample.R

/**
 * Created by mertsimsek on 31/01/16.
 */
class GravityFragment : Fragment(), MaterialIntroListener {

    lateinit var cardView1: CardView
    lateinit var cardView2: CardView
    lateinit var cardView3: CardView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gravity, container, false)

        cardView1 = view.findViewById<View>(R.id.my_card) as CardView
        cardView2 = view.findViewById<View>(R.id.my_card2) as CardView
        cardView3 = view.findViewById<View>(R.id.my_card3) as CardView

        showIntro(cardView1, INTRO_CARD1, "This intro focuses on RIGHT", FocusGravity.RIGHT)

        return view
    }

    override fun onUserClicked(materialIntroViewId: String) {
        if (materialIntroViewId === INTRO_CARD1)
            showIntro(cardView2, INTRO_CARD2, "This intro focuses on CENTER.", FocusGravity.CENTER)
        if (materialIntroViewId === INTRO_CARD2)
            showIntro(cardView3, INTRO_CARD3, "This intro focuses on LEFT.", FocusGravity.LEFT)
    }

    fun showIntro(view: View, id: String, text: String, focusGravity: FocusGravity) {
        MaterialIntroView.build(activity as AppCompatActivity, MaterialIntroView.MaterialIntroViewConfig(
                focusGravity = focusGravity,
                focusType = Focus.MINIMUM,
                delayMillis = 200,
                isFadeAnimationEnabled = true,
                isPerformClick = true,
                textTitle = text,
                textInfo = text,
                targetView = view,
                materialIntroListener = this,
                introViewId = id //THIS SHOULD BE UNIQUE ID
        ))
    }

    companion object {

        private val INTRO_CARD1 = "intro_card_1"
        private val INTRO_CARD2 = "intro_card_2"
        private val INTRO_CARD3 = "intro_card_3"
    }
}
