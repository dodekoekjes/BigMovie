package com.groep2.Parser;

import java.io.*;
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
        System.out.println("database rebuild started.\nDO NOT STOP THE PARSER AFTER THIS MESSAGE!\ndeleted all tables.");

        sql = "CREATE TABLE IF NOT EXISTS ratings (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "votes INT," +
                "rating FLOAT," +
                "movietitle VARCHAR(999)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new ratings table.");

        sql = "CREATE TABLE IF NOT EXISTS locations (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "movietitle VARCHAR(999)," +
                "releaseyear VARCHAR(13)," +
                "fulllocation VARCHAR(999)," +
                "country VARCHAR(999)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new locations table.");

        sql = "CREATE TABLE IF NOT EXISTS movies (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "movietitle VARCHAR(999)," +
                "startyear VARCHAR(13)," +
                "endyear VARCHAR(13)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new movies table.");

        sql = "CREATE TABLE IF NOT EXISTS soundtracks (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "movietitle VARCHAR(999)," +
                "releaseyear VARCHAR(999)," +
                "song VARCHAR(999)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new soundtracks table.");

        sql = "CREATE TABLE IF NOT EXISTS genres (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "movietitle VARCHAR(999)," +
                "releaseyear VARCHAR(999)," +
                "genre VARCHAR(999)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new genres table.");

        List<String> addToDatabase = Arrays.asList("genres", "ratings", "locations", "soundtracks", "movies");
        writeToDB(addToDatabase);
    }

    static void writeToDB(List<String> csvFiles) throws SQLException {

        //Ga door elke csvFile
        for (int i=0; i<csvFiles.size(); i++){
            System.out.println("building "+ csvFiles.get(i)+" query...");

            String csvFile = "Parser/src/main/resources/output/csv"+ csvFiles.get(i) + ".csv";
            BufferedReader br = null;
            String line = "";
            String add = "";
            StringBuilder query = new StringBuilder("INSERT INTO " + csvFiles.get(i) + " VALUES ");

            try {
                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {

                    //Komma seperate
                    String[] current = line.split(",&");

                    //genres file
                    if(csvFiles.get(i).equals("genres")){
                        //Maak ze schoon en voeg ze toe aan de query
                        add = "(NULL,\""+clean(current[0])+"\",\""+clean(current[1])+"\",\""+clean(current[2])+"\"),";
                        query.append(add);
                    }
                    //locations file
                    if(csvFiles.get(i).equals("locations")){
                        String cleanedLocation = clean(current[2].replaceAll("\\(.*?\\)",""));
                        String country = "";
                        String[] bits = cleanedLocation.split(",");
                        if (bits.length>1){
                            country=bits[bits.length-1];
                        }
                        else if(bits.length==0){
                            System.out.println("Empty country found somehow");
                        }
                        else {
                            country = bits[0];
                        }


                        add = "(NULL,\""+clean(current[0])+"\",\""+clean(current[1])+"\",\""+cleanedLocation+"\",\""+clean(country)+"\"),";
                        query.append(add);
                    }
                    //movies file
                    else if(csvFiles.get(i).equals("movies")){
                        add = "(NULL,\""+clean(current[0])+"\",\""+clean(current[1])+"\",\""+clean(current[2])+"\"),";
                        query.append(add);
                    }
                    //Ratings file
                    else if(csvFiles.get(i).equals("ratings")){
                        String str =  clean(current[2]);
                        add = "(NULL,"+current[0]+","+current[1]+",\""+str+"\"),";
                        query.append(add);
                    }
                    //soundtracks file
                    else if(csvFiles.get(i).equals("soundtracks")){
                        String movietitle = clean(current[0]);
                        String releaseyear = clean(current[1]);
                        for(int c=2; c<1000; c++){
                            try{
                                if(current[c]!=null){
                                    add = "(NULL,\""+movietitle+"\",\""+releaseyear+"\",\""+clean(current[c])+"\"),";
                                    query.append(add);
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException e){
                                break;
                            }
                        }
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
            System.out.println(csvFiles.get(i)+" query was build.\nexporting to database...");
            stmt.executeLargeUpdate(query.toString());
            System.out.println(csvFiles.get(i)+" exported to database.");

        }
        conn.close();
        stmt.close();
        System.out.println("database build finished and all connections closed.");
    }

    static String clean(String input){
        return input.replaceAll("\\)", "").replaceAll("\\(", "").replaceAll("\"", "").replaceAll("\'","").replaceAll(",","\\\\,").replaceAll("\\\\","").trim();
    }
}
