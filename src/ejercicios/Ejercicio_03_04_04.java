package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class Ejercicio_03_04_04 {

    public static void main(String[] args) {
    	
    	String url = "jdbc:mysql://localhost/dbnotas";
        String usuario = "birt";
        String contrasena = "birt";
        
        String dni_alumno;
        int codigo_asignatura;
        int nota_notas_actualizada;
        
        String informacion_prepared_statement = "SELECT apenom FROM alumnos WHERE dni= ?";
        String informacion_prepared_statement_2 = "SELECT cod, nombre, abreviatura from asignaturas";
        String informacion_callable_statement = "{? = call insertar_nota(?,?,?)}";
        //"? = " es para mostrar el resultado de la operación.
    	
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena); PreparedStatement acceso = conexion.prepareStatement(informacion_prepared_statement)) {
            System.out.println("Por favor, introduzca el DNI del alumno cuya nota desea añadir o modificar:");
            dni_alumno = Consola.readString();
            acceso.setString(1, dni_alumno);
            
            try(ResultSet resultado_acceso = acceso.executeQuery()){
            	if (resultado_acceso.next()) {
            		System.out.println(resultado_acceso.getString("apenom"));
                }
            	
            } catch (Exception e) {
            	e.printStackTrace();
            }
            
            try (PreparedStatement acceso_2 = conexion.prepareStatement(informacion_prepared_statement_2); ResultSet resultado_acceso_2 = acceso_2.executeQuery()) {
            	while (resultado_acceso_2.next()) {
                    System.out.println(resultado_acceso_2.getInt(1) + "-. " + resultado_acceso_2.getString(2) + " (" + resultado_acceso_2.getString(3) + ")");
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }

            System.out.println("Por favor, introduzca el código de la asignatura cuya nota desea añadir o modificar:");
            codigo_asignatura = Consola.readInt();

            System.out.println("Por favor, introduzca la nueva nota:");
            nota_notas_actualizada = Consola.readInt();
            
            if(nota_notas_actualizada >= 0 && nota_notas_actualizada <= 10) {
            	try (CallableStatement acceso_3 = conexion.prepareCall(informacion_callable_statement)) {
            		acceso_3.registerOutParameter(1, java.sql.Types.VARCHAR);//siembro
            		acceso_3.setInt(2, codigo_asignatura);
            		acceso_3.setString(3, dni_alumno);
            		acceso_3.setInt(4, nota_notas_actualizada);
            		acceso_3.execute();
            		
                    String resultado_informacion_callable_statement = acceso_3.getString(1);//cosecho
                    
                    System.out.println(resultado_informacion_callable_statement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}