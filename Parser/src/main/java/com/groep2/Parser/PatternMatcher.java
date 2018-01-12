package com.groep2.Parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
    public static void Match(String regex, String filename, List<Integer> groups){

        CSVWriter writer = new CSVWriter(filename);
        List<String> writeables;
        String path = "Parser/src/main/resources/"+filename;

        try {
            BufferedReader bufferreader = Files.newBufferedReader(Paths.get(path), StandardCharsets.ISO_8859_1);
            String line = bufferreader.readLine();
            int count=0;

            while (line != null) {

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                boolean matches = matcher.matches();

                if (matches && !line.trim().equals("7: Copying Policy: Internet Movie Database (IMDb)")){
                     writeables = new ArrayList<>();

                    //Haal de correcte strings uit de match en zet ze in een lijstje
                    for (Integer group : groups) {
                        writeables.add(matcher.group(group).trim());
                    }
                    writer.Write(writeables);
                    count++;

                }
                else {
                    //System.out.println("FAILED: "+ line);
                }
                line = bufferreader.readLine();
            }
            writer.Finish();
            System.out.println(count + " Lines matched");
        }
        catch (FileNotFoundException ex) {
            System.out.println("File not found");
        }
        catch (IOException ex) {
            System.out.println("File read failed");
        }
        catch(IllegalStateException ex){
            System.out.println("No match found");
        }
    }

    public static void MatchSound(String regex, String filename){

        CSVWriter writer = new CSVWriter(filename);
        List<String> writeables;

        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("Parser/src/main/resources/"+filename));
            String line = bufferreader.readLine();
            int count=0;
            boolean ignorenext=false;
            writeables = new ArrayList<>();

            while (line != null) {

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                boolean matches = matcher.matches();

                if (matches){

                    //System.out.println(line);
                    if (ignorenext){
                        ignorenext=false;
                    }
                    else{
                        if(matcher.group(2)!=null){
                            writer.Write(writeables);
                            writeables = new ArrayList<>();
                            //System.out.println("Film: "+matcher.group(2));
                            writeables.add(matcher.group(2).trim().toLowerCase());
                            writeables.add(matcher.group(3).trim().toLowerCase());
                        }

                        else if(matcher.group(6)!=null){
                            //System.out.println("Song: "+matcher.group(4));
                            writeables.add(matcher.group(6).toLowerCase().trim());
                        }

                        else if(matcher.group(7)!=null){
                            //System.out.println("Ignore next, no film attached");
                            ignorenext=true;
                        }
                    }
                    count++;

                }
                else {
                    //System.out.println("FAILED: "+ line);
                }
                line = bufferreader.readLine();
            }
            writer.Write(writeables);
            writer.Finish();
            System.out.println(count + " Lines matched");
        }
        catch (FileNotFoundException ex) {
            System.out.println("File not found");
        }
        catch (IOException ex) {
            System.out.println("File read failed");
        }
        catch(IllegalStateException ex){
            System.out.println("No match found");
        }

    }
}
