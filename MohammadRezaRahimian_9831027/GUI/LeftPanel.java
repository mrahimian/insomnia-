package GUI;

import LOGIC.Request;
import LOGIC.Response;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Tab;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static GUI.MainFrame.*;

/**
 * make left panel and its items
 */
public class LeftPanel extends JPanel {
    static boolean select = false;
    JPanel req;
    ReqAndRes reqAndRes ;
    JTextField next;
    static ArrayList<RightPanel> rights = new ArrayList<>();
    static ArrayList<JTextField> requestsFields = new ArrayList<>();
    static RightPanel current ;
    public void setLeftPanel() {
        req = new JPanel(true){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.setColor(new Color(152, 42, 255));
                g.fillRect(0,0,252,46);
                g.setFont(new Font("Arial",Font.BOLD,15));
                g.setColor(Color.WHITE);
                g.drawString("Insomnia",20,28);
            }
        };
        this.setBackground(new Color(46, 47, 43));
        this.setPreferredSize(new Dimension(247,491));
        this.setLayout(new BorderLayout(0,0));
        req.setLayout(new GridLayout(15,1,0,0));
        req.setBorder(new EmptyBorder(40,0,0,0));
        req.setBackground(new Color(46, 47, 43));
        //set request box

        add(req,BorderLayout.CENTER);
        JButton newReq = new JButton("new request");
        newReq.setPreferredSize(new Dimension(247,30));
        NewReq handler = new NewReq();
        newReq.addActionListener(handler);
        add(newReq,BorderLayout.SOUTH);
        newReq.setHorizontalTextPosition(SwingConstants.LEFT);
    }

    /**
     * ask name of request and add it to list of requests
     */
    public void addRequest(){
        JOptionPane o = new JOptionPane();
        String name = o.showInputDialog("Name of Request");
        for(JTextField textField : unique.keySet()){
            if (textField.getText().equals(name)){
                JOptionPane error = new JOptionPane();
                error.showMessageDialog(null,"Repetitive Request","Error ",JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        try{
            if (name.equals(null));
        }catch (Exception e1){
            name = "";
        }
        if(!name.trim().equals("")) {
            next = new JTextField(name);
            next.setPreferredSize(new Dimension(247, 15));
            next.setFont(new Font("Arial", Font.BOLD, 14));
            next.setEditable(false);
            next.addMouseListener(new NewReq());
            req.add(next);
            req.repaint();
            reqAndRes = new ReqAndRes();
            rights.add(reqAndRes.rightPanel);
            mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
            mainPanel.remove(1);
            mainPanel.revalidate();
            mainPanel.add(reqAndRes,BorderLayout.CENTER);
            unique.putIfAbsent(next, reqAndRes);
            frame.revalidate();
            frame.repaint();
        }
    }

    /**
     * add textFields to a list to load
     */
    public static void addToList(){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null  ;
        try {
            fos = new FileOutputStream("list.txt");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(requestsFields);
        } catch (EOFException | StreamCorruptedException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (oos!=null){
                    oos.close();
                }
                if (fos!=null){
                    fos.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    /**
     * fill response panel with response info
     * @param rightPanel to fill
     * @param i number of response
     */
    private void matchWithResponse(RightPanel rightPanel, int i){
        try(FileInputStream is = new FileInputStream("responses.txt");
        ObjectInputStream ois = new ObjectInputStream(is)) {
            ArrayList<Response> list = (ArrayList<Response>) ois.readObject();
            Response response = list.get(i);
            rightPanel.responseHeader.setText(response.responseHeader);
            rightPanel.code.setText(response.bodyResponse);
            rightPanel.messageSize.setText(response.responseSize);
            rightPanel.statusCode.setText(response.responseCode + " " + response.responseMessage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * put the request info in center panel components
     * @param centerPanel to fill
     * @param i number of request
     */
    private void putInRequest(CenterPanel centerPanel, int i){
        try(FileInputStream is = new FileInputStream("requests.txt");
            ObjectInputStream ois = new ObjectInputStream(is)){
            ArrayList<Request> list = (ArrayList<Request>) ois.readObject();
            centerPanel.url.setText(list.get(i).url.toString());
            centerPanel.followRedirect = list.get(i).follow;
            if (list.get(i).saveToFile){
                centerPanel.saveFile = true;
                centerPanel.saveInFile = list.get(i).fileToSave;
                centerPanel.save.setBackground(Color.GREEN);
            }
            switch (list.get(i).method){
                case "GET":
                    centerPanel.methods.setSelectedIndex(0);
                    break;
                case "POST":
                    centerPanel.methods.setSelectedIndex(1);
                    break;
                case "PUT":
                    centerPanel.methods.setSelectedIndex(2);
                    break;
                default:
                centerPanel.methods.setSelectedIndex(4);
            }
            if (!(list.get(i).json.equals(""))){
                centerPanel.json.setText(list.get(i).json);
            }
            else if (list.get(i).binaryFile!=null){
                centerPanel.fileToSend = list.get(i).binaryFile;
                String s1 = list.get(i).binaryFile.toString();
                String s2 = "";
                for (int j = s1.length() - 1; s1.charAt(j) != '\\'; j--) {
                    s2 += s1.charAt(j);
                }
                StringBuilder show = new StringBuilder();
                show.append(s2);
                centerPanel.file.setText(show.reverse().toString());
            }
            else {
                centerPanel.formData = new TabHeader("key") ;
                TabHeader tabHeader = centerPanel.formData ;
                Header header = tabHeader.header;
                int count = 0;
                for (String keyValue : list.get(i).fooBody.keySet()) {
                    header.key.setText(keyValue);
                    header.value.setText(list.get(i).fooBody.get(keyValue));
                    tabHeader.revalidate();
                    count++;
                    if (count == list.get(i).fooBody.keySet().size()){
                        continue;
                    }
                    header = header.addHeader();
                }
            }
            TabHeader tabHeader = centerPanel.tabHeader ;
            Header header = tabHeader.header;
            int count = 0;
            for (String keyValue : list.get(i).headerToSend.keySet()) {
                header.key.setText(keyValue);
                header.value.setText(list.get(i).headerToSend.get(keyValue));
                tabHeader.revalidate();
                count++;
                if (count == list.get(i).headerToSend.keySet().size()){
                    continue;
                }
                header = header.addHeader();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (NullPointerException e){e.printStackTrace();}
    }

    /**
     * load requestsFields from file and match with requests
     */
    public void loadFromFile() {
        try (FileInputStream is = new FileInputStream("list.txt");
             ObjectInputStream ois = new ObjectInputStream(is)) {
            ArrayList<JTextField> list = (ArrayList<JTextField>) ois.readObject();
            int i=0;
            for (JTextField reqs : list) {
                req.add(reqs);
                req.repaint();
                reqs.addMouseListener(new NewReq());
                reqAndRes = new ReqAndRes();
                rights.add(reqAndRes.rightPanel);
                putInRequest(reqAndRes.centerPanel , i);
                matchWithResponse(reqAndRes.rightPanel,i);
                mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
                try {
                    mainPanel.remove(1);
                }catch (NullPointerException |ArrayIndexOutOfBoundsException e){e.printStackTrace();}
                mainPanel.repaint();
                mainPanel.add(reqAndRes,BorderLayout.CENTER);
                unique.putIfAbsent(reqs, reqAndRes);
                requestsFields.add(reqs);
                frame.revalidate();
                frame.repaint();
                i++;
            }
        } catch (EOFException e) {
            System.err.println("Nothing has been saved yet");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private class NewReq extends MouseAdapter implements ActionListener {

        /**
         * create new request by clicking at button
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            addRequest();
        }

        /**
         * match request with its panel
         * @param e
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            int counter = 0;
            mainPanel.remove(1);
            mainPanel.revalidate();
            mainPanel.add(unique.get(e.getComponent()),BorderLayout.CENTER);
            for (JTextField reqs : unique.keySet()) {
                if (reqs.equals(e.getComponent())){
                    current = rights.get(counter);
                }
                counter++;
            }
            mainPanel.revalidate();
            mainPanel.repaint();
            frame.repaint();
            select = true;
        }
    }
}
