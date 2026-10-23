package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Ejercicio_03_04_02 {

    public static void main(String[] args) {
    	
    	String url = "jdbc:mysql://localhost/dbnotas";
        String usuario = "birt";
        String contrasena = "birt";
        
        String dni_alumno;
        String nombre_alumno;
        String nombre_alumno_actualizado;
        
        String informacion_prepared_statement = "SELECT apenom FROM alumnos WHERE dni= ?";
        String informacion_prepared_statement_2 = "UPDATE alumnos SET apenom = ? WHERE dni= ?";
        
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena); PreparedStatement acceso = conexion.prepareStatement(informacion_prepared_statement)) {
        	System.out.println("Por favor, introduzca el DNI del alumno cuyo nombre desea modificar:");
            dni_alumno = Consola.readString();
            acceso.setString(1, dni_alumno);
            try (ResultSet resultado_acceso = acceso.executeQuery()) {
            	if (resultado_acceso.next()) {
                	nombre_alumno = resultado_acceso.getString("apenom");
                	System.out.println("Nombre del alumno cuyo DNI es " + dni_alumno + " :" + nombre_alumno);
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }

            System.out.println("Por favor, introduzca el nuevo nombre del alumno:");
            nombre_alumno_actualizado = Consola.readString();

            if (!nombre_alumno_actualizado.isEmpty()) {
            	try (PreparedStatement acceso_2 = conexion.prepareStatement(informacion_prepared_statement_2)) {
            		acceso_2.setString(1, nombre_alumno_actualizado);
            		acceso_2.setString(2, dni_alumno);
            		acceso_2.executeUpdate();
                } catch (Exception e) {
                	e.printStackTrace();
                }
            	
            	System.out.println("Alumno modificado correctamente");
            	System.out.println("El nombre del alumno cuyo DNI es " + dni_alumno + " ha sido actualizado.");
            	System.out.println("Nuevo nombre del alumno: " + nombre_alumno_actualizado);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}