package com.groep2.Parser;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

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
        String sql = "DROP TABLE IF EXISTS " +
                //"ratings, " +
                "locations, " +
                "soundtracks " +
                //"genres," +
                //"movies" +
                ";";
        stmt.executeUpdate(sql);
        System.out.println("database rebuild started.\nDO NOT STOP THE PARSER AFTER THIS MESSAGE!\ndeleted all tables.");

        sql = "CREATE TABLE IF NOT EXISTS movies (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "movietitle VARCHAR(999)," +
                "INDEX (movietitle),"+
                "startyear VARCHAR(999)," +
                "endyear VARCHAR(999)," +
                "isSerie INT(1)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new movies table.");

        sql = "CREATE TABLE IF NOT EXISTS locations (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "movie_id INT REFERENCES movies(id)," +
                "fulllocation VARCHAR(999)," +
                "country VARCHAR(999)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new locations table.");

        sql = "CREATE TABLE IF NOT EXISTS genres (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "movie_id INT REFERENCES movies(id)," +
                "genre VARCHAR(999)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new genres table.");

        sql = "CREATE TABLE IF NOT EXISTS soundtracks (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "movie_id INT REFERENCES movies(id)," +
                "soundtrack VARCHAR(999)" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new soundtracks table.");

        sql = "CREATE TABLE IF NOT EXISTS ratings (" +
                "id INT AUTO_INCREMENT KEY,"+
                "INDEX (id),"+
                "movie_id INT REFERENCES movies(id)," +
                "votes INT(99)," +
                "rating DOUBLE" +
                ");";
        stmt.executeUpdate(sql);
        System.out.println("created new ratings table.");

        List<String> addToDatabase = Arrays.asList("locations");
        writeToDB(addToDatabase);
    }

    static void writeToDB(List<String> csvFiles) throws SQLException {

        //Ga door elke csvFile
        for (int i=0; i<csvFiles.size(); i++){
            System.out.println("building "+ csvFiles.get(i)+" query...");

            String csvFile = "Parser/src/main/resources/output/csv/"+ csvFiles.get(i) + ".csv";
            BufferedReader br = null;
            String line = "";
            String add = "";
            StringBuilder query = new StringBuilder("INSERT INTO " + csvFiles.get(i) + " VALUES ");

            //Nodig voor movies
            String title = "";
            String year1 = "";
            String year2 = "";

            //Nodig voor ratings
            int count = 1;
            int votes = 0;
            double rating = 0.0;
            int movie_id = 0;

            //Nodig voor genres
            StringBuilder items = new StringBuilder();

            //Skip alle "top" ratings/genres
            Boolean skipped = false;

            try {
                br = new BufferedReader(new FileReader(csvFile));

                while ((line = br.readLine()) != null) {
                    //Komma seperate
                    String[] current = line.split(",&");

                    //movies file
                    if(csvFiles.get(i).equals("movies") && !clean(current[0]).equals("")){
                        if(current[0].equals(title)){
                            try{
                                //Probeer als het meerdere met dezelfde naam zijn, de kleinste startjaar als jaar 1 te zetten.
                                if(parseInt(clean(current[1]))<parseInt(year1)){
                                    year1 = clean(current[1]);
                                }
                                if(parseInt(clean(current[2]))>parseInt(year2)){
                                    year2 = clean(current[2]);
                                }
                            }
                            catch(Exception e){
                                //System.out.println(clean(current[1]) + " is not an integer.");
                            }
                        }
                        else{
                            //Als de titel start met " is het een serie.
                            if(title.startsWith("\"")){
                                //Zet hem in de database als serie
                                query.append("\""+clean(year1)+"\",\""+clean(year2)+"\","+1+"),");
                            }
                            //Anders check je of het de eerste film is, zo niet, zet hem er in als film.
                            else if(!title.equals("")){
                                query.append("\""+clean(year1)+"\",\""+clean(year2)+"\","+0+"),");
                            }
                            query.append("(NULL,\""+clean(current[0])+"\",");
                            title = current[0];
                            year1 = clean(current[1]);
                            year2 = clean(current[2]);
                        }
                    }

                    //Ratings file
                    else if(csvFiles.get(i).equals("ratings")){
                        if (!skipped){
                            if(line.equals("25656,&1.4,&Kod Adi K.O.Z.")){
                                skipped = true;
                            }
                        }
                        else if(skipped){
                            //Als de titel hetzelfde is als de vorige
                            if(current[2].equals(title)){
                                count++;
                                votes = votes+parseInt(current[0]);
                                rating = rating+Double.parseDouble(current[1]);
                            }
                            //Anders plaats je de vorige in de database en maak je een nieuwe set aan
                            else{
                                if (!title.equals("")){
                                    Double averageRating = round((rating/count),1);
                                    add = "(NULL,"+movie_id+","+votes+","+averageRating+"),";
                                    query.append(add);
                                    if(movie_id % 2000 == 0){
                                        System.out.println("Now at movie ID: "+movie_id);
                                        Double percentage = round((((movie_id*1.0)/1324000)*100),2);
                                        System.out.println(percentage +"% done with ratings.csv");
                                    }
                                }

                                //Reset gegevens
                                count=1;
                                String sql = "SELECT id FROM movies WHERE movietitle = \""+clean(current[2])+"\"";
                                ResultSet rs = stmt.executeQuery(sql);
                                if(rs.next()) {
                                    movie_id = rs.getInt("id");
                                }
                                title = current[2];
                                votes = parseInt(current[0]);
                                rating = Double.parseDouble(current[1]);
                            }
                        }
                    }

                    //genres file
                    else if(csvFiles.get(i).equals("genres")){
                        if (!skipped){
                            if(line.equals("Internet  Movie  Database  Ltd,&IMDb,&. While every effort has been")){
                                skipped = true;
                            }
                        }
                        else if(skipped){
                            //Als de titel hetzelfde is als de vorige
                            if(current[0].equals(title)){
                                //Als dit genre nog niet in de lijst staat
                                if(items.indexOf(current[2])==-1){
                                    //Voeg hem dan toe aan de lijst met genres
                                    items.append(current[2]+" ");
                                }
                            }
                            //Anders plaats je de vorige in de database en maak je een nieuwe set aan
                            else{
                                if (!title.equals("")){
                                    add = "(NULL,"+movie_id+",\""+clean(items.toString())+"\"),";
                                    query.append(add);
                                    if(movie_id % 2000 == 0){
                                        System.out.println("Now at movie ID: "+movie_id);
                                        Double percentage = round((((movie_id*1.0)/1324000)*100),2);
                                        System.out.println(percentage +"% done with genres.csv");
                                    }
                                }
                                //Reset gegevens
                                String sql = "SELECT id FROM movies WHERE movietitle = \""+clean(current[0])+"\"";
                                ResultSet rs = stmt.executeQuery(sql);
                                items = new StringBuilder();
                                if(rs.next()) {
                                    movie_id = rs.getInt("id");
                                }
                                title = current[0];
                                items.append(current[2]+" ");
                            }
                        }
                    }
                    //locations file
                    if(csvFiles.get(i).equals("locations")){
                        String sql = "SELECT id FROM movies WHERE movietitle = \""+clean(current[0])+"\"";
                        ResultSet rs = stmt.executeQuery(sql);
                        if(rs.next()) {
                            movie_id = rs.getInt("id");
                        }

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
                        add = "(NULL,"+movie_id+",\""+cleanedLocation+"\",\""+clean(country)+"\"),";
                        //Als hij nog niet in de lijst staat
                        if(query.indexOf(add)==-1){
                            //Voeg hem toe
                            query.append(add);
                            System.out.println(add);
                            if(movie_id % 2000 == 0){
                                System.out.println("Now at movie ID: "+movie_id);
                                Double percentage = round((((movie_id*1.0)/1324000)*100),2);
                                System.out.println(percentage +"% done with locations.csv");
                            }
                        }
                    }
                    //soundtracks file NOG NIET GOED
                    else if(csvFiles.get(i).equals("soundtracks")){
                        String movietitle = clean(current[0]);
                        String releaseyear = clean(current[1]);
                        StringBuilder songs = new StringBuilder();
                        query.append("(NULL,\""+movietitle+"\",\""+releaseyear+"\",\"");

                        for(int c=2; c<10000; c++){
                            try{
                                if (c==2){
                                    songs.append(clean(current[c]));
                                }

                                else if(current[c]!=null){
                                    if (songs.indexOf(clean(current[c]))==-1){
                                        songs.append("," + clean(current[c]));
                                    }
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException e){
                                break;
                            }
                            if(c==9999){System.out.println("Limit reached");}
                        }
                        query.append(songs.toString()+"\"),");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Voeg de laatste data toe voor movies
                if(csvFiles.get(i).equals("movies")){
                    query.append("\""+clean(year1)+"\",\""+clean(year2)+"\","+0+"),");
                }
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

            stmt.executeUpdate(query.toString());
            System.out.println(csvFiles.get(i)+" exported to database.");
        }

        conn.close();
        stmt.close();
        System.out.println("database build finished and all connections closed.");
    }

    static String clean(String input){
        return input.replaceAll("\\\\","").replaceAll("\\{", "").replaceAll("javascript:void;", "").replaceAll("}", "").replaceAll("\\)", "").replaceAll("\\(", "").replaceAll("\"", "\\\\\"").replaceAll("\'","").replaceAll(",","\\,").trim();
    }
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
