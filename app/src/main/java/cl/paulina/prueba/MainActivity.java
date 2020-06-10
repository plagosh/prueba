package cl.paulina.prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ListView listaView;
    private ArrayList id, nombre, imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaView = findViewById(R.id.listview);
        id = new ArrayList();
        nombre = new ArrayList();
        imagen = new ArrayList();

        descargarDatos();
    }

    private void descargarDatos() {
        id.clear();
        nombre.clear();
        imagen.clear();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://yotrabajoconpecs.ddns.net/buscar_picto.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonarray = new JSONArray(new String(responseBody));
                        //for (int i = 0; i < jsonarray.length(); i++) {
                        for (int i = 0; i < 5; i++) {
                            id.add(jsonarray.getJSONObject(i).getString("id"));
                            nombre.add(jsonarray.getJSONObject(i).getString("nombre"));
                            imagen.add(jsonarray.getJSONObject(i).getString("imagen"));
                        }
                        listaView.setAdapter(new CustomAdapter(getApplicationContext()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private class CustomAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutinflater;
        TextView tvimagen, tvid, tvnombre;

        public CustomAdapter(Context applicationContext){
            this.ctx = applicationContext;
            layoutinflater = (LayoutInflater)this.ctx.getSystemService((LAYOUT_INFLATER_SERVICE));
        }

        @Override
        public int getCount() {

            return id.size();
        }

        @Override
        public Object getItem(int position){
            return position;
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent){
            ViewGroup viewgroup = (ViewGroup) layoutinflater.inflate(R.layout.list_view_item,null);
            tvid = (TextView) viewgroup.findViewById(R.id.tv_id);
            tvnombre = (TextView) viewgroup.findViewById(R.id.tv_nombre);
            tvimagen = (TextView) viewgroup.findViewById(R.id.tv_imagen);

            tvid.setText(id.get(position).toString());
            tvnombre.setText(nombre.get(position).toString());
            tvimagen.setText(imagen.get(position).toString());
            return viewgroup;
        }
    }
}

