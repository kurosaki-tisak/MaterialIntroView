package co.mobiwise.sample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import co.mobiwise.materialintro.MaterialIntroView
import co.mobiwise.materialintro.MaterialIntroListener
import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.sample.R

/**
 * Created by mertsimsek on 31/01/16.
 */
class FocusFragment : Fragment(), MaterialIntroListener {

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_focus, container, false)

        button1 = view.findViewById<View>(R.id.button_focus_1) as Button
        button2 = view.findViewById<View>(R.id.button_focus_2) as Button
        button3 = view.findViewById<View>(R.id.button_focus_3) as Button

        showIntro(button1, INTRO_FOCUS_1, "This intro view focus on all target.", Focus.ALL)

        return view
    }

    fun showIntro(view: View, id: String, text: String, focusType: Focus) {

        MaterialIntroView.build(activity as AppCompatActivity, MaterialIntroView.MaterialIntroViewConfig(
                introViewId = id,
                targetView = view,
                focusGravity = FocusGravity.CENTER,
                delayMillis = 200,
                materialIntroListener = this,
                textTitle = text,
                textInfo = text,
                focusType = focusType,
                isFadeAnimationEnabled = true,
                isPerformClick = true
        ))
    }

    override fun onUserClicked(viewId: String) {
        if (viewId === INTRO_FOCUS_1)
            showIntro(button2, INTRO_FOCUS_2, "This intro view focus on minimum size", Focus.MINIMUM)
        else if (viewId === INTRO_FOCUS_2)
            showIntro(button3, INTRO_FOCUS_3, "This intro view focus on normal size (avarage of MIN and ALL)", Focus.NORMAL)
    }

    companion object {

        private val INTRO_FOCUS_1 = "intro_focus_1"
        private val INTRO_FOCUS_2 = "intro_focus_2"
        private val INTRO_FOCUS_3 = "intro_focus_3"
    }
}
