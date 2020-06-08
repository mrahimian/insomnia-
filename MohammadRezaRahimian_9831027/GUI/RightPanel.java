package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static GUI.MainFrame.mainPanel;

/**
 * make and set right panel
 */
public class RightPanel extends JPanel {
    JTabbedPane responseTabs;
    JPanel preview,header,info,tabsPanel;
    JTextField statusCode,duration,messageSize;
    JTextArea code;
    JPopupMenu pm;
    int counter = 0;
    JScrollPane scrollPane;
    boolean flag = false;
    ArrayList<JPanel> headers;
    JTextArea responseHeader ;
    JLabel pic ;
    String stringCopy = null;

    /**
     * set right panel
     */
    public RightPanel(){
        this.setBackground(new Color(40, 41, 37));
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setPreferredSize(new Dimension(419, 491));
        //set panel for showing response information
        info = new JPanel(new FlowLayout(0));
        info.setPreferredSize(new Dimension(419, 46));
        info.setBorder(new EmptyBorder(3, 0, 0, 0));
        info.setBackground(new Color(255, 255, 255));
        add(info, BorderLayout.NORTH);
        //show time duration and status code and
        statusCode = new JTextField("code");
        statusCode.setEditable(false);
        statusCode.setPreferredSize(new Dimension(150, 30));
        statusCode.setBackground(new Color(72, 217, 37));
        statusCode.setForeground(Color.white);
        info.add(statusCode);
        duration = new JTextField("duration");
        duration.setEditable(false);
        duration.setPreferredSize(new Dimension(100, 30));
        info.add(duration);
        messageSize = new JTextField("size");
        messageSize.setEditable(false);
        messageSize.setPreferredSize(new Dimension(100, 30));
        info.add(messageSize);
        Popup p = new Popup();
        //set header and preview
        responseTabs = new JTabbedPane();
        tabsPanel = new JPanel();
        tabsPanel.setBackground(new Color(40, 41, 37));
        tabsPanel.add(responseTabs);
        add(tabsPanel, BorderLayout.CENTER);
        preview = new JPanel();
        preview.setPreferredSize(new Dimension(419, 450));
        preview.setBackground(new Color(40, 41, 37));
        preview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    pm.setVisible(false);
                }catch (NullPointerException e1){

                }
            }
        });
        this.header = new JPanel(new BorderLayout(0, 0));
        this.header.setPreferredSize(new Dimension(419, 450));
        this.header.setBorder(new EmptyBorder(0, 0, 50, 0));
        this.header.setBackground(new Color(40, 41, 37));
        JButton copy = new JButton("copy to Clipboard");
        copy.setPreferredSize(new Dimension(50, 40));
        try {
            copy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StringSelection stringSelection = new StringSelection(stringCopy);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
            });
        }catch (Exception e){}
        this.header.add(copy, BorderLayout.SOUTH);
        JPanel h = new JPanel(new GridLayout(8, 1, 0, 0));
        h.setBorder(new EmptyBorder(0, 0, 0, 0));
        h.setBackground(new Color(40, 41, 37));
        responseHeader = new JTextArea();
        responseHeader.setPreferredSize(new Dimension(419, 420));
        responseHeader.setBackground(new Color(40, 41, 37));
        responseHeader.setForeground(Color.white);
        responseHeader.setEditable(false);
        responseHeader.setLineWrap(true);
        this.header.add(responseHeader, BorderLayout.CENTER);
        responseTabs.add("message body", preview);
        responseTabs.add("Header", this.header);
        responseTabs.addMouseListener(p);
        code = new JTextArea("source code");
        code.setLineWrap(true);
        code.setEditable(false);
        code.setBackground(new Color(60, 63, 65));
        code.setForeground(Color.white);

        scrollPane = new JScrollPane(code);
        scrollPane.setPreferredSize(new Dimension(400, 407));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        preview.add(scrollPane);
    }
    public void update(String body , int responseCode , String responseMessage ,String header , String contentLength , String contentType){
        stringCopy = header;
        Date secondDate = new Date();
        long time = secondDate.getTime() - ConnectToLogic.first;
        responseHeader.setText(header);
        code.setText(body);
        statusCode.setText(responseCode + " " + responseMessage);
        messageSize.setText(contentLength);
        System.out.println(body+"\n"+responseCode+"\n"+responseMessage+"\n"+header+"\n"+contentLength);
        if (contentType.equals("image/gif") || contentType.equals("image/jpeg") || contentType.equals("image/png")){
            pic = new JLabel(new ImageIcon("prev.png"));
            preview.add(pic);
            responseTabs.setTitleAt(0,"Preview");
        }
        else {
            responseTabs.setTitleAt(0,"Raw");
        }
        duration.setText(time + "  ms");
        mainPanel.revalidate();
    }
    private class Popup extends MouseAdapter   {
        JLabel raw;
        JLabel prev;
        /**
         * handle tabs
         * @param e tab which is clicked
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (responseTabs.getSelectedIndex() == 0){
                pm = new JPopupMenu();
                raw = new JLabel("Raw        ");
                raw.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        responseTabs.setTitleAt(0,"Raw");
                        if(counter>0){
                            preview.add(scrollPane,0);
                        }
                        pm.setVisible(false);
                        flag = false;
                        repaint();
                    }
                });
                prev = new JLabel("Preview  ");
                prev.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        responseTabs.setTitleAt(0,"Preview");
                        if (!flag) {
                            preview.remove(0);
                            pm.setVisible(false);
                        }
                        counter++;
                        flag = true;
                        repaint();
                    }
                });

                pm.add(raw);
                pm.add(prev);
                pm.show(preview,0,0);
            }
        }
    }

}
