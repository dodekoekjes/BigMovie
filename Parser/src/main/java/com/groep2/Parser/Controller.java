package com.groep2.Parser;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

class Controller {

    boolean doPatternMatching = true;
    boolean doDatabaseBuild = false;

    Controller() throws SQLException {

        //Je regular expression
        String regexRatings = "\\s+.{10}\\s+(\\d+)\\s+(.{3}|.{4})\\s+(.+?)\\(((.{4}).*?|)\\)(.+|)"; //1 = aantal stemmen, 2 = rating 3 = FilmNaam, 4 = releaseYear
        String regexSound = "(# (.+?)\\((.+?)\\)(.+|))|(- (.+))|#(\\s)";
        String regexMovies = "(.*?)\\s*\\((.{4,12}?)\\)(.*)(.{4})";
        String regexLocations = "(.*?)\\s\\((.{4,12})\\)(\\s*)(\\(.{0,24}?\\)|)(\\{.*\\}|)(\\s*)(.*?\\b\\s\\(.*\\)|.*)";
        String regexGenres = "(.*?)(\\s*)\\((.*?)\\)(\\s*)(\\(.*\\)|)(\\{.*\\}|)(.*)";

        //De naam van de list, list moet in resources.
        String ratingsFile =    "ratings.list";
        String soundFile =      "soundtracks.list";
        String movieFile =      "movies.list";
        String genreFile =      "genres.list";
        String locationFile =   "locations.list";

        //De groepen uit je regular expression die hij in je CSV moet plaatsen
        List<Integer> ratingsGroups = Arrays.asList(1,2,3);
        List<Integer> moviesGroups = Arrays.asList(1,2,4);
        List<Integer> locationsGroups = Arrays.asList(1,2,7);
        List<Integer> genresGroups = Arrays.asList(1,3,7);

        if(doPatternMatching){
            PatternMatcher.Match(regexRatings, ratingsFile, ratingsGroups);
            PatternMatcher.Match(regexGenres, genreFile, genresGroups);
            PatternMatcher.Match(regexLocations, locationFile, locationsGroups);
            PatternMatcher.Match(regexMovies, movieFile, moviesGroups);

            PatternMatcher.MatchSound(regexSound, soundFile);
            //Patternmatch en schrijf het resultaat naar een CSV
        }
        if (doDatabaseBuild){
            //Connect met de database
            DatabaseHandler.connect();
            //Maak de database tabellen (opnieuw) aan
            DatabaseHandler.createTables();
        }
    }
}
