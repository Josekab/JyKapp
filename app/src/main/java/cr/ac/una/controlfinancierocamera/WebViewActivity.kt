package cr.ac.una.controlfinancierocamera

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import cr.ac.menufragment.ListControlFinancieroFragment

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            // Replace the current fragment with ListControlFinancieroFragment
            val fragment = ListControlFinancieroFragment()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }
        webView.settings.javaScriptEnabled = true

        val url = intent.getStringExtra("url")
        Log.d("WebViewActivity", "URL recibida: $url")

        if (!url.isNullOrBlank()) {
            webView.loadUrl(url)
        } else {
            Log.e("WebViewActivity", "URL es null o está vacía")
            Toast.makeText(this, "URL inválida. No se puede cargar la página.", Toast.LENGTH_SHORT).show()
        }
    }
}