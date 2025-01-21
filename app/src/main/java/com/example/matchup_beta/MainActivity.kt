package com.example.matchup_beta

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        enableEdgeToEdge()


        // splash screen
        installSplashScreen()
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        // inicialitza toolbar i l'asigna com actionbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // inicialitza el NavigationView
        navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)  // retraso de 1 segon
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment()).commit()
                navigationView.setCheckedItem(R.id.perfil)
                setSelectedItemColor(R.id.perfil)
            }
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        resetItemColors()
        item.isChecked = true
        setSelectedItemColor(item.itemId)
        when(item.itemId) {
            R.id.perfil -> replaceFragment(ProfileFragment())
            R.id.chat -> replaceFragment(MessagesFragment())
            R.id.ayuda -> replaceFragment(LikesFragment())
            R.id.ajustes -> replaceFragment(SettingsFragment())
            R.id.logout -> replaceFragment(LoginFragment())
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setSelectedItemColor(itemId: Int) {
        // Aquí se cambia el color del ítem seleccionado
        val item = navigationView.menu.findItem(itemId)
        item.icon?.setTint(getColor(R.color.selected_item_lht)) // Cambiar icono al color seleccionado
        item.title?.let { title ->
            val spannable = SpannableString(title)
            spannable.setSpan(ForegroundColorSpan(getColor(R.color.selected_text_lht)), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            item.title = spannable
        }
    }

    private fun resetItemColors() {
        // Restaura los colores de todos los ítems a su color no seleccionado
        for (i in 0 until navigationView.menu.size()) {
            val item = navigationView.menu.getItem(i)
            item.icon?.setTint(getColor(R.color.unselected_item_lht)) // Restaurar el color del icono
            item.title?.let { title ->
                val spannable = SpannableString(title)
                spannable.setSpan(ForegroundColorSpan(getColor(R.color.unselected_text_lht)), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                item.title = spannable
            }
        }
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}
