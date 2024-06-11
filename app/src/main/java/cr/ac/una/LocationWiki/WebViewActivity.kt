package cr.ac.una.LocationWiki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

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
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("openListControlFinancieroFragment", true)
        startActivity(intent)
        finish()
    }
}