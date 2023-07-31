package io.github.mateuszlubian00.itemcompare;

import java.sql.SQLException;

public class Main {

    /** Dummy main method and entry point, useful for packaging the application.
     *  Without this method, you would get an error that the package can't find javafx modules.
     */
    public static void main(String[] args) {
        try {
            ComparatorApplication.main(args);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
