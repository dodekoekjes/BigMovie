//-------------------------------------------------------//
// Deze pagina is gemaakt door Kevin Snijder
// Spoiler alert: de hele parser is gemaakt door mij! :)
//-------------------------------------------------------//

package com.groep2.Parser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVWriter {
    private BufferedWriter writer;
    private StringBuilder sb;


    CSVWriter(String filename){
        filename = filename.substring(0, filename.length() - 5);
        String location = "Parser/src/main/resources/output/"+filename+".csv";

        Path path = Paths.get(location);

        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Delete het oude bestand
        try {
            Files.delete(path);
            System.out.println("Old "+ filename +" deleted.");
        }
        catch (NoSuchFileException x) {
            System.out.println("No old"+ filename +"file found.");
        }
        catch (IOException x) {
            // File permission problems are caught here.
            System.out.println("I have no permission to delete the old file.");
        }

        //Start een nieuwe filewriter
        try {
            writer = Files.newBufferedWriter(Paths.get(location), StandardCharsets.UTF_8);
            sb = new StringBuilder();
        }
        catch (IOException e) {
            System.out.println("IOException");
        }
    }

    public void Write(List<String> writeables){
        for(int i = 0; i < writeables.size(); i++){
            sb.append(writeables.get(i));
            //Als de volgende niet leeg is, plaats een komma
            if(i!=writeables.size()-1){
                sb.append(",&");
            }
            else{
                sb.append("\n");
            }

        }
    }

    public void Finish() {
        try {
            writer.write(sb.toString());
            System.out.println("File write success!");
        } catch (IOException e) {
            System.out.println("Something went wrong whole trying to write.");
        }
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("Something went wrong while trying to close the writer.");
        }
    }
}
