package com.example.kayituygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Ziyaretci_Aydinlatma extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziyaretci_aydinlatma);


        PDFView pdfView = findViewById(R.id.aydinlatma_metni);
        pdfView.fromAsset("Aydınlatma Metni.pdf").load();
    }
}