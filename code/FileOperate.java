import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FileOperate {
    public static void readz(String filename, HashMap<String,Integer> z) {
        try {
            String strLine;
            FileInputStream in = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((strLine = br.readLine()) != null) {
                String[] token = strLine.split("\\|");
                z.put(token[0],Integer.parseInt(token[1]));
            }
            in.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public static FileInputStream readdata(String filename) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(filename);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        return in;
    }

    public static void closeFile(FileInputStream in) {
        try {
            in.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
}