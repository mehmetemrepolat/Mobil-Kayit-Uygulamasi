package com.example.kayituygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class KVKK_Metni_PDF extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kvkk_metni_pdf);


        PDFView pdfView = findViewById(R.id.KVKK_Metin_PDF);
        pdfView.fromAsset("KVKK.pdf").load();


    }
}