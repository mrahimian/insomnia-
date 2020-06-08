package GUI;

import javax.swing.*;
import java.awt.event.*;

import static GUI.LeftPanel.addToList;
import static GUI.MainFrame.*;


public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
            }
        }
    } catch (Exception e){
         }
        MainFrame gui = new MainFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                addToList();
            }
        });
    }

}
