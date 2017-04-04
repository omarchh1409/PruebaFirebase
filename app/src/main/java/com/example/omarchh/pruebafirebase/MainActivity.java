
package com.example.omarchh.pruebafirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {



    private TextView nameTextView;
    private TextView emailTextView;
    private TextView datoTextView;
    ImageView imagenPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTextView=(TextView)findViewById(R.id.tv_name);
        emailTextView=(TextView)findViewById(R.id.tv_email);
        datoTextView=(TextView)findViewById(R.id.tv_dato);
        imagenPerfil=(ImageView)findViewById(R.id.imagePerfil);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Bitmap obtner_imagen;
        if(user!=null){

            String name=user.getDisplayName();
            String email=user.getEmail();
            String uid=user.getUid();
            String url=user.getPhotoUrl().toString();
            GetImageTask task=new GetImageTask();
            task.execute(new String[]{url});
            nameTextView.setText(name);
            emailTextView.setText(email);
            datoTextView.setText(uid);

        }
        else{
            goLoginScreen();
        }
    }



    private void goLoginScreen() {
        Intent intent=new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    private class GetImageTask extends AsyncTask<String,Void,Bitmap>
    {


        private ProgressDialog progreso;

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map=null;
            for(String url:urls){
                map=downloadImage(url);
            }
            return map;
        }

        @Override
        protected void onPreExecute(){
            progreso=new ProgressDialog(MainActivity.this);
            progreso.setMessage("CAPTURANDO IMAGEN");
            progreso.setCancelable(false);
            progreso.show();

        }
        @Override
        protected void onPostExecute(Bitmap result){
            progreso.dismiss();
            imagenPerfil.setImageBitmap(result);
        }

        private Bitmap downloadImage(String url){
            Bitmap bitmap=null;
            InputStream stream=null;
            BitmapFactory.Options bmOptions=new BitmapFactory.Options();

            try{
                stream=getHttpConnection(url);
                bitmap=BitmapFactory.decodeStream(stream,null,bmOptions);
                stream.close();
            }catch(IOException e1){
                e1.printStackTrace();
            }
            return bitmap;
        }

        private InputStream getHttpConnection(String urlString)throws IOException{

            InputStream stream=null;
            URL url=new URL(urlString);
            URLConnection connection=url.openConnection();
            try{
                HttpURLConnection httpConnection=(HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if(httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    stream=httpConnection.getInputStream();

                }

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return stream;
        }

    }

}

