package com.groep2.Parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class DatabaseHandler {
    static Connection conn = null;
    static Statement stmt;

    static void connect() throws SQLException {
        String url="jdbc:mysql://server.kevinswebsite.nl:3306/moviedatabase";

        //Probeer een database connectie te maken
        try {
            conn = DriverManager.getConnection( url, "root", "fietsbel" );
            System.out.println("database connection success.");
        } catch (SQLException e) {
            System.out.println("database connection failed.");
        }
        stmt = conn.createStatement();
    }
    static void createTables() throws SQLException {
        //Reset database
        String sql = "DROP TABLE IF EXISTS ratings, locations, soundtracks, genres, movies;";
        stmt.executeUpdate(sql);
        System.out.println("deleted all tables.");

        sql = "CREATE TABLE IF NOT EXISTS ratings (" +
                "votes INT," +
                "rating FLOAT," +
                "movietitle VARCHAR(999)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new ratings table.");

        sql = "CREATE TABLE IF NOT EXISTS locations (" +
                "movietitle VARCHAR(999)," +
                "releaseyear VARCHAR(4)," +
                "location VARCHAR(999)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new locations table.");

        List<String> addToDatabase = Arrays.asList("locations","ratings");
        writeToDB(addToDatabase);

    }

    static void writeToDB(List<String> csvFiles) throws SQLException {

        //Ga door elke csvFile
        for (int i=0; i<csvFiles.size(); i++){
            String csvFile = "Parser/src/main/resources/output/"+ csvFiles.get(i) + ".csv";

            BufferedReader br = null;
            String line = "";
            String add = "";
            StringBuilder query = new StringBuilder("INSERT INTO " + csvFiles.get(i) + " VALUES ");

            try {

                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {

                    //Komma seperate
                    String[] current = line.split(",&");


                    //locations file
                    if(csvFiles.get(i).equals("locations")){
                        String str1 =  current[0].replaceAll("\"", "").replaceAll("\'","").replaceAll(",","").trim();
                        String str2 =  current[1].replaceAll("\"", "").replaceAll("\'","").replaceAll(",","").trim();
                        String str3 =  current[2].replaceAll("\"", "").replaceAll("\'","").replaceAll(",","").trim();
                        add = "(\""+str1+"\",\""+str2+"\",\""+str3+"\"),";
                        query.append(add);
                    }
                    //movies file
                    else if(csvFiles.get(i).equals("movies")){

                    }
                    //Ratings file
                    else if(csvFiles.get(i).equals("ratings")){
                        String str =  current[2].replaceAll("\"", "").replaceAll("\'","").replaceAll(",","").trim();
                        add = "("+current[0]+","+current[1]+",\""+str+"\"),";
                        query.append(add);
                    }
                    //soundtracks file
                    else if(csvFiles.get(i).equals("soundtracks")){

                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            query.deleteCharAt(query.length()-1);
            query.append(";");

            stmt.executeLargeUpdate(query.toString());
            System.out.println(csvFiles.get(i)+" exported to database.");

        }
        conn.close();
        stmt.close();

    }
}
