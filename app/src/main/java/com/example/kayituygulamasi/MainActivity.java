package com.example.kayituygulamasi;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import com.github.gcacace.signaturepad.views.SignaturePad;


public class MainActivity extends AppCompatActivity {

    public static EditText Z_Adi_1, Z_SoyAdi_1, Z_TelefonNumarasi_1, Z_MailAdresi_1, Z_ZiyaretSebebi_1, Z_TC_Kimlik_1;
    private CheckBox KVKK_1, Acik_Riza_1, Checkbox_Tedarikci;
    private Button KVKK_Butonu, Acik_Riza_Butonu, Tedarikci_Butonu;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView textView4, KVKK_Metni_Text, acikriza_metni_Text;
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "Deneme";
    private static String username = "sa";
    private static String password = "1234";
    private static String url = "jdbc:jtds:sqlserver://192.168.1.10:1433";
    private Connection connection = null;
    public SignaturePad signaturePad;
    public static String Ziyaretci_TC_Kimlik_Numarasi;
    public static String TC_Kimlik_Imza;
    public static Button kayit_al_1, Elektronik_Imza;
    public static int mail_kontrol = 0;

    /////////////////////////////////////
    ////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED); //

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Z_Adi_1 = ((EditText) findViewById(R.id.Z_Adi));
        Z_SoyAdi_1 = (EditText) findViewById(R.id.Z_SoyAdi);
        Z_TelefonNumarasi_1 = (EditText) findViewById(R.id.Z_TelefonNumarasi);
        Z_TC_Kimlik_1 = (EditText) findViewById(R.id.Z_TC_No);
        Z_MailAdresi_1 = (EditText) findViewById(R.id.Z_MailAdresi);
        Z_ZiyaretSebebi_1 = (EditText) findViewById(R.id.Z_ZiyaretSebebi);
        kayit_al_1 = (Button) findViewById(R.id.kayit_al);
        sp = getSharedPreferences("Giris Bilgi", MODE_PRIVATE);
        editor = sp.edit();


        ////////////////////////////////////TIKLANABİLİR METİN//////////////////////////////////////////////////

        KVKK_Metni_Text = (TextView) findViewById(R.id.KVKK_Text);
        String kvkk_link = "KVKK Metnini Okudum Kabul Ediyorum.";
        SpannableString ss = new SpannableString(kvkk_link);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent  intent_ziyaretci = new Intent(MainActivity.this, Ziyaretci_Aydinlatma.class);
                startActivity(intent_ziyaretci);
                //PDF AÇILIŞI
            }
        };
        ss.setSpan(clickableSpan1, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        KVKK_Metni_Text.setText(ss);
        KVKK_Metni_Text.setMovementMethod(LinkMovementMethod.getInstance());



        ////////////////////////////////////TIKLANABİLİR METİN//////////////////////////////////////////////////
        ////////////////////////////////////TIKLANABİLİR METİN//////////////////////////////////////////////////
        acikriza_metni_Text = (TextView) findViewById(R.id.AcikRiza_Text);
        String acikriza_link = "Açık Rıza Metnini Okudum Kabul Ediyorum.";
        SpannableString ss2 = new SpannableString(acikriza_link);
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //PDF AÇILIŞI
                Intent  intent_kvkk = new Intent(MainActivity.this, KVKK_Metni_PDF.class);
                startActivity(intent_kvkk);
            }
        };
        ss2.setSpan(clickableSpan2, 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        acikriza_metni_Text.setText(ss2);
        acikriza_metni_Text.setMovementMethod(LinkMovementMethod.getInstance());
        ////////////////////////////////////TIKLANABİLİR METİN//////////////////////////////////////////////////
        ////////////////////////////////////TIKLANABİLİR METİN//////////////////////////////////////////////////



        //////////////////////////////// SAAT TARİH ////////////////////////////////////////////////

        Thread t = new Thread()
        {
            @Override
            public void run(){
                try{
                    while (!isInterrupted())
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tarihsaat = (TextView) findViewById(R.id.tarih_saat);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy hh-mm-ss a");
                                String dateString = sdf.format(date);
                                tarihsaat.setText(dateString);
                            }
                        });
                    }
                }
                catch (InterruptedException e)
                {

                }

            }
        };
        t.start();

        //////////////////////////////// SAAT TARİH ////////////////////////////////////////////////



        /////////////////////////////////////////////////// ELEKTRONİK İMZA SAYFA GEÇİŞİ ////////////////////////////////////////////////////////////

        Elektronik_Imza = (Button) findViewById(R.id.button);
        Elektronik_Imza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ////////////////Alan Kontrolü /////////////////////////
                ////////////////Alan Kontrolü /////////////////////////

                String Ziyaretci_Adi = Z_Adi_1.getText().toString();
                String Ziyaretci_SoyAdi = Z_SoyAdi_1.getText().toString();
                String Ziyaretci_TelefonNumarasi = Z_TelefonNumarasi_1.getText().toString();
                Ziyaretci_TC_Kimlik_Numarasi = Z_TC_Kimlik_1.getText().toString();
                String Ziyaretci_MailAdresi = Z_MailAdresi_1.getText().toString();
                String Ziyaret_Sebebi = Z_ZiyaretSebebi_1.getText().toString();
                KVKK_1 = (CheckBox) findViewById(R.id.KVKK);
                Acik_Riza_1 = (CheckBox) findViewById(R.id.Acik_Riza);
                if (KVKK_1.isChecked())      //KONTROL BAŞLANGIÇ                                                                            //KVKK TİK KONTROLÜ
                {
                    if (Acik_Riza_1.isChecked())                                                                                            //AÇIK RIZA METNİ KONTROLÜ
                    {
                        if                                                                                                                  ///////////////////////////
                        (                                                                                                                   ///////////////////////////
                                Ziyaretci_Adi.length() != 0 && Ziyaretci_SoyAdi.length() != 0                                               ///////////////////////////
                                && Ziyaretci_TelefonNumarasi.length() != 0                                                                  ///EDIT TEXT KONTROLLERİ///
                                && Ziyaretci_TC_Kimlik_Numarasi.length() != 0                                                               ///////////////////////////
                                && Ziyaretci_MailAdresi.length() !=0                                                                        ///////////////////////////
                                && Ziyaret_Sebebi.length() != 0                                                                             ///////////////////////////
                        )
                            {
                                if (Ziyaretci_TC_Kimlik_Numarasi.length() == 11)                                                            //TC KİMLİK UZUNLUK KONTROLÜ
                                {
                                    if(Ziyaretci_TelefonNumarasi.length() ==10 || Ziyaretci_TelefonNumarasi.length() ==11)                  //TELEFON NUMARASI UZUNLUK KONTROLÜ
                                    {
                                        mail_kontrol = Ziyaretci_MailAdresi.indexOf('@');
                                        int mail_kontrol2 = 0;
                                        mail_kontrol2 = Ziyaretci_MailAdresi.indexOf(".com");
                                        if(mail_kontrol != -1)
                                        {
                                            if (mail_kontrol2 != -1)
                                            {
                                                Toast.makeText(getApplicationContext(), "Bütün Alanlar Dolduruldu.", Toast.LENGTH_SHORT).show();       ///KONTROLLER BİTTİKTEN SONRA KOMUT
                                                Ziyaretci_TC_Kimlik_Numarasi = Z_TC_Kimlik_1.getText().toString();
                                                Intent intent_imza = new Intent(MainActivity.this, imza.class);
                                                startActivity(intent_imza);
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(), "Mail Adresinizi Yanlış Girdiniz.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Mail Adresinizi Yanlış Girdiniz.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Telefon Numaranızı Eksik Girdiniz.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                    {
                                        Toast.makeText(getApplicationContext(), "TC Kimlik Numaranızı Eksik Girdiniz.", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        else
                            {
                                Toast.makeText(getApplicationContext(), "İmzalamadan Önce Bütün Alanlar Doldurulmalıdır.", Toast.LENGTH_SHORT).show();
                            }
                    }
                    else{Toast.makeText(getApplicationContext(), "Açık Rıza Metnini Okuyup Kabul Etmeniz Gerekmektedir.", Toast.LENGTH_SHORT).show(); } }

                else{
                    if(Ziyaretci_Adi.length() == 0 && Ziyaretci_SoyAdi.length() == 0 && Ziyaretci_TelefonNumarasi.length() == 0 && Ziyaretci_TC_Kimlik_Numarasi.length() == 0 && Ziyaretci_MailAdresi.length() ==0 && Ziyaret_Sebebi.length() == 0)
                    {Toast.makeText(getApplicationContext(), "İmzalamadan Önce Bütün Alanlar Doldurulmalıdır.", Toast.LENGTH_SHORT).show(); }//BÜTÜN ALANLARIN DOLU OLUP OLMADIĞI KONTROL EDİLİR DOLUYSA KVKK TİKİNİN İŞARETLENİP İŞARETLENMEDİĞİ KONTROL EDİLİR.

                    else{Toast.makeText(getApplicationContext(), "KVKK Metnini Okuyup Kabul Ettiğinizi Belirtmelisiniz.", Toast.LENGTH_SHORT).show(); }
                    }                       ///KONTROL BİTİŞ
            }});
        ////////////////Alan Kontrolü /////////////////////////
        ////////////////Alan Kontrolü /////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////TEDARİKÇİ KONTROL /////////////////////////
        ////////////////TEDARİKÇİ KONTROL /////////////////////////

        ////////////////TEDARİKÇİ KONTROL /////////////////////////
        ////////////////TEDARİKÇİ KONTROL /////////////////////////


        /////////////////////////////////////////////////// ELEKTRONİK İMZA SAYFA GEÇİŞİ ////////////////////////////////////////////////////////////
        textView4 = findViewById(R.id.textView4);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url,"sa","1234");
            textView4.setText("Sunucuyla Bağlantı Başarılı");
            }
        catch (ClassNotFoundException e)
            {
            e.printStackTrace();
            textView4.setText("Sunucu Bağlantı Hatası");
            }
        catch (SQLException throwables)
            {
            textView4.setText((throwables.getMessage()));
            }

        /////////////////////
        kayit_al_1.setEnabled(false);
        //Kayıt Gönderme Butonu
        kayit_al_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Ziyaretci_Adi = Z_Adi_1.getText().toString();
                String Ziyaretci_SoyAdi = Z_SoyAdi_1.getText().toString();
                String Ziyaretci_TelefonNumarasi = Z_TelefonNumarasi_1.getText().toString();
                Ziyaretci_TC_Kimlik_Numarasi = Z_TC_Kimlik_1.getText().toString();
                String Ziyaretci_MailAdresi = Z_MailAdresi_1.getText().toString();
                String Ziyaret_Sebebi = Z_ZiyaretSebebi_1.getText().toString();
                KVKK_1 = (CheckBox) findViewById(R.id.KVKK);
                Acik_Riza_1 = (CheckBox) findViewById(R.id.Acik_Riza);


                if (KVKK_1.isChecked())      //KONTROL BAŞLANGIÇ                                                                            //KVKK TİK KONTROLÜ
                {
                    if (Acik_Riza_1.isChecked())                                                                                            //AÇIK RIZA METNİ KONTROLÜ
                    {
                        if                                                                                                                  ///////////////////////////
                        (                                                                                                                   ///////////////////////////
                                Ziyaretci_Adi.length() != 0 && Ziyaretci_SoyAdi.length() != 0                                               ///////////////////////////
                                        && Ziyaretci_TelefonNumarasi.length() != 0                                                          ///EDIT TEXT KONTROLLERİ///
                                        && Ziyaretci_TC_Kimlik_Numarasi.length() != 0                                                       ///////////////////////////
                                        && Ziyaretci_MailAdresi.length() !=0                                                                ///////////////////////////
                                        && Ziyaret_Sebebi.length() != 0                                                                     ///////////////////////////
                        )
                        {
                            if (Ziyaretci_TC_Kimlik_Numarasi.length() == 11)                                                            //TC KİMLİK UZUNLUK KONTROLÜ
                            {
                                if(Ziyaretci_TelefonNumarasi.length() ==10 || Ziyaretci_TelefonNumarasi.length() ==11)                  //TELEFON NUMARASI UZUNLUK KONTROLÜ
                                {
                                    mail_kontrol = Ziyaretci_MailAdresi.indexOf('@');
                                    int mail_kontrol2 = 0;
                                    mail_kontrol2 = Ziyaretci_MailAdresi.indexOf(".com");
                                    if(mail_kontrol != -1)
                                    {
                                        if (mail_kontrol2 != -1) {///KONTROLLER BİTTİKTEN SONRA KOMUT BAŞLANGICI
                                            Toast.makeText(getApplicationContext(), "Bütün Alanlar Dolduruldu", Toast.LENGTH_SHORT).show();
                                            try {
                                                if (connection != null) {
                                                    if(!Checkbox_Tedarikci.isChecked())
                                                    {
                                                        Statement stmt = connection.createStatement();
                                                        //SQL SORGU - VERİ GİRME KOMUTU
                                                        String SQL_Sorgu = new StringBuilder()
                                                                .append("use Deneme;").append("INSERT INTO Ziyaretci_Kayit_Tablosu" +
                                                                        "(Ziyaretci_Ad, Ziyaretci_SoyAd," +
                                                                        "Ziyaretci_TC_Kimlik, Ziyaretci_Telefon_Numarasi," +
                                                                        "Ziyaretci_Mail_Adresi, Ziyaretci_Sebep, Ziyaretci_Imza, Ziyaretci_Kayit_Zaman)")
                                                                .append("VALUES('")
                                                                .append(Ziyaretci_Adi).append("','")
                                                                .append(Ziyaretci_SoyAdi).append("','")
                                                                .append(Ziyaretci_TC_Kimlik_Numarasi).append("','")
                                                                .append(Ziyaretci_TelefonNumarasi).append("','")
                                                                .append(Ziyaretci_MailAdresi).append("','")
                                                                .append(Ziyaret_Sebebi).append("','")
                                                                .append(imza.imza_txt).append("',")
                                                                .append("GETDATE()").append("")
                                                                .append(")")
                                                                .toString(); ///////////////////İMZA .SVG -> TEXT
                                                        ResultSet rs = stmt.executeQuery(SQL_Sorgu);
                                                        //SQL SORGU - VERİ GİRME KOMUTU
                                                        while (rs.next())
                                                        {
                                                            textView4.setText(rs.getString(2));
                                                        }
                                                        recreate();
                                                    }
                                                    else  {
                                                        Statement stmt = connection.createStatement();
                                                        //SQL SORGU - VERİ GİRME KOMUTU
                                                        String SQL_Sorgu = new StringBuilder()
                                                                .append("use MobilUygulama_Kayit;").append("INSERT INTO Tedarikci_Kayit_Tablosu" +
                                                                        "(Ziyaretci_Ad, Ziyaretci_SoyAd," +
                                                                        "Ziyaretci_TC_Kimlik, Ziyaretci_Telefon_Numarasi," +
                                                                        "Ziyaretci_Mail_Adresi, Ziyaretci_Sebep, Ziyaretci_Imza, Ziyaretci_Kayit_Zaman)")
                                                                .append("VALUES('")
                                                                .append(Ziyaretci_Adi).append("','")
                                                                .append(Ziyaretci_SoyAdi).append("','")
                                                                .append(Ziyaretci_TC_Kimlik_Numarasi).append("','")
                                                                .append(Ziyaretci_TelefonNumarasi).append("','")
                                                                .append(Ziyaretci_MailAdresi).append("','")
                                                                .append(Ziyaret_Sebebi).append("','")
                                                                .append(imza.imza_txt).append("',")
                                                                .append("GETDATE()").append("")
                                                                .append(")")
                                                                .toString(); ///////////////////İMZA .SVG -> TEXT
                                                        ResultSet rs = stmt.executeQuery(SQL_Sorgu);
                                                        //SQL SORGU - VERİ GİRME KOMUTU
                                                        while (rs.next())
                                                        {
                                                            textView4.setText(rs.getString(2));
                                                        }
                                                        recreate();
                                                    }
                                                } else {
                                                    textView4.setText("Sunucu Bağlantısı Kurulamadı");
                                                }
                                            } catch (SQLException throwables) {
                                                textView4.setText((throwables.getMessage()));
                                            }
                                            Z_Adi_1.setText("");                                    ////////////////////////
                                            Z_SoyAdi_1.setText("");                                 ////////////////////////
                                            Z_TelefonNumarasi_1.setText("");                        ////////////////////////
                                            Z_TC_Kimlik_1.setText("");                              ////////////////////////
                                            Z_TelefonNumarasi_1.setText("");                        ////////////////////////
                                            Z_MailAdresi_1.setText("");                             ///TEXTLERİ SIFIRLAMA///
                                            Z_ZiyaretSebebi_1.setText("");                          ////////////////////////
                                            Elektronik_Imza.setEnabled(true);                       ////////////////////////
                                            Elektronik_Imza.setText("İMZALA");                      ////////////////////////
                                            KVKK_1.setChecked(false);                               ////////////////////////
                                            Acik_Riza_1.setChecked(false);                          ////////////////////////
                                            kayit_al_1.setEnabled(false);                           ////////////////////////

                                            Toast.makeText(getApplicationContext(), "Kaydınız Gönderilmiştir.", Toast.LENGTH_SHORT).show();

                                        }///KONTROLLER BİTTİKTEN SONRA KOMUT BİTİŞİ
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Mail Adresinizi Yanlış Girdiniz.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Mail Adresinizi Yanlış Girdiniz.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Telefon Numaranızı Eksik Girdiniz.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "TC Kimlik Numaranızı Eksik Girdiniz.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "İmzalamadan Önce Bütün Alanlar Doldurulmalıdır.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else{Toast.makeText(getApplicationContext(), "Açık Rıza Metnini Okuyup Kabul Etmeniz Gerekmektedir.", Toast.LENGTH_SHORT).show(); } }

                else{
                    if(Ziyaretci_Adi.length() == 0 && Ziyaretci_SoyAdi.length() == 0 && Ziyaretci_TelefonNumarasi.length() == 0 && Ziyaretci_TC_Kimlik_Numarasi.length() == 0 && Ziyaretci_MailAdresi.length() ==0 && Ziyaret_Sebebi.length() == 0)
                    {Toast.makeText(getApplicationContext(), "İmzalamadan Önce Bütün Alanlar Doldurulmalıdır.", Toast.LENGTH_SHORT).show(); }//BÜTÜN ALANLARIN DOLU OLUP OLMADIĞI KONTROL EDİLİR DOLUYSA KVKK TİKİNİN İŞARETLENİP İŞARETLENMEDİĞİ KONTROL EDİLİR.

                    else{Toast.makeText(getApplicationContext(), "KVKK Metnini Okuyup Kabul Ettiğinizi Belirtmelisiniz.", Toast.LENGTH_SHORT).show(); }
                }
            }
    }
    );
    };
}


