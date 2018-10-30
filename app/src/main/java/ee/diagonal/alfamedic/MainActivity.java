package ee.diagonal.alfamedic;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    //URL inicial de la aplicación
    private final String URL = "http://www.app.alfamedic.es/";

    private WebView webView;
    private ImageView imgAlfaMedic;
    private LinearLayout linearLayout;
    private int firstLoad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.web_view);
        webView.setVisibility(View.GONE);
        imgAlfaMedic = findViewById(R.id.img_alfamedic);
        linearLayout = findViewById(R.id.header_progress);
        /*
         * Las páginas se cargan dentro de la aplicación,
         * en caso de que se abra un link externo las abrirá
         * desde el navegador del teléfono
         */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final String HOST = Uri.parse(URL).getHost();
                String urlHost = Uri.parse(url).getHost();
                if (HOST.equals(urlHost)) {
                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        });
        /*
         * Mostrar la vista de carga entre cambios de página
         */
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                /* No muetra la vista de carga durante el splash
                    Cambiar el número de cargas para adaptarse a la página
                 */
                if (firstLoad > 4)
                    linearLayout.setVisibility(View.VISIBLE);
                else
                    firstLoad++;
                if (progress == 100) {
                    webView.setVisibility(View.VISIBLE);
                    imgAlfaMedic.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(URL);
    }

    @Override
    /*
     * Volver una página atrás pulsando la tecla del teléfono,
     * si no existe sale de la aplicación
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
