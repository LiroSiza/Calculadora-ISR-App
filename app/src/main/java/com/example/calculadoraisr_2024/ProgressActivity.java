package com.example.calculadoraisr_2024;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProgressActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000;    //3 seg

    Intent newIntent;
    Double salario;
    int opcionISR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.progress_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Simula tiempo de carga mediante un Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Se reciben los datos
                newIntent = getIntent();
                salario = newIntent.getDoubleExtra("salario",0.0);
                opcionISR = newIntent.getIntExtra("tipoISR",0);

                // Se irá a la actividad donde se harán los calculos, pasando los datos
                Intent intent = new Intent(ProgressActivity.this, ReporteCalculos.class);
                intent.putExtra("salario", salario);
                intent.putExtra("tipoISR",opcionISR);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);

    }
}
