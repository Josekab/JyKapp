package cr.ac.una.LocationWiki

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import cr.ac.menufragment.ListControlFinancieroFragment
import cr.ac.una.LocationWiki.service.LocationService

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            startLocationService()
        }

        // Manejar el Intent
        val locationName = intent.getStringExtra("location_name")
        // Para pruebas: usar "Costa Rica" en lugar de la ubicación desde el Intent
        //val locationName = "Costa Rica"
        if (locationName != null) {
            Log.d("MainActivity", "Lugar presionado: $locationName")
            openFragmentWithSearch(locationName)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openFragmentWithSearch(query: String) {
        val fragment = ListControlFinancieroFragment().apply {
            arguments = Bundle().apply {
                putString("search_query", query)
            }
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.home_content)

            if (currentFragment is ListControlFinancieroFragment) {
                super.onBackPressed()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.home_content, ListControlFinancieroFragment())
                    .commit()
            }
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_camera -> {
                // Aquí puedes manejar la acción para "@+id/nav_camera"
                // Por ejemplo, si quieres redirigir al usuario al inicio (MainActivity), puedes hacerlo de la siguiente manera:
                val intent = Intent(this, MainActivity::class.java)
                // Opcional: Si quieres limpiar todas las otras actividades en la pila de actividades, puedes usar las siguientes banderas
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            R.id.nav_gallery -> {
                // Aquí puedes manejar la acción para "@+id/nav_gallery"
                // Por ejemplo, si quieres redirigir al usuario a ListControlFinancieroFragment, puedes cargar ese fragmento
                val fragment = ListControlFinancieroFragment()
                reemplazarFragmento(fragment, getString(R.string.menu_gallery))
            }
            R.id.nav_manage -> {
                // Aquí puedes manejar la acción para "@+id/nav_manage"
                // Por ejemplo, si quieres que la aplicación se cierre cuando el usuario seleccione esta opción, puedes llamar a finish()
                finish()
            }
            else -> throw IllegalArgumentException("menu option not implemented!!")
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun reemplazarFragmento(fragment: Fragment, title: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment)
            .commit()
        setTitle(title)
    }

    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}