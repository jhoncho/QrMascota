package bo.com.innovasoft.qrmascota;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
public class crear_usuario extends AppCompatActivity implements View.OnClickListener  {

    private final String CARPETA_RAIZ="misImagenesMaskota/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";

    final int COD_SELECCIONA=100;
    final int COD_FOTO=200;

    Button botonCargar,btncrearcuenta;
    ImageView imagen;
    String path;
    /*para llenar los datos de crear usuario*/
    EditText txtNombre,txtapellido,txtcorreo,ettelefono,txtcontra;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_usuario);
        /*para llenar el formulario de crear usuario*/
          txtNombre= findViewById(R.id.etnombre);
          txtapellido=findViewById(R.id.etapellido);
          txtcorreo=findViewById(R.id.etcorreo);
          ettelefono=findViewById(R.id.ettelefono);
          txtcontra=findViewById(R.id.etcontrasena);

          btncrearcuenta= (Button) findViewById(R.id.btncrearusuario);
          //btncrearcuenta.setOnClickListener(this);


        /*aqui termina para llenardatos*/

        //desde aqui hasta
        Button siguiente=(Button) findViewById(R.id.btnmascota);
       // Button siguiente = (Button) findViewById(R.id.btnmascota);
        siguiente.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                //CODIGO PARA QUE PASE A LA SIGUIENTE PANTALLA Y ASI MISMO NO PUEDA RETORNAR
                Intent siguiente=new Intent(crear_usuario.this, crear_mascota.class);
                startActivity(siguiente);

            }
        });//aqui es para poder pasar de pantalla


        imagen=(ImageView) findViewById(R.id.fotousuario);
        botonCargar=(Button)findViewById(R.id.btncargar);

        if (validaPermisos()){
            botonCargar.setEnabled(true);
        }else {
            botonCargar.setEnabled(false);
        }
    }



    //para boton crearusuario
    @Override
    public void onClick(View view)
    {

        final String nombre=txtNombre.getText().toString();
        final String apellido=txtapellido.getText().toString();
        final String email=txtcorreo.getText().toString();
        final int celular= Integer.parseInt(ettelefono.getText().toString());
        final String contrasena=txtcontra.getText().toString();

        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean succes=jsonResponse.getBoolean("succes");

                    if (succes)
                    {
                        //para cambiar de activity
                        Intent intent=new Intent(crear_usuario.this,Menu.class);
                        crear_usuario.this.startActivity(intent);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(crear_usuario.this);
                        builder.setMessage("Error de Registro").setNegativeButton("Retry",null).create().show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest=new RegisterRequest(nombre,apellido,email,celular,contrasena, respoListener);
        RequestQueue queue= Volley.newRequestQueue(crear_usuario.this);
        queue.add(registerRequest);


    }


        /* APARTIR DE ANDROID N SE TIENE QUE PEDIR PERMISO PARA UTLIZAR LOS SERVICIOS
        CODIGO PARA PEDIR PERMISOS PARA LEER LA CAMARA Y LA MEMORIA EXTERNA */

    private boolean validaPermisos() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        //permiso para camara y leer memoria externa
        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if ((shouldShowRequestPermissionRationale(CAMERA))||(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){

            cargarDialogoRecomendacion();

        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }
    /*INICIO PARA PEDIR PERMISOS MANUAL DESPUES DE QUE EL USUARIO RECHAZA LOS MISMOS*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==100){
            if (grantResults.length==2 && grantResults[0] ==PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                botonCargar.setEnabled(true);

            }else {
                solicitarPermisoManual();
            }

        }
    }


    private void solicitarPermisoManual() {

        final CharSequence[] opciones={"Si","No"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(crear_usuario.this);
        alertOpciones.setTitle("¿Desea Configurar los permisos de Forma Manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (opciones[i].equals("Si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri =Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else {

                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }

            }


        });
        alertOpciones.show();
    }
//FIN PERMISO MANUAL

    /*INICIO PARA CARGAR DIALOGO PARA PEDIR PERMISOS PARA QUE FUNCIONE LA APP*/
    private void cargarDialogoRecomendacion() {

        AlertDialog.Builder dialogo=new AlertDialog.Builder(crear_usuario.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar",new DialogInterface.OnClickListener(){


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);

            }
        });
        dialogo.show();

    }
    /*FIN DE DIALOGO*/
        public void onclick(View view) {

        cargarimagen();

    }

    private void cargarimagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(crear_usuario.this);
        alertOpciones.setTitle("Seleccione una Opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else {
                    if (opciones[i].equals("Cargar Imagen")){

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion "),COD_SELECCIONA);
                        dialog.dismiss();
                    }else {
                        dialog.dismiss();
                    }

                }

            }
        });
        alertOpciones.show();
    }
    /*INICIO PARA PODER UTILIZAR LA CAMARA */
    private void tomarFotografia() {
        File fileImagen = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean iscreada = fileImagen.exists();
        String nombreImagen="";

        if (iscreada==false){
            iscreada=fileImagen.mkdirs();
        }
        if (iscreada==true){
            nombreImagen=(System.currentTimeMillis()/100)+".jpg";
        }
        path = Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
        //CONSTRUCTOR PARA DETECTAR FALLAS
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // FIN DE CONSTRUCTOR
        File imagen = new File(path);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        startActivityForResult(intent,COD_FOTO);
    }
    /*fIN DE CODIGO PARA UTILIZAR LA CAMARA*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath =data.getData();
                    imagen.setImageURI(miPath);
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this,new String[]{path},null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de Almacenamiento","Path: "+path);
                                }
                            });
                    Bitmap bitmap= BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);
                    break;

            }
        }
    }

}

