package com.example.calculadoraisr_2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private int opcionISR;
    Button btnCalcular;
    EditText salario;
    private Double salarioDiario;

    // Para formatear el salario que proviene de ReporteCalculos
    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Adaptador para el Spinner de tipos de ISR
        ArrayAdapter<CharSequence> adaptadorDatosArray =
                ArrayAdapter.createFromResource (this, R.array.datosArray, R.layout.spinner_dropdown);

        // Asignación de adaptador al spinner
        Spinner opcionISR = findViewById(R.id.opcionesISR);
        adaptadorDatosArray.setDropDownViewResource(R.layout.spinner_dropdown);
        opcionISR.setAdapter(adaptadorDatosArray);

        // Asignación de un evento al spinner
        opcionISR.setOnItemSelectedListener(evento);

        // Agregar evento onclik al boton para pasar a nueva actividad con envío de datos
        btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(evento2);

        //Recuperar datos para mandar a otra actividad
        salario = findViewById(R.id.salario);

        Intent intentReturn = getIntent();
        if(intentReturn != null && intentReturn.getExtras() != null){  // Intent desde ReporteCalculos
            salario.setText(df.format(intentReturn.getDoubleExtra("salario",0.0)));
            opcionISR.setSelection(intentReturn.getIntExtra("tipoISR",0)-1);
        }

    }
    // Funciones para manejar el evento de item seleccionado del spinner
    private AdapterView.OnItemSelectedListener evento = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            // opciones dependiendo que tipo de ISR se elige
            switch (i){
                case 0:
                    opcionISR = 1;
                    break;

                case 1:
                    opcionISR = 2;
                    break;

                case 2:
                    opcionISR = 3;
                    break;

                case 3:
                    opcionISR = 4;
                    break;

                case 4:
                    opcionISR = 5;
                    break;

                default:
                    opcionISR = 1;
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private View.OnClickListener evento2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Checamos que hay un numero ingresado o que el salario por dia sea mayor a 257.83 para que pague impuestos
            if(!salario.getText().toString().isEmpty()){
                    Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
                    intent.putExtra("salario",Double.parseDouble(salario.getText().toString()));
                    intent.putExtra("tipoISR",opcionISR);
                    startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show();
            }
        }
    };

}