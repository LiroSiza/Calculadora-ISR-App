package com.example.calculadoraisr_2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;

public class ReporteCalculos extends AppCompatActivity {

    // Recibirá el intent recibido, newIntent enviará el nuevo
    Intent intent, newIntent;
    Double salario;
    int opcionISR;

    //Boton de regreso y de Información
    ImageButton btnImage, btnInfo;
    // Para formatear la vista de calculos
    NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.US);
    NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();

    // Estructua del reporte
    TextView ingreso, limite, diferencia, tasa, cuota, impuesto, impuestoM, percepcion, subEmpleo;

    // Datos calculados del reporte
    double isrCalculado, subsidio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.reporte_calculo_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        // Boton de regreso
        btnImage = findViewById(R.id.imageButton);
        btnImage.setOnClickListener(evento);
        // Boton de info
        btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(evento);

        intent = getIntent();
        salario = intent.getDoubleExtra("salario",0.0);
        opcionISR = intent.getIntExtra("tipoISR",0);

        // Inicializar los TextViews del reporte
        ingreso = findViewById(R.id.ingreso);
        limite = findViewById(R.id.limite);
        diferencia = findViewById(R.id.diferencia);
        tasa = findViewById(R.id.tasa);
        cuota = findViewById(R.id.cuota);
        impuesto = findViewById(R.id.impuestoISR);
        impuestoM = findViewById(R.id.impuestoMarginal);
        percepcion = findViewById(R.id.percepcion);
        subEmpleo = findViewById(R.id.subEmpleo);

        // Reporte con variables y calculos
        Double salarioFormateado = salario;
        ingreso.setText(formato.format(salarioFormateado));

        isrCalculado = calcularISR(salario,opcionISR); //Calculo de ISR
        impuesto.setText(formato.format(isrCalculado));

        subsidio = calcularSubsidio(salario,opcionISR); // Calcular subsidio
        subEmpleo.setText(formato.format(subsidio));
        percepcion.setText(formato.format(salario - isrCalculado + subsidio)); // Calcular saldo final
    }
    private View.OnClickListener evento = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == btnImage){ // Boton return
                // Le enviamos el salario en decimal y la opcion elegida a la pantalla anterrior MainActivity
                Intent intent = new Intent(ReporteCalculos.this, MainActivity.class);
                intent.putExtra("salario",salario);
                intent.putExtra("tipoISR",opcionISR);
                startActivity(intent);
            }else{ // Boton de info
                Intent intent2 = new Intent(ReporteCalculos.this, TablasScreen.class);
                startActivity(intent2);
            }
        }
    };
    // Método para calcular el ISR basado en el salario
    public double calcularISR(double salario, int opc) {
        // Los rangos de cada tabla dependiendo el tipo de ISR
        CalculosISR[] tramos;
        switch (opc){
            case 1: {
                tramos = new CalculosISR[] {
                        new CalculosISR(0.01, 24.54, 0.00, 1.92),
                        new CalculosISR(24.55, 208.29, 0.47, 6.40),
                        new CalculosISR(208.30, 366.05, 12.23, 10.88),
                        new CalculosISR(366.06, 425.52, 29.40, 16.00),
                        new CalculosISR(425.53, 509.46, 38.91, 17.92),
                        new CalculosISR(509.47, 1027.52, 53.95, 21.36),
                        new CalculosISR(1027.53, 1619.51, 164.61, 23.52),
                        new CalculosISR(1619.52, 3091.90, 303.85, 30.00),
                        new CalculosISR(3091.91, 4122.54, 745.56, 32.00),
                        new CalculosISR(4122.55, 12367.62, 1075.37, 34.00),
                        new CalculosISR(12367.63, Double.MAX_VALUE, 3878.69, 35.00)
                };
                break;
            }
            case 2:{
                tramos = new CalculosISR[] {
                        new CalculosISR(0.01, 171.78, 0.00, 1.92),
                        new CalculosISR(171.79, 1458.03, 3.29, 6.40),
                        new CalculosISR(1458.04, 2562.35, 85.61, 10.88),
                        new CalculosISR(2562.36, 2978.64, 205.80, 16.00),
                        new CalculosISR(2978.65, 3566.22, 272.37, 17.92),
                        new CalculosISR(3566.23, 7192.64, 377.65, 21.36),
                        new CalculosISR(7192.65, 11336.57, 1152.27, 23.52),
                        new CalculosISR(11336.58, 21643.30, 2126.95, 30.00),
                        new CalculosISR(21643.31, 28857.78, 5218.92, 32.00),
                        new CalculosISR(28857.79, 86573.34, 7527.59, 34.00),
                        new CalculosISR(86573.35, Double.MAX_VALUE, 27150.83, 35.00)
                };

                break;
            }
            case 3:{
                tramos = new CalculosISR[] {
                        new CalculosISR(0.01, 245.40, 0.00, 1.92),
                        new CalculosISR(245.41, 2082.90, 4.70, 6.40),
                        new CalculosISR(2082.91, 3660.50, 122.30, 10.88),
                        new CalculosISR(3660.51, 4255.20, 294.00, 16.00),
                        new CalculosISR(4255.21, 5094.60, 389.10, 17.92),
                        new CalculosISR(5094.61, 10275.20, 539.50, 21.36),
                        new CalculosISR(10275.21, 16195.10, 1646.10, 23.52),
                        new CalculosISR(16195.11, 30919.00, 3038.50, 30.00),
                        new CalculosISR(30919.01, 41225.40, 7455.60, 32.00),
                        new CalculosISR(41225.41, 123676.20, 10753.70, 34.00),
                        new CalculosISR(123676.21, Double.MAX_VALUE, 38786.90, 35.00)
                };
                break;
            }
            case 4:{
                tramos = new CalculosISR[] {
                        new CalculosISR(0.01, 368.10, 0.00, 1.92),
                        new CalculosISR(368.11, 3124.35, 7.05, 6.40),
                        new CalculosISR(3124.36, 5490.75, 183.45, 10.88),
                        new CalculosISR(5490.76, 6382.80, 441.00, 16.00),
                        new CalculosISR(6382.81, 7641.90, 583.65, 17.92),
                        new CalculosISR(7641.91, 15412.80, 809.25, 21.36),
                        new CalculosISR(15412.81, 24292.65, 2469.15, 23.52),
                        new CalculosISR(24292.66, 46378.50, 4557.75, 30.00),
                        new CalculosISR(46378.51, 61838.10, 11183.40, 32.00),
                        new CalculosISR(61838.11, 185514.30, 16130.55, 34.00),
                        new CalculosISR(185514.31, Double.MAX_VALUE, 58180.35, 35.00)
                };
                break;
            }
            case 5:{
                tramos = new CalculosISR[] {
                        new CalculosISR(0.01, 746.04, 0.00, 1.92),
                        new CalculosISR(746.05, 6332.05, 14.32, 6.40),
                        new CalculosISR(6332.06, 11128.01, 371.83, 10.88),
                        new CalculosISR(11128.02, 12935.82, 893.63, 16.00),
                        new CalculosISR(12935.83, 15487.71, 1182.88, 17.92),
                        new CalculosISR(15487.72, 31236.49, 1640.18, 21.36),
                        new CalculosISR(31236.50, 49233.00, 5004.12, 23.52),
                        new CalculosISR(49233.01, 93993.90, 9236.89, 30.00),
                        new CalculosISR(93993.91, 125325.20, 22665.17, 32.00),
                        new CalculosISR(125325.21, 375975.61, 32691.18, 34.00),
                        new CalculosISR(375975.62, Double.MAX_VALUE, 117912.32, 35.00)
                };
                break;
            }
            default:
                tramos = new CalculosISR[] {
                        new CalculosISR(0.01, 24.54, 0.00, 1.92),
                        new CalculosISR(24.55, 208.29, 0.47, 6.40),
                        new CalculosISR(208.30, 366.05, 12.23, 10.88),
                        new CalculosISR(366.06, 425.52, 29.40, 16.00),
                        new CalculosISR(425.53, 509.46, 38.91, 17.92),
                        new CalculosISR(509.47, 1027.52, 53.95, 21.36),
                        new CalculosISR(1027.53, 1619.51, 164.61, 23.52),
                        new CalculosISR(1619.52, 3091.90, 303.85, 30.00),
                        new CalculosISR(3091.91, 4122.54, 745.56, 32.00),
                        new CalculosISR(4122.55, 12367.62, 1075.37, 34.00),
                        new CalculosISR(12367.63, Double.MAX_VALUE, 3878.69, 35.00)
                };
                break;
        }
        // Buscar en qué tramo cae el salario
        for (CalculosISR tramo : tramos) {
            if (salario >= tramo.limiteInferior && salario <= tramo.limiteSuperior) {

                formatoPorcentaje.setMinimumFractionDigits(2);

                // Asignación de datos en la vista del reporte
                limite.setText(formato.format(tramo.limiteInferior));
                cuota.setText(formato.format(tramo.cuotaFija));
                tasa.setText(formatoPorcentaje.format(tramo.porcentajeExcedente / 100));

                // Calcular el ISR para este tramo
                double excedente = salario - tramo.limiteInferior;
                // Asignar Diferencia
                diferencia.setText(formato.format(excedente));
                impuestoM.setText(formato.format(excedente * tramo.porcentajeExcedente / 100));

                return tramo.cuotaFija + (excedente * tramo.porcentajeExcedente / 100);
            }
        }

        // En caso de que el salario no caiga en ningún tramo (aunque siempre debería)
        return 0.0;
    }

    // CALCULO DE SUBSIDIO AL EMPLEO
    public double calcularSubsidio(double salario, int opc) {
        // Los rangos de cada tabla dependiendo el tipo de ISR
        SubsidioEmpleo[] tramos;
        switch (opc){
            case 1: {
                tramos = new SubsidioEmpleo[]{
                        new SubsidioEmpleo(0.01, 58.19, 13.39),
                        new SubsidioEmpleo(58.20, 87.28, 13.38),
                        new SubsidioEmpleo(87.29, 114.24, 13.38),
                        new SubsidioEmpleo(114.25, 116.38, 12.92),
                        new SubsidioEmpleo(116.39, 146.25, 12.58),
                        new SubsidioEmpleo(146.26, 155.17, 11.65),
                        new SubsidioEmpleo(155.18, 175.51, 10.69),
                        new SubsidioEmpleo(175.52, 204.76, 9.69),
                        new SubsidioEmpleo(204.77, 234.01, 8.34),
                        new SubsidioEmpleo(234.02, 242.84, 7.16),
                        new SubsidioEmpleo(242.85, Double.MAX_VALUE, 0.00)
                };
                break;
            }
            case 2:{
                tramos = new SubsidioEmpleo[]{
                        new SubsidioEmpleo(0.01, 407.33, 93.73),
                        new SubsidioEmpleo(407.34, 610.96, 93.66),
                        new SubsidioEmpleo(610.97, 799.68, 93.66),
                        new SubsidioEmpleo(799.69, 814.66, 90.44),
                        new SubsidioEmpleo(814.67, 1023.75, 88.06),
                        new SubsidioEmpleo(1023.76, 1086.19, 81.55),
                        new SubsidioEmpleo(1086.20, 1228.57, 74.83),
                        new SubsidioEmpleo(1228.58, 1433.32, 67.83),
                        new SubsidioEmpleo(1433.33, 1638.07, 58.38),
                        new SubsidioEmpleo(1638.08, 1699.88, 50.12),
                        new SubsidioEmpleo(1699.89, Double.MAX_VALUE, 0.00) // Para "En adelante"
                };
                break;
            }
            case 3:{
                tramos = new SubsidioEmpleo[]{
                        new SubsidioEmpleo(0.01, 581.90, 133.90),
                            new SubsidioEmpleo(581.91, 872.80, 133.80),
                            new SubsidioEmpleo(872.81, 1142.40, 133.80),
                            new SubsidioEmpleo(1142.41, 1163.80, 129.20),
                            new SubsidioEmpleo(1163.81, 1462.50, 125.80),
                            new SubsidioEmpleo(1462.51, 1551.70, 116.50),
                            new SubsidioEmpleo(1551.71, 1755.10, 106.90),
                            new SubsidioEmpleo(1755.11, 2047.60, 96.90),
                            new SubsidioEmpleo(2047.61, 2340.10, 83.40),
                            new SubsidioEmpleo(2340.11, 2428.40, 71.60),
                            new SubsidioEmpleo(2428.41, Double.MAX_VALUE, 0.00)
                };
                break;
            }
            case 4:{
                tramos = new SubsidioEmpleo[] {
                    new SubsidioEmpleo(0.01, 872.85, 200.85),
                            new SubsidioEmpleo(872.86, 1309.20, 200.70),
                            new SubsidioEmpleo(1309.21, 1713.60, 200.70),
                            new SubsidioEmpleo(1713.61, 1745.70, 193.80),
                            new SubsidioEmpleo(1745.71, 2193.75, 188.70),
                            new SubsidioEmpleo(2193.76, 2327.55, 174.75),
                            new SubsidioEmpleo(2327.56, 2632.65, 160.35),
                            new SubsidioEmpleo(2632.66, 3071.40, 145.35),
                            new SubsidioEmpleo(3071.41, 3510.15, 125.10),
                            new SubsidioEmpleo(3510.16, 3642.60, 107.40),
                            new SubsidioEmpleo(3642.61, Double.MAX_VALUE, 0.00)
                };
                break;
            }
            case 5:{
                tramos = new SubsidioEmpleo[] {
                        new SubsidioEmpleo(0.01, 1768.96, 407.02),
                        new SubsidioEmpleo(1768.97, 2653.38, 406.83),
                        new SubsidioEmpleo(2653.39, 3472.84, 406.62),
                        new SubsidioEmpleo(3472.85, 3537.87, 392.77),
                        new SubsidioEmpleo(3537.88, 4446.15, 382.46),
                        new SubsidioEmpleo(4446.16, 4717.18, 354.23),
                        new SubsidioEmpleo(4717.19, 5335.42, 324.87),
                        new SubsidioEmpleo(5335.43, 6224.67, 294.63),
                        new SubsidioEmpleo(6224.68, 7113.90, 253.54),
                        new SubsidioEmpleo(7113.91, 7382.33, 217.61),
                        new SubsidioEmpleo(7382.34, Double.MAX_VALUE, 0.00)
                };
                break;
            }
            default:
                tramos = new SubsidioEmpleo[]{
                        new SubsidioEmpleo(0.01, 58.19, 13.39),
                        new SubsidioEmpleo(58.20, 87.28, 13.38),
                        new SubsidioEmpleo(87.29, 114.24, 13.38),
                        new SubsidioEmpleo(114.25, 116.38, 12.92),
                        new SubsidioEmpleo(116.39, 146.25, 12.58),
                        new SubsidioEmpleo(146.26, 155.17, 11.65),
                        new SubsidioEmpleo(155.18, 175.51, 10.69),
                        new SubsidioEmpleo(175.52, 204.76, 9.69),
                        new SubsidioEmpleo(204.77, 234.01, 8.34),
                        new SubsidioEmpleo(234.02, 242.84, 7.16),
                        new SubsidioEmpleo(242.85, Double.MAX_VALUE, 0.00)
                };
                break;
        }

        for (SubsidioEmpleo tramo : tramos) {
            if (salario >= tramo.limiteInferior && salario <= tramo.limiteSuperior) {
                return tramo.subsidio;
            }
        }
        // En caso de que no haya subsidio aplicable
        return 0.0;
    }
}
