package io.github.mateuszlubian00.itemcompare;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            ComparatorApplication.main(args);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
