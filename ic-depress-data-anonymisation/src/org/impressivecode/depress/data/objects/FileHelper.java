package org.impressivecode.depress.data.objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;

import javax.annotation.processing.FilerException;

import org.impressivecode.depress.data.anonymisation.AnonymisationNodeModel;

public class FileHelper {

    public static String RandomString(int length) {

        SecureRandom random = new SecureRandom();
        return new java.math.BigInteger(length * 5, random).toString(32);
    }

    public static String CreateTmpFile(String fileName) {
        String path = System.getProperty("java.io.tmpdir") + "\\";
        String ext = ".txt";
        File tempFile = new File(path + fileName + ext);

        try {

            int i = 0;
            while (tempFile.exists()) {
                tempFile = new File(path + fileName + ((i > 0) ? "(" + i + ")" : "") + ext);
                i++;
            }
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

        return tempFile.getPath();
    }

    public static void WriteToFile(String fullPath, String text) throws IOException {
        FileWriter fstream = new FileWriter(fullPath);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(text);
        // Close the output stream
        out.close();
    }

    public static String GenerateKeyFile(String fileName) throws IOException {

        String path = CreateTmpFile(fileName);
        WriteToFile(path, RandomString(AnonymisationNodeModel.KEY_LENGTH));

        return path;
    }

    public static String ReadFromFile(String fullPath) throws IOException {
        File file = new File(fullPath);

        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!file.canRead()) {
            throw new FilerException("Cant read from file");
        }

        FileInputStream fstream = new FileInputStream(fullPath);
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String text = "";
        String line;
        // Read File Line By Line
        while ((line = br.readLine()) != null) {
            // Print the content on the console
            text += line;
            text += '\n';
        }
        return text;
    }

}