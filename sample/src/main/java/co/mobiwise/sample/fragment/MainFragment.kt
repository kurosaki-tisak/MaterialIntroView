package co.mobiwise.sample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import co.mobiwise.materialintro.MaterialIntroView

import co.mobiwise.materialintro.prefs.PreferencesManager
import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.materialintro.shape.ShapeType
import co.mobiwise.sample.R

/**
 * Created by mertsimsek on 31/01/16.
 */
class MainFragment : Fragment(), View.OnClickListener {

    private var cardView: CardView? = null
    private var button: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_main, container, false)
        cardView = view.findViewById<View>(R.id.my_card) as CardView
        button = view.findViewById<View>(R.id.button_reset_all) as Button
        button!!.setOnClickListener(this)

        //Show intro
        showIntro(cardView, INTRO_CARD, "This is card! Hello There. You can set this text!")

        return view
    }

    override fun onClick(v: View) {
        val id = v.id

        if (id == R.id.button_reset_all)
            PreferencesManager(activity!!.applicationContext).resetAll()
    }

    private fun showIntro(view: View?, usageId: String, text: String) {
        MaterialIntroView.build(activity as AppCompatActivity, MaterialIntroView.MaterialIntroViewConfig(
                focusGravity = FocusGravity.CENTER,
                focusType = Focus.MINIMUM,
                delayMillis = 200,
                isFadeAnimationEnabled = true,
                isPerformClick = true,
                textTitle = text,
                textInfo = text,
                targetView = view,
                shapeType = ShapeType.RECTANGLE,
                introViewId = usageId //THIS SHOULD BE UNIQUE ID
        ))
    }

    companion object {

        private val INTRO_CARD = "material_intro"
    }
}
