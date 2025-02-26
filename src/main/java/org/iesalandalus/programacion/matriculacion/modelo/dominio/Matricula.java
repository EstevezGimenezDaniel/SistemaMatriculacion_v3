package org.iesalandalus.programacion.matriculacion.modelo.dominio;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

public class Matricula {
    public static final int MAXIMO_MESES_ANTERIOR_ANULACION = 6;
    public static final int MAXIMO_DIAS_ANTERIOR_MATRICULA = 15;
    public static final int MAXIMO_NUMERO_HORAS_MATRICULA = 1000;
    public static final int MAXIMO_NUMERO_ASIGNATURAS_POR_MATRICULA = 10;
    private static final String ER_CURSO_ACADEMICO = "\\d{2}-\\d{2}";
    public static final String FORMATO_FECHA = "dd-MM-yyyy";
    private int idMatricula;
    private String cursoAcademico;
    private LocalDate fechaMatriculacion;
    private LocalDate fechaAnulacion;
    private Alumno alumno;
    private ArrayList<Asignatura> coleccionAsignaturas;

    public Matricula(int idMatricula, String cursoAcademico, LocalDate fechaMatriculacion, Alumno alumno,
                     ArrayList<Asignatura> coleccionAsignaturas) throws OperationNotSupportedException {
        setIdMatricula(idMatricula);
        setCursoAcademico(cursoAcademico);
        setFechaMatriculacion(fechaMatriculacion);
        setAlumno(alumno);
        setColeccionAsignaturas(coleccionAsignaturas);
    }
    public Matricula(Matricula matricula) throws OperationNotSupportedException {
        if (matricula==null) {
            throw new NullPointerException("ERROR: No es posible copiar una matrícula nula.");
        }
        setIdMatricula(matricula.getIdMatricula());
        setCursoAcademico(matricula.getCursoAcademico());
        setFechaMatriculacion(matricula.getFechaMatriculacion());
        setAlumno(matricula.getAlumno());
        setColeccionAsignaturas(matricula.getColeccionAsignaturas());
    }
    public int getIdMatricula() {
        return idMatricula;
    }
    public void setIdMatricula(int idMatricula) {
        if (idMatricula <= 0) {
            throw new IllegalArgumentException("ERROR: El identificador de una matr?cula no puede ser menor o igual a 0.");
        }
        this.idMatricula = idMatricula;
    }
    public String getCursoAcademico() {
        return cursoAcademico;
    }
    public void setCursoAcademico(String cursoAcademico) {
        if (cursoAcademico==null) {
            throw new NullPointerException("ERROR: El curso acad?mico de una matr?cula no puede ser nulo.");
        }
        if (cursoAcademico.isBlank()) {
            throw new IllegalArgumentException("ERROR: El curso acad?mico de una matr?cula no puede estar vac?o.");
        }
        if ( cursoAcademico.isEmpty()) {
            throw new IllegalArgumentException("ERROR: El curso acad?mico de una matr?cula no puede estar vac?o.");
        }
        if (!cursoAcademico.matches(ER_CURSO_ACADEMICO)) {
            throw new IllegalArgumentException("ERROR: El formato del curso acad?mico no es correcto.");
        }
        this.cursoAcademico = cursoAcademico;
    }

    public LocalDate getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(LocalDate fechaMatriculacion) {
        if (fechaMatriculacion == null) {
            throw new NullPointerException("ERROR: La fecha de matriculaci?n de una m?tricula no puede ser nula.");
        }
        if (fechaMatriculacion.isAfter(LocalDate.now().plusDays(MAXIMO_DIAS_ANTERIOR_MATRICULA))) {
            throw new IllegalArgumentException("La fecha de matriculaci?n no puede superar los 15 d?as de retraso.");
        }
        if (fechaMatriculacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("ERROR: La fecha de matriculaci?n no puede ser posterior a hoy.");
        }
        if (fechaMatriculacion.isBefore(LocalDate.now().minusDays(MAXIMO_DIAS_ANTERIOR_MATRICULA))) {
            throw new IllegalArgumentException("ERROR: La fecha de matriculaci?n no puede ser anterior a 15 d?as.");
        }
        this.fechaMatriculacion = fechaMatriculacion;
    }

    public LocalDate getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(LocalDate fechaAnulacion) {

        if (fechaAnulacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("ERROR: La fecha de anulaci?n de una matr?cula no puede ser posterior a hoy.");
        }
        if (fechaAnulacion.isBefore(fechaMatriculacion)) {
            throw new IllegalArgumentException("ERROR: La fecha de anulaci?n no puede ser anterior a la fecha de matriculaci?n.");
        }

        if (fechaAnulacion.isAfter(fechaMatriculacion.plusMonths(MAXIMO_MESES_ANTERIOR_ANULACION))) {
            throw new IllegalArgumentException("ERROR: La edad del alumno debe ser mayor o igual a 16 a?os");
        }
        long mesesDeDiferencia = ChronoUnit.MONTHS.between(fechaAnulacion, LocalDate.now());
        if (mesesDeDiferencia>=MAXIMO_MESES_ANTERIOR_ANULACION) {
            throw new IllegalArgumentException("ERROR: La fecha de anulaci?n debe ser anterior a 6 meses");
        }

        this.fechaAnulacion = fechaAnulacion;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno)  {
        if (alumno == null) {
            throw new NullPointerException("ERROR: El alumno de una matr?cula no puede ser nulo.");
        }

        this.alumno = alumno;
    }

    public ArrayList<Asignatura> getColeccionAsignaturas() {
        return new ArrayList<>(this.coleccionAsignaturas);
    }


    public void setColeccionAsignaturas(ArrayList<Asignatura> coleccionAsignaturas) throws OperationNotSupportedException {

        if (coleccionAsignaturas==null) {
            throw new NullPointerException("ERROR: La lista de asignaturas de una matr?cula no puede ser nula.");
        }
        if (coleccionAsignaturas.size() > MAXIMO_NUMERO_ASIGNATURAS_POR_MATRICULA) {
            throw new IllegalArgumentException("ERROR: El n?mero de asignaturas no puede superar el m?ximo permitido");
        }
        if (superaMaximoNumeroHorasMatricula(coleccionAsignaturas)) {
            throw new OperationNotSupportedException("ERROR: No se puede realizar la matrícula ya que supera el máximo de horas permitidas ("
                    + Matricula.MAXIMO_NUMERO_HORAS_MATRICULA + " horas).");
        }
        this.coleccionAsignaturas = new ArrayList<>();
        for (Asignatura a : coleccionAsignaturas) {
            if(a == null) continue;
            this.coleccionAsignaturas.add(new Asignatura(a));
        }
        this.coleccionAsignaturas = new ArrayList<>();
        for (Asignatura a : coleccionAsignaturas) {
            if(a == null) continue;
            this.coleccionAsignaturas.add(new Asignatura(a));
        }
    }

    private boolean superaMaximoNumeroHorasMatricula(ArrayList<Asignatura> asignaturasMatricula) {
        int horasMatricula = 0;
        for (Asignatura a : asignaturasMatricula) {
            if (a != null) {
                horasMatricula += a.getHorasAnuales();
            }
        }
        return horasMatricula > MAXIMO_NUMERO_HORAS_MATRICULA;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matricula matricula = (Matricula) o;
        return idMatricula == matricula.idMatricula;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMatricula);
    }

    private String asignaturasMatricula() {
        if (coleccionAsignaturas.size()== 0) {
            return "Sin asignaturas";
        }
        StringBuilder asignaturasTexto = new StringBuilder();
        for (Asignatura asignatura : coleccionAsignaturas) {
            if (asignatura != null) {
                asignaturasTexto.append(asignatura.imprimir()).append(", ");
            }
        }
        return asignaturasTexto.toString();
    }
    public String imprimir() {
        return "idMatricula=" + getIdMatricula() + ", " + "curso académico=" + getCursoAcademico() + ", "
                + "fecha matriculación=" + getFechaMatriculacion().format(DateTimeFormatter.ofPattern(FORMATO_FECHA))
                + ", " + "alumno=" + "{" + getAlumno().imprimir() + "}";

    }
    public String toString() {
        if (fechaAnulacion == null) {
            return "idMatricula=" + getIdMatricula() + ", " + "curso académico=" + getCursoAcademico() + ", "
                    + "fecha matriculación="
                    + getFechaMatriculacion().format(DateTimeFormatter.ofPattern(FORMATO_FECHA)) + ", " + "alumno="
                    + getAlumno().imprimir() + ", " + "Asignaturas=" + "{ " + asignaturasMatricula() + "}";

        } else {
            return "idMatricula=" + getIdMatricula() + ", " + "curso académico=" + getCursoAcademico() + ", "
                    + "fecha matriculación="
                    + getFechaMatriculacion().format(DateTimeFormatter.ofPattern(FORMATO_FECHA)) + ", "
                    + "fecha anulación=" + getFechaAnulacion().format(DateTimeFormatter.ofPattern(FORMATO_FECHA)) + ", "
                    + "alumno=" + getAlumno().imprimir() + ", " + "Asignaturas=" + "{ " + asignaturasMatricula() + "}";
        }
    }
}