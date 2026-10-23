package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Ejercicio_03_04_01 {
	
    public static void main(String[] args) {
    	
    	String url = "jdbc:mysql://localhost/dbnotas";
        String usuario = "birt";
        String contrasena = "birt";
        
        String informacion_prepared_statement = "SELECT dni, apenom FROM alumnos ORDER BY apenom DESC";
        String informacion_prepared_statement_2 = "SELECT abreviatura, nota FROM notas n, asignaturas a WHERE dni= ? and a.cod=n.cod";
        
        
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena); PreparedStatement acceso = conexion.prepareStatement(informacion_prepared_statement); ResultSet resultado_acceso = acceso.executeQuery()) {
            while (resultado_acceso.next()) {
                String dni = resultado_acceso.getString("dni");
                System.out.println("\n" + resultado_acceso.getString("apenom"));
                System.out.println("-----------------------------------------");
                
                try (PreparedStatement acceso_2 = conexion.prepareStatement(informacion_prepared_statement_2)) {
                	acceso_2.setString(1, dni);
                    try (ResultSet resultado_acceso_2 = acceso_2.executeQuery()) {
                        while (resultado_acceso_2.next()) {
                            System.out.println(resultado_acceso_2.getString(1) + "\t\t" + resultado_acceso_2.getInt(2));
                        }
                    }
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

}