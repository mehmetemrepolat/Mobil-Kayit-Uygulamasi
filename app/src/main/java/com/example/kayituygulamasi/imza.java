package com.example.kayituygulamasi;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import androidx.appcompat.app.AlertDialog;
import net.gotev.uploadservice.UploadService;

public class imza extends AppCompatActivity {

/////////////////SQL BAĞLANTISI////////////////////
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "MobilUygulama_Kayit";
    private static String username = "mehmetpolat";
    private static String password = "mp@123.com";
    //private static String url = "jdbc:jtds:sqlserver://10.0.0.23:50899";
    //private Connection connection = null;
/////////////////SQL BAĞLANTISI////////////////////
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private Button mClearButton, mSaveButton;
    private TextView signature_pad_description;
    //int toplam_kayit = 0;
    public static String imza_txt = "";


    ////////img|mobile-to-server////////
    //final String SERVER = "";
    int imza_kayit_talep = 1;
    ////////img|mobile-to-server////////

    public static File photo;
    public static String ziyaret_imza;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_imza);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED); //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView signature_pad_description = findViewById(R.id.signature_pad_description);


        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        //try
        //{
        //    Class.forName(Classes);
      //      connection = DriverManager.getConnection(url,"mehmetpolat","mp@123.com");
        //}
        //catch (ClassNotFoundException e)
        //{
         ///   e.printStackTrace();
           // Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
        //}
        //catch (SQLException throwables)
        //{
        //    Toast.makeText(getApplicationContext(), throwables.getMessage(), Toast.LENGTH_SHORT).show();
        //}

//////////////////////////////////
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener()
        {
            @Override
            public void onStartSigning()
            { }

            @Override
            public void onSigned()
            {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear()
            {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        }
        );

        mSaveButton = (Button) findViewById(R.id.save_button);
        mClearButton = (Button) findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(imza.this);
                builder.setTitle("Kayıt Uyarısı").setMessage("İmzayı Kaydetmek istediğinizden emin misiniz ?").setCancelable(false)
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.Elektronik_Imza.setText("İmzalandı");
                                MainActivity.kayit_al_1.setEnabled(true);
                                MainActivity.Elektronik_Imza.setEnabled(false);
                                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

                                if(addSvgSignatureToGallery(mSignaturePad.getSignatureSvg()))
                                {
                                    Toast.makeText(imza.this,"Form İmzalandı", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(imza.this,"Form imzalanamadı||HATA", Toast.LENGTH_SHORT).show();
                                }


                                finish();

                            }
                        }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(imza.this,"Seçilen seçenek: Hayır",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog  = builder.create();
                dialog.show();


            }
        }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_EXTERNAL_STORAGE:
                {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(imza.this, "İmza Yüklenemedi", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName)
    {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs())
        {
            Log.e("SignaturePad", "Klasör Oluşturulmadı");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException
    {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature)
    {
        boolean result = false;
        try
        {
            String imza_ismi =  String.valueOf(MainActivity.Ziyaretci_TC_Kimlik_Numarasi)+ ".jpg";
            photo = new File(getAlbumStorageDir("Signature Pad"), String.format(imza_ismi, System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        }

        catch (IOException e)
        {
            Toast.makeText(imza.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }


    private void scanMediaFile(File photo)

    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        imza.this.sendBroadcast(mediaScanIntent);

    }

    public Boolean addSvgSignatureToGallery(String signatureSvg)
    {
        boolean result = false;
        try
        {
            String svg_to_txt =  String.valueOf(MainActivity.Ziyaretci_TC_Kimlik_Numarasi)+ ".txt";
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format(svg_to_txt, System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            ziyaret_imza = svgFile.toString();
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            imza_txt = signatureSvg;
            result = true;
        }
        catch (IOException e)
        {
            Toast.makeText(imza.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }


    public static void verifyStoragePermissions(Activity activity)
    {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions
                    (
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                    );

        }
    }
}


