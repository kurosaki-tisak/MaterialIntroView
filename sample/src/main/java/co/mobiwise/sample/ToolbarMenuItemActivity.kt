package co.mobiwise.sample

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import co.mobiwise.materialintro.MaterialIntroView

import co.mobiwise.materialintro.MaterialIntroListener
import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.sample.fragment.FocusFragment
import co.mobiwise.sample.fragment.GravityFragment
import co.mobiwise.sample.fragment.MainFragment
import co.mobiwise.sample.fragment.RecyclerviewFragment

/**
 * This activity demonstrates how to implement Material introView on ToolBar MenuItems
 *
 * @author Thomas Kioko
 */
class ToolbarMenuItemActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MaterialIntroListener {
    private var mIvShare: ImageView? = null
    private var mIvAbout: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        //User toolbar to access the views
        val ivSearch = toolbar.findViewById<View>(R.id.ivToolbarSearch) as ImageView
        mIvShare = toolbar.findViewById<View>(R.id.ivToolbarShare) as ImageView
        mIvAbout = toolbar.findViewById<View>(R.id.ivToolbarAbout) as ImageView

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        //show the intro view
        showIntro(ivSearch, MENU_SEARCH_ID_TAG, getString(R.string.guide_setup_profile), FocusGravity.CENTER)

    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_demo -> supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment()).commit()
            R.id.nav_gravity -> supportFragmentManager.beginTransaction().replace(R.id.container, GravityFragment()).commit()
            R.id.nav_focus -> supportFragmentManager.beginTransaction().replace(R.id.container, FocusFragment()).commit()
            R.id.nav_recyclerview -> supportFragmentManager.beginTransaction().replace(R.id.container, RecyclerviewFragment()).commit()
            R.id.nav_toolbar -> startActivity(Intent(applicationContext, ToolbarMenuItemActivity::class.java))
            R.id.nav_tab -> {
            }
            else -> {
            }
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Method that handles display of intro screens
     *
     * @param view         View to show guide
     * @param id           Unique ID
     * @param text         Display message
     * @param focusGravity Focus Gravity of the display
     */
    fun showIntro(view: View?, id: String, text: String, focusGravity: FocusGravity) {
        MaterialIntroView.build(this, MaterialIntroView.MaterialIntroViewConfig(
                focusGravity = focusGravity,
                focusType = Focus.MINIMUM,
                delayMillis = 100,
                isFadeAnimationEnabled = true,
                isPerformClick = true,
                textTitle = text,
                textInfo = text,
                targetView = view,
                materialIntroListener = this,
                introViewId = id
        ))
    }

    override fun onUserClicked(viewId: String) {
        when (viewId) {
            MENU_SEARCH_ID_TAG -> showIntro(mIvAbout, MENU_ABOUT_ID_TAG, getString(R.string.guide_setup_profile), FocusGravity.LEFT)
            MENU_ABOUT_ID_TAG -> showIntro(mIvShare, MENU_SHARED_ID_TAG, getString(R.string.guide_setup_profile), FocusGravity.LEFT)
            MENU_SHARED_ID_TAG -> Toast.makeText(this@ToolbarMenuItemActivity, "Complete!", Toast.LENGTH_SHORT).show()
            else -> {
            }
        }
    }

    companion object {

        private val MENU_SHARED_ID_TAG = "menuSharedIdTag"
        private val MENU_ABOUT_ID_TAG = "menuAboutIdTag"
        private val MENU_SEARCH_ID_TAG = "menuSearchIdTag"
    }
}
