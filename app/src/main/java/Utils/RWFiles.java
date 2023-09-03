package Utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RWFiles {
    public static boolean WriteFileInternalDir(Context context, String fileName, String strData) {

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(strData);
            writer.close();
            fos.close();

            File directory = context.getFilesDir();
            File file = new File(directory, fileName);

            return file.exists();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  false;
    }

    public static String ReadFileInternalDir(Context context, String fileName) throws IOException {
        File directory = context.getFilesDir();
        File file = new File(directory, fileName);

        if(file.exists()){
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            isr.close();
            fis.close();

            return String.valueOf(sb);
        }else{
            return "";
        }
    }
}
