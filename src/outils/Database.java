package outils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/booking";
    private static final String USER = "root";         // change si tu utilises un autre utilisateur
    private static final String PASSWORD = "";         // ajoute ton mot de passe MySQL si besoin

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
