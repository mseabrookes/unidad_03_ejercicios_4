package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ejercicio_03_04_03 {

    public static void main(String[] args) {
    	
    	String url = "jdbc:mysql://localhost/dbnotas";
        String usuario = "birt";
        String contrasena = "birt";
        
        String dni_alumno;
        int codigo_asignatura;
        int nota_notas_actualizada;
        
        String informacion_prepared_statement = "SELECT apenom FROM alumnos WHERE dni= ?";
        String informacion_prepared_statement_2 = "SELECT cod, nombre, abreviatura from asignaturas";
        //Tengo la duda de si este segundo prepared statement se podría reeplazar por un statement normal, al carecer de "?"
        String informacion_prepared_statement_3 = "SELECT nota from notas WHERE dni=? and cod=?";
        String informacion_prepared_statement_4 = "UPDATE notas SET nota =  ? WHERE dni= ? and cod = ?";
        String informacion_prepared_statement_5 = "INSERT INTO notas (dni, cod, nota) VALUES (?,?,?)";
    	
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
            	try (PreparedStatement acceso_3 = conexion.prepareStatement(informacion_prepared_statement_3)) {
                	acceso_3.setString(1, dni_alumno);
                	acceso_3.setInt(2, codigo_asignatura);
                    try (ResultSet resultado_acceso_3 = acceso_3.executeQuery()) {
                        if (resultado_acceso_3.next()) {
                            try (PreparedStatement acceso_4 = conexion.prepareStatement(informacion_prepared_statement_4)) {
                            	acceso_4.setInt(1, nota_notas_actualizada);
                            	acceso_4.setString(2, dni_alumno);
                            	acceso_4.setInt(3, codigo_asignatura);
                            	acceso_4.executeUpdate();
                            	
                                System.out.println("La nota de la asignatura cuyo código es " + codigo_asignatura + " perteneciente al alumno cuyo DNI es " + dni_alumno + " ha sido actualizada.");
                                System.out.println("Nueva nota: " + nota_notas_actualizada);
                            } catch (Exception e) {
                            	e.printStackTrace();
                            }
                        } else {
                        	try (PreparedStatement acceso_5 = conexion.prepareStatement(informacion_prepared_statement_5)) {
                        		acceso_5.setString(1, dni_alumno);
                        		acceso_5.setInt(2, codigo_asignatura);
                        		acceso_5.setInt(3, nota_notas_actualizada);
                        		acceso_5.executeUpdate();
                        		System.out.println("La nota de la asignatura cuyo código es " + codigo_asignatura + " perteneciente al alumno cuyo DNI es " + dni_alumno + " ha sido añadida.");
                                System.out.println("Nueva nota: " + nota_notas_actualizada);
                            } catch (Exception e) {
                            	e.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}