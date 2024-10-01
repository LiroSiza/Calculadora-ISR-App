package com.example.calculadoraisr_2024;

public class CalculosISR {
    double limiteInferior;
    double limiteSuperior;
    double cuotaFija;
    double porcentajeExcedente;

    // Constructor
    CalculosISR(double limiteInferior, double limiteSuperior, double cuotaFija, double porcentajeExcedente) {
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.cuotaFija = cuotaFija;
        this.porcentajeExcedente = porcentajeExcedente;
    }
}
