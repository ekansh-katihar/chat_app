package com.example.demo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import java.util.stream.Stream;

public class Utils {
     public static void main(String[] args) {
        Path filePath = Paths.get("path/to/your/file.txt"); // Replace with the path to your file
      
        try (Stream<String> linesStream = Files.lines(filePath)) {
            Iterator<String> iterator = linesStream.iterator();
            int chunkSize = 10;
            List<String> chunk;

            while ((chunk = readNextLines(iterator, chunkSize)).size() > 0) {
                chunk.forEach(System.out::println);
                System.out.println("------ End of Chunk ------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readNextLines(Iterator<String> iterator, int numberOfLines) {
        return Stream.generate(() -> null)
                     .takeWhile(x -> iterator.hasNext())
                     .map(n -> iterator.next())
                     .limit(numberOfLines)
                     .collect(Collectors.toList());
    }


    public static List<String> readFile(String path, int from, int till) {

    
        Path filePath = Paths.get(path); // Replace with the path to your file

        List<String> l = new ArrayList<>();

        try (Stream<String> linesStream = Files.lines(filePath)) {
            
            l = linesStream
                .skip(from ) // Skip lines before the start line (0-based index)
                .limit(till) // Limit to the number of lines we want to read
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }

public static List<String> readFromDB(int pageSize, int pageNumber) {
        List<String> l = new ArrayList<>();
        String URL = "jdbc:postgresql://pg:5432/postgres";
        String USER = "postgres";
        String PASSWORD = "password";
        String query = "SELECT * FROM poc.stock LIMIT ? OFFSET ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, pageSize);
            statement.setInt(2, pageNumber * pageSize);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String symbol = resultSet.getString("symbol");
                    double value = resultSet.getDouble("value");
                    l.add("Symbol: " + symbol + ", Value: " + value);
                }
            }catch (Exception e) {
            e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }
}
