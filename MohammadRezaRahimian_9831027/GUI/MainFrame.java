package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Scanner;

import static GUI.LeftPanel.addToList;
import static java.awt.Frame.NORMAL;

/**
 * set frame and main panel and handle menus
 */

public class MainFrame extends JPanel {
    protected static JFrame frame;
    static TrayIcon trayIcon;
    SystemTray tray;
    private JMenu application,view,help;
    private JMenuBar menu;
    JMenuItem app1,app2;
    JMenuItem view1,view2;
    JMenuItem help1,help2;
    protected static JPanel mainPanel = new JPanel(new BorderLayout(0,0));
    protected static LinkedHashMap<JTextField,ReqAndRes> unique = new LinkedHashMap<>();
    int n = 1;
    static Checkbox check = new Checkbox();
    Checkbox check2 = new Checkbox();
    String data;
    boolean flag = false;
    String h;
    public MainFrame(){
        try {
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        if (data.charAt(0) == '0'){
            check.setState(false);
        }
        else {
            check.setState(true);
        }
        if (data.charAt(1) == '0'){
            check2.setState(false);
        }
        else {
            check2.setState(true);
        }
         //  set frame
        frame = new JFrame("Insomnia");
        //frame.setLayout(null);
        frame.setLocation(110,95);
        frame.setSize(1130,565);
        frame.setMinimumSize(new Dimension(700, 100));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon insomnia = new ImageIcon(getClass().getResource("1.png"));
        frame.setIconImage(insomnia.getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //set menu bar
        menu = new JMenuBar();
        application = new JMenu("Application");
        app1 = new JMenuItem("Option");
        app2 = new JMenuItem("Exit");
        app1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.SHIFT_MASK));
        application.add(app1);
        application.add(app2);
        app2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.SHIFT_MASK));
        view = new JMenu("View");
        view1 = new JMenuItem("Toggle Full Screen");
        view1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.SHIFT_MASK));
        view2 = new JMenuItem("Toggle Sidebar");
        view2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_MASK));
        view.add(view1);
        view.add(view2);
        help = new JMenu("Help");
        help1 = new JMenuItem("About");
        help2 = new JMenuItem("Help");
        help1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.SHIFT_MASK));
        help2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.SHIFT_MASK));
        help.add(help1);
        help.add(help2);
        menu.add(application);
        menu.add(view);
        menu.add(help);
        frame.setJMenuBar(menu);
        // make Object from MenuHandler
        MenuHandler menuHandler = new MenuHandler();
        app1.addActionListener(menuHandler);
        app2.addActionListener(menuHandler);
        view1.addActionListener(menuHandler);
        view2.addActionListener(menuHandler);
        help1.addActionListener(menuHandler);
        help2.addActionListener(menuHandler);
        check.addItemListener(menuHandler);
        check2.addItemListener(menuHandler);

        // add panels to frame
        frame.setContentPane(mainPanel);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        LeftPanel leftPanel = new LeftPanel();
        leftPanel.setLeftPanel();
        mainPanel.add(leftPanel,BorderLayout.WEST);
        ReqAndRes reqAndRes = new ReqAndRes();
        leftPanel.repaint();
        mainPanel.add(reqAndRes,BorderLayout.CENTER);
        leftPanel.loadFromFile();
    }


    private class MenuHandler implements ActionListener , ItemListener {
        /**
         * handle menu items
         * @param e menu item which clicked
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(app1)) {
                JFrame f2 = new JFrame("option");
                f2.setLayout(null);
                f2.setLocation(40, 40);
                f2.setSize(300, 300);
                f2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f2.setVisible(true);
                JLabel label = new JLabel("Follow Redirect");
                label.setSize(new Dimension(150,20));
                label.setLocation(10,100 );
                label.setForeground(new Color(63, 128, 48));
                label.setFont(new Font("Arial",Font.BOLD,14));
                f2.add(label);
                JLabel label2 = new JLabel("Hide To System Tray");
                label2.setSize(new Dimension(200,20));
                label2.setLocation(10,130 );
                label2.setForeground(new Color(63, 128, 48));
                label2.setFont(new Font("Arial",Font.BOLD,14));
                f2.add(label2);
                check.setSize(new Dimension(20, 20));
                check.setLocation(250,100);
                f2.add(check);
                check2.setSize(new Dimension(20, 20));
                check2.setLocation(250,130);
                f2.add(check2);
            }
            else if(e.getSource().equals(app2)){
                if (check2.getState()) {
                        if(SystemTray.isSupported()){
                            tray=SystemTray.getSystemTray();
                            Image image=Toolkit.getDefaultToolkit().getImage("1.png");
                            ActionListener exitListener= e13 -> System.exit(0);
                            PopupMenu popup=new PopupMenu();
                            MenuItem defaultItem=new MenuItem("Exit");
                            defaultItem.addActionListener(exitListener);
                            popup.add(defaultItem);
                            defaultItem=new MenuItem("Open");
                            defaultItem.addActionListener(e12 -> {
                                frame.setVisible(true);
                                frame.setExtendedState(NORMAL);
                            });
                            popup.add(defaultItem);
                            trayIcon=new TrayIcon(image, "Insomnia", popup);
                            trayIcon.setImageAutoSize(true);
                        }
                    try {
                        if (!flag) {
                            tray.add(trayIcon);
                        }
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                    trayIcon.addActionListener(e1 -> {
                        frame.setExtendedState(NORMAL);
                        frame.setVisible(true);
                    });
                    frame.setVisible(false);
                    flag = true ;
                }
                else {
                    File req = new File("requests.txt");
                    File res = new File("responses.txt");
                    File list = new File("list.txt");
//                    req.delete();
//                    res.delete();
                    list.delete();
                    addToList();
                    System.exit(0);
                }
            }
            else if(e.getSource().equals(view1)){
                if (n % 2 == 1) {
                    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                }
                else {
                    frame.setExtendedState(NORMAL);
                }
                n++;
            }
            else if (e.getSource().equals(help1)){
                JFrame f2 = new JFrame("About Me");
                f2.setLocation(40, 40);
                f2.setLayout(null);
                f2.setSize(330, 300);
                f2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f2.setVisible(true);
                JLabel info = new JLabel(" Developer : Mohammad Reza Rahimian");
                info.setSize(new Dimension(300,20));
                info.setLocation(5,100);
                info.setForeground(new Color(63, 128, 48));
                info.setFont(new Font("Arial",Font.BOLD,14));
                JLabel info2 = new JLabel(" ID : 9831027");
                info2.setSize(new Dimension(300,20));
                info2.setLocation(5,120);
                info2.setFont(new Font("Arial",Font.BOLD,14));
                info2.setForeground(new Color(63, 128, 48));
                f2.add(info);
                f2.add(info2);
                f2.repaint();
            }
            else {
                try {
                    Desktop.getDesktop().browse(new URI("https://support.insomnia.rest/"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (URISyntaxException uriSyntaxException) {
                    uriSyntaxException.printStackTrace();
                }

            }
        }

        /**
         * save checkbox state in file
         */
        @Override
        public void itemStateChanged(ItemEvent e) {
            try {
                FileWriter myWriter = new FileWriter("filename.txt");
                if (check.getState()) {
                    myWriter.write("1");
                }
                else {
                    myWriter.write("0");
                }
                if (check2.getState()) {
                    myWriter.write("1");
                }
                else {
                    myWriter.write("0");
                }
                myWriter.close();
            } catch (IOException e1) {
                System.out.println("An error occurred.");
                e1.printStackTrace();
            }
        }
    }
}
