package GUI;

import LOGIC.Request;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;


public class ConnectToLogic implements Runnable {
    public static long first;
    URL url ;
    String method ;
    boolean header ;
    int post ;
    boolean follow ;
    HashMap<String, String> fooBody;
    String json ;
    File binaryFile ;
    boolean saveToFile ;
    File fileToSave ;
    HashMap<String, String> headerToSend;
    boolean addToList;
    RightPanel rightPanel;
    ConnectToLogic(URL url , String method , boolean header , int post , boolean follow , HashMap<String, String> fooBody
            , String json , File binaryFile , boolean saveToFile , File fileToSave , HashMap<String, String> headerToSend, boolean addToList , RightPanel rightPanel ){
        this.url = url;this.method = method;this.header = header;this.post = post;this.follow = follow;this.fooBody = fooBody;
        this.json = json;this.binaryFile = binaryFile;this.saveToFile = saveToFile;this.fileToSave = fileToSave;this.headerToSend = headerToSend;
        this.addToList = addToList;this.rightPanel= rightPanel;

    }
    @Override
    public void run() {
        Date firstDate = new Date();
        first = firstDate.getTime();
        Request request = new Request(url,method,header,post,follow,fooBody,json,binaryFile,saveToFile,fileToSave,headerToSend,true,rightPanel);

    }
}
