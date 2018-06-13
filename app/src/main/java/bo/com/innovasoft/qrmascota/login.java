package bo.com.innovasoft.qrmascota;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Aqui comienza elcodigo para pasar de actividad para crear usuario
        Button siguiente=(Button) findViewById(R.id.btncrearusuario);
        siguiente.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //CODIGO PARA QUE PASE A LA SIGUIENTE PANTALLA Y ASI MISMO NO PUEDA RETORNAR
                Intent siguiente=new Intent(login.this, crear_usuario.class);
                startActivity(siguiente);

            }
        });//Aqui termina el codigo para pasar de pantalla

        //Para el Login
        
    }
}
