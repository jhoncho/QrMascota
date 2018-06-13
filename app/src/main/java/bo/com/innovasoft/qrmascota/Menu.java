package bo.com.innovasoft.qrmascota;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Menu extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA=1;
    private ZXingScannerView scannerView;
    private Object view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);}
   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            if(checkPermission()){
                Toast.makeText(Menu.this,"Permission is granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }
    }*/
    public void EscanerQr(View view)
    {
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

   /* private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(Menu.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }
    public void onRequestPermissionsResult(final int requestCode, String permission[], int grantResults[])
    {
       switch(requestCode){
           case REQUEST_CAMERA:
               if(grantResults.length>0)
               {
                   boolean cameraAccepted=grantResults[0] ==PackageManager.PERMISSION_GRANTED;
                   if(cameraAccepted)
                   {
                       Toast.makeText(Menu.this, "Permission Granted", Toast.LENGTH_LONG).show();
                   }
                   else
                   {
                       Toast.makeText(Menu.this, "Permission Denied", Toast.LENGTH_LONG).show();
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                       {
                           if(shouldShowRequestPermissionRationale(CAMERA))
                           {
                            displayAlertMessage("you need to allow access for both permissions",
                               new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                       {
                                           requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                       }
                                   }
                               } );
                            return;

                           }
                       }
                   }
               }
               break;
       }
    }*/
   /* @Override
    public void onResume()
    {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkPermission())
            {
                if (scannerView==null)
                {
                 scannerView = new ZXingScannerView(this);
                 setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else
            {
                requestPermission();
            }
        }
    }*/
    @Override
    public void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void    displayAlertMessage(String message, DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(Menu.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void handleResult(final Result result) {
        final String scanResult =result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(scanResult));
                startActivity(intent);
            }
        });
        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();
    }


}
