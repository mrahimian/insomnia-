package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static GUI.LeftPanel.current;
import static GUI.LeftPanel.select;
import static GUI.MainFrame.unique;
import static java.awt.BorderLayout.*;

public class CenterPanel extends JPanel {
    JTabbedPane tabs;
    private JPanel urlPanel;
    JPanel tabsPanel;
    JPanel body;
    JPanel binaryFile;
    JButton file;
    File fileToSend ;
    File saveInFile = null ;
    JTextArea json;
    JTextField url;
    private JPopupMenu pm;
    JButton send ;
    JButton save ;
    boolean saveFile = false;
    int next = 2;
    JComboBox methods;
    TabHeader tabHeader;
    TabHeader formData;
    JScrollPane scrollPane;
    HashMap<String,String> header = null;
    HashMap<String,String> fooBody = null;
    int post  = 0 ;
    int sendData = 0;
    boolean flag1 = true ;
    boolean followRedirect = false;
    /**
     * set center panel
     */
    public CenterPanel(){
        this.setBackground(new Color(60, 63, 65));
        this.setLayout(new BorderLayout(0,0));
        this.setBorder(new EmptyBorder(0,0,0,0));
        this.setPreferredSize(new Dimension(434,491));
        // url panel for north
        urlPanel = new JPanel(new BorderLayout(0,0));
        urlPanel.setPreferredSize(new Dimension(434,46));
        urlPanel.setBorder(new EmptyBorder(0,0,0,0));
        urlPanel.setBackground(new Color(255, 255, 255));
        // set methods
        methods = new JComboBox();
        methods.setBackground(Color.WHITE);
        methods.setPreferredSize(new Dimension(70,44));
        methods.setFont(new Font("Arial", 14, 12));
        methods.addItem("GET");
        methods.addItem("POST");
        methods.addItem("PUT");
        methods.addItem("PATCH");
        methods.addItem("DELETE");
        // field to write URL
        url = new JTextField("Enter URL...");
        url.setBackground(new Color(255, 255, 255));
        url.setFont(new Font("Arial",14,14));
        url.setForeground(Color.BLACK);
        url.setPreferredSize(new Dimension(338,46));
        url.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10){
                    sendRequest();
                }
            }
        });
        Handler clear = new Handler(this);
        url.addFocusListener(clear);

        //add send and save buttons
        JPanel buttons = new JPanel(new GridLayout(1,2));
        buttons.setBackground(new Color(255, 255, 255));
        send = new JButton("Send");
        send.setBackground(Color.WHITE);
        save = new JButton("Save");
        save.setBackground(Color.WHITE);
        save.addActionListener(clear);
        send.addActionListener(clear);
        buttons.add(send);
        buttons.add(save);
        //set north
        urlPanel.add(methods,WEST);
        urlPanel.add(url,CENTER);
        urlPanel.add(buttons,EAST);
        this.add(urlPanel,BorderLayout.NORTH);

        //tabs panel is under url panel

        tabsPanel = new JPanel();
        tabsPanel.setBackground(new Color(40, 41, 37));
        tabsPanel.setPreferredSize(new Dimension(434,427));
        tabs = new JTabbedPane();
        tabs.setBounds(0,0,434,427);
        tabsPanel.add(tabs);
        tabs.setBackground(new Color(40, 41, 37));


        //body tab
        body = new JPanel();
        body.setBackground(new Color(40,41,37));
        body.setBackground(new Color(40,41,37));
        body.setPreferredSize(new Dimension(434,427));
        tabs.add("Body",body);
        body.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    pm.setVisible(false);
                }catch (NullPointerException e1){}
            }
        });
        //header tab
        tabHeader = new TabHeader("header");

        tabs.add("Header",tabHeader);
        Handler clearText = new Handler(this);
        tabs.addMouseListener(clearText);

        add(tabsPanel, CENTER);

        // body : form data , json , binary file
        json = new JTextArea("Enter Your Code");
        json.setForeground(Color.WHITE);
        json.setBackground(new Color(60,63,65));
        json.addFocusListener(clearText);
        scrollPane = new JScrollPane(json);
        scrollPane.setPreferredSize(new Dimension(404, 407));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        binaryFile = new JPanel();
        file = new JButton("Select File");
        file.setPreferredSize(new Dimension(150,80));
        binaryFile.add(file);
        binaryFile.setPreferredSize(new Dimension(404,407));
        file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fileToSend = getFileOrDirectoryPath();
                    String s1 = fileToSend.toString();
                    String s2 = "";
                    for (int i = s1.length() - 1; s1.charAt(i) != '\\'; i--) {
                        s2 += s1.charAt(i);
                    }
                    StringBuilder show = new StringBuilder();
                    show.append(s2);
                    file.setText(show.reverse().toString());
                }catch (NullPointerException e1){
                }
            }
        });
        binaryFile.setBackground(new Color(40,41,37));
        formData = new TabHeader("key");
    }
    private File getFileOrDirectoryPath()
    {
        // configure dialog allowing selection of a file or directory
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(
                JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION)
            fileChooser.cancelSelection();
        return fileChooser.getSelectedFile();
    }
    private void sendRequest() {
        String link = "";
        File fileToSave = null;
        String method = "GET";
        boolean saveRequest = false;
        followRedirect = false;
        String jsonText = "";
        File uploadBinary = null;
            if (!select) {
                JOptionPane error = new JOptionPane();
                error.showMessageDialog(null, "make a request or select one", "Error ", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                link = url.getText();
                method = methods.getSelectedItem().toString();
                saveRequest = saveFile;
                if (!(json.getText().equals("Enter Your Code"))) {
                    jsonText = json.getText();
                }
                try {
                    fileToSave = saveInFile;
                } catch (NullPointerException e1) {
                    fileToSave = null;
                }
                try {
                    uploadBinary = fileToSend;
                } catch (NullPointerException e1) {
                    uploadBinary = null;
                }
                try {
                    fooBody = formData.header.getHeaders();
                } catch (NullPointerException e1) {
                    fooBody = null;
                }
                try {
                    header = tabHeader.header.getHeaders();
                } catch (NullPointerException e1) {
                    header = null;
                    e1.printStackTrace();
                }
                if (MainFrame.check.getState()) {
                    followRedirect = true;
                }
                post = sendData;
            } catch (NullPointerException e1) {
                e1.printStackTrace();
                fileToSave = null;
                uploadBinary = null;
            } finally {
                try {
                    ConnectToLogic req = new ConnectToLogic(new URL(link), method, true, post, followRedirect, fooBody, jsonText, uploadBinary, saveRequest, fileToSave, header, true, current);
                    ExecutorService executorService = Executors.newCachedThreadPool();
                    executorService.execute(req);
                } catch (MalformedURLException ex) {
                    ConnectToLogic req = null;
                    try {
                        req = new ConnectToLogic(new URL("http://" + link), method, true, post, followRedirect, fooBody, jsonText, uploadBinary, saveRequest, fileToSave, header, true, current);
                        ExecutorService executorService = Executors.newCachedThreadPool();
                        executorService.execute(req);
                    } catch (MalformedURLException exc) {
                        exc.printStackTrace();
                    }
                }
                for (JTextField reqs : unique.keySet()) {
                    if (unique.get(reqs).centerPanel.equals(this)) {
                        LeftPanel.requestsFields.add(reqs);
                    }
                }
            }
    }

    /**
     * handle textField and textArea
     */
    /*private */class Handler extends MouseAdapter implements FocusListener , ActionListener  {
        CenterPanel c;
        public Handler(CenterPanel c) {
            this.c = c;
        }

        /**
         * clear tex boxes when click
         * @param e click on check boxes
         */
        @Override
        public void focusGained(FocusEvent e) {
            if (e.getComponent().equals(url) && url.getText().equals("Enter URL...")) {
                url.setText("");
            }
            else if(e.getComponent().equals(json) && ((json.getText().equals("Enter Your Code")))){
                json.setText("");
            }
        }

        /**
         * set tex boxes to default when nothing is written
         * @param e focus on text boxes
         */
        @Override
        public void focusLost(FocusEvent e) {
            if(e.getComponent().equals(url) ){
                if (url.getText().length()==0) {
                    url.setText("Enter URL...");
                }
            }
            else {
                if (json.getText().length()==0) {
                    json.setText("Enter Your Code");
                }
            }
        }
        /**
         * add popup menu to body tab
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel formData1;
            JLabel json;
            JLabel binary;
            if (tabs.getSelectedIndex() == 0){
                formData1 = new JLabel("FormData  ");
                formData1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (flag1) {
//                            formData = new TabHeader("key");
                        }
                            tabs.setTitleAt(0, "FormData");
                            body.add(formData, 0);
                            sendData = 1;
                            pm.setVisible(false);
                            flag1 = false ;
                    }
                });
                json = new JLabel("JSON         ");
                json.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        body.add(scrollPane, 0);
                        tabs.setTitleAt(0,"JSON");
                        body.revalidate();
                        sendData = 2 ;
                        pm.setVisible(false);
                    }
                });
                binary = new JLabel("Binary File");
                binary.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        tabs.setTitleAt(0,"Binary File");
                        body.add(binaryFile,0);
                        body.revalidate();
                        sendData = 3;
                        pm.setVisible(false);
                    }
                });
                pm = new JPopupMenu();
                pm.add(formData1);
                pm.add(json);
                pm.add(binary);
                pm.show(body,0,0);
            }
        }

        /**
         * send request when clicking on "send"
         * save request if "save" is green
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(save)){
                if (!saveFile) {
                    try {
                        saveInFile = getFileOrDirectoryPath();
                        save.setBackground(Color.GREEN);
                    }catch (Exception e1){

                    }
                    saveFile = true;
                }
                else {
                    save.setBackground(Color.white);
                    saveFile = false;
                }
            }
            if (e.getSource().equals(send)){
                sendRequest();
            }
        }
    }

}
