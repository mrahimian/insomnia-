package LOGIC;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Response implements Serializable{
    public String responseHeader  ;
    public String responseCode ;
    public String responseMessage ;
    public String bodyResponse ;
    public String responseSize ;
    /**
     * show header
     * @param connection to connect
     * @return header message as a string
     */
    String showResponseHeader(HttpURLConnection connection){
        String header = "" ;
        for(String key : connection.getHeaderFields().keySet()){
            String value = connection.getHeaderField(key);
            try {
                if (key.contains("null")) {
                    key = "";
                }
                key += " = ";
            }catch (NullPointerException e){
                key = "";
            }
            header += String.format("%s%s\n", key,value);
        }
        responseHeader = header ;
        return header;
    }

    /**
     * @return response code
     * @throws IOException for unexpected exceptions
     */
    int showResponseCode(HttpURLConnection connection) throws IOException {
        responseCode = connection.getResponseCode()+"";
        return connection.getResponseCode();
    }

    /**
     * @return response message
     * @throws IOException for unexpected exceptions
     */
    String showResponseMessage(HttpURLConnection connection) throws IOException {
        responseMessage = connection.getResponseMessage();
        return connection.getResponseMessage();
    }

    /**
     * get server response body and return as a string and save in file if wanted
     * @param responseBody server response
     * @param saveToFile save to file if true
     * @param fileToSave
     * @return response body
     */
    String showResponseBody(BufferedInputStream responseBody , boolean saveToFile , File fileToSave) {
        String body = "";
        FileOutputStream writeInFile = null;
        FileOutputStream pic ;

        try{
            pic = new FileOutputStream("prev.png");
            if (saveToFile) {
                writeInFile = new FileOutputStream(fileToSave);
                int count;
                while ((count = responseBody.read()) != -1) {
                    body += (char) count;
                    writeInFile.write((char)count);
                    pic.write((char)count);
                }
                System.out.println("response body has been saved in " + fileToSave.getName());
            }
            else {
                int count;
                while ((count = responseBody.read()) != -1) {
                    body += (char) count;
                    pic.write((char)count);
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (responseBody != null)
                    responseBody.close();
                if (writeInFile != null)
                    writeInFile.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        bodyResponse = body ;
        return body;
    }

    /**
     * get response size
     * @return message size as an String with B or kB
     */
    String getSize(HttpURLConnection connection){
        int length = connection.getContentLength();
        if (length == -1){
            return "unknown";
        }
        String size = "";
        if (length < 1000){
            size = length + " B";
        }
        else {
            size = String.format("%.1f KB",(float) connection.getContentLength()/1000);
        }
        responseSize = size ;
        return size ;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseHeader='" + responseHeader + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                ", bodyResponse='" + bodyResponse + '\'' +
                ", responseSize='" + responseSize + '\'' +
                '}';
    }
}
