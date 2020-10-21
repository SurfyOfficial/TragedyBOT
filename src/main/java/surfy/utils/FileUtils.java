package surfy.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {



    public static List<String> readLinesFromFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            return reader.lines().collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static void writeLinesToFile(File file,List<String> lines) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            lines.forEach(printWriter::println);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }




}
