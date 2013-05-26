package org.impressivecode.depress.data.anonymisation.objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.processing.FilerException;

public class FileHelper {
    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    public static final String KEY_FILENAME = "key-file";
    public static final String KEY_FILENAME_EXT = "txt";
    public static final String DIR_SEPARATOR = System.getProperty("file.separator");
    
    /**
     * Checks for filename correctness
     * 
     * @param String filename filename to be checked for valid characters
     * @return result of test true means that filename is valid
     */
    private static boolean isFilenameValid(String filename)
    {
        final File aFile = new File(filename);
        boolean isValid = true;
        try
        {
            if (aFile.createNewFile())
            {
                aFile.delete();
            }
        }
        catch (IOException e)
        {
            isValid = false;
        }
        return isValid;
    }

    public static String CreateTmpFile(String fileName) {
        fileName = isFilenameValid(fileName) ? fileName : KEY_FILENAME;
        File tempFile = getUniqueFile(fileName);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

        return tempFile.getPath();
    }

    public static File getUniqueFile(String fileName) {
        
        File tempFile = null;
        int i = 0;
        try
        {
            do
            {
                tempFile = new File(TMP_DIR + DIR_SEPARATOR + fileName + "-" + i + "." + KEY_FILENAME_EXT);
                i++;
            }
            while (tempFile.exists());
        }
        // Catch exception if any
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        return tempFile;
    }

    /**
     * Writes text to specified path
     * 
     * @param fullPath location of the file where to write 
     * @param text contents of the file
     * @throws IOException
     */
    public static void WriteToFile(String fullPath, String text) throws IOException {
        FileWriter fstream = new FileWriter(fullPath);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(text);
        // Close the output stream
        out.close();
    }

    /**
     * Generates key file
     * 
     * @param fileName name of the file to be created
     * @return location of generated key file
     * @throws IOException
     */
    public static String GenerateKeyFile(String fileName) throws IOException {

        String path = CreateTmpFile(fileName);
        WriteToFile(path, CryptographicUtility.generateKey());

        return path;
    }

    /**
     * Method abstracts io calls
     * 
     * @param fullPath path of file to be read
     * @return contents of the file
     * @throws IOException
     */
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
        String line = br.readLine();
        String text = line;
        // Read File Line By Line
        while ((line = br.readLine()) != null) {
            // Print the content on the console
            text += '\n';
            text += line;
        }
        return text;
    }

}