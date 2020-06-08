package GUI;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static GUI.MainFrame.frame;

/**
 * handle Header tab
 */
public class TabHeader extends JPanel {
    int headerCounter = 0;
    LinkedHashMap<Header,Integer> headers = new LinkedHashMap<>();
    String key;
    Header header ;
    public TabHeader(String key){
        this.key = key;
        header = new Header(this,headers,headerCounter,key) ;
        this.setLayout(new GridLayout(9,1,0,0));
        this.setBorder(new EmptyBorder(0,0,0,0));
        this.setPreferredSize(new Dimension(434,427));
        this.setBackground(new Color(40,41,37));
        add(header);
    }
}
