package co.mobiwise.sample.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.mobiwise.materialintro.MaterialIntroView

import java.util.ArrayList

import co.mobiwise.materialintro.MaterialIntroListener
import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.sample.R
import co.mobiwise.sample.adapter.RecyclerViewAdapter
import co.mobiwise.sample.model.Song

class RecyclerviewFragment : Fragment(), MaterialIntroListener {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_recyclerview, container, false)
        recyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView

        initializeRecyclerview()
        loadData()

        Handler().postDelayed({ showMaterialIntro() }, 2000)
        return view
    }

    private fun showMaterialIntro() {

        MaterialIntroView.build(activity as AppCompatActivity, MaterialIntroView.MaterialIntroViewConfig(
                focusGravity = FocusGravity.CENTER,
                focusType = Focus.MINIMUM,
                delayMillis = 200,
                isFadeAnimationEnabled = true,
                materialIntroListener = this,
                isPerformClick = true,
                textTitle = "",
                textInfo = "This intro focuses on Recyclerview item",
                targetView = recyclerView!!.getChildAt(2),
                introViewId = INTRO_CARD //THIS SHOULD BE UNIQUE ID
        ))
    }

    private fun loadData() {

        val song = Song("Diamond", R.drawable.diamond, "Rihanna")
        val songList = ArrayList<Song>()

        for (i in 0..9) {
            songList.add(song)
        }
        adapter!!.setSongList(songList)
    }


    private fun initializeRecyclerview() {

        val layoutManager = GridLayoutManager(activity, 2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)

        adapter = RecyclerViewAdapter(activity!!.applicationContext)
        recyclerView!!.adapter = adapter
    }

    override fun onUserClicked(viewId: String) {
        if (viewId == INTRO_CARD) {
            Toast.makeText(activity, "User Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        private val INTRO_CARD = "recyclerView_material_intro"
    }
}
