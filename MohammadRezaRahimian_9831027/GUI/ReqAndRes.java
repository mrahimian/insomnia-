package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static GUI.LeftPanel.current;


public class ReqAndRes extends JPanel {
     CenterPanel centerPanel;
     RightPanel rightPanel;
    public ReqAndRes(){
        super(new FlowLayout(0));
        this.setPreferredSize(new Dimension(880, 491));
        this.setBackground(new Color(40, 41, 37));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        centerPanel = new CenterPanel();
        rightPanel = new RightPanel();
        add(centerPanel);
        add(rightPanel);
        current = rightPanel;
    }

}
