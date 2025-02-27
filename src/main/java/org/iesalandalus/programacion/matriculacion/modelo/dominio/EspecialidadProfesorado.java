package org.iesalandalus.programacion.matriculacion.modelo.dominio;

public enum EspecialidadProfesorado {
    INFORMATICA("Informática"),
    SISTEMAS("Sistemas"),
    FOL("Fol");
    private String cadenaAMostrar;
    private EspecialidadProfesorado(String cadenaAMostrar) {
        this.cadenaAMostrar = cadenaAMostrar;
    }

    public String imprimir() {
        int digito = 0;
        if (cadenaAMostrar == INFORMATICA.cadenaAMostrar) {
            digito = 0;
        } else if (cadenaAMostrar == SISTEMAS.cadenaAMostrar) {
            digito = 1;
        } else {
            digito = 2;
        }
        return digito + ".-" + cadenaAMostrar;
    }

    @Override
    public String toString() {
        return "La especialidad seleccionada:" + cadenaAMostrar;
    }
}
