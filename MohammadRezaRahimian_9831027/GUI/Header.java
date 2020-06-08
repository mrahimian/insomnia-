package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import static GUI.MainFrame.frame;

public class Header extends JPanel{
        JLabel label;
        JTextField key,value;
        Checkbox check;
        JButton trash;
        LinkedHashMap<Header,Integer> headers;
        int headerCounter;
        TabHeader tabHeader;
        String keyName;
        ArrayList<Header> heads ;
        public Header(TabHeader tabHeader,LinkedHashMap<Header,Integer> headers,int headerCounter,String keyName) {
            this.keyName = keyName;
            this.tabHeader = tabHeader;
            this.headerCounter = headerCounter;
            this.headers = headers;
            this.setLayout(new FlowLayout());
            this.setBackground(new Color(40, 41, 37));
            this.setPreferredSize(new Dimension(157, 39));

            label = new JLabel(new ImageIcon("line.png"));
            key = new JTextField(keyName);

            key.setPreferredSize(new Dimension(157, 39));
            value = new JTextField("value");
            value.setPreferredSize(new Dimension(157, 39));
            AddAndRemoveHeader key1 = new AddAndRemoveHeader();
            AddAndRemoveHeader value1 = new AddAndRemoveHeader();
            key.addMouseListener(key1);
            key.addFocusListener(key1);
            value.addMouseListener(value1);
            value.addFocusListener(value1);
            //header active when true
            check = new Checkbox();
            check.setPreferredSize(new Dimension(20, 20));
            AddAndRemoveHeader check1 = new AddAndRemoveHeader();
            check.setState(true);
            check.addItemListener(check1);
            trash = new JButton(new ImageIcon("trash2.png"));
            trash.setPreferredSize(new Dimension(28, 28));
            AddAndRemoveHeader button = new AddAndRemoveHeader();
            trash.addActionListener(button);

            add(label);
            add(key);
            add(value);
            add(check);
            add(trash);
            headerCounter++;
            headers.put(this,headerCounter);
        }

    /**
     * convert headers or formData to ArrayList
     */
    public HashMap<String,String> getHeaders() {
        heads = new ArrayList<>() ;
        int rem = 0 ;
        for (Header i : headers.keySet()){
            heads.add(i);
            rem++;
        }
        heads.remove(rem-1);
        return getHeadersText(heads);
    }

    /**
     * @param headerArrayList headers or fooBody
     * @return key and value text
     */
    private HashMap<String,String> getHeadersText(ArrayList<Header> headerArrayList){
        HashMap<String,String> map = new HashMap<>();
        for (Header i : headerArrayList){
            map.put(i.key.getText(),i.value.getText());
        }
        return map;
    }

    /**
     * add new header
     */
    public Header addHeader(){
        Header header = new Header(tabHeader,headers,headerCounter,keyName);
        tabHeader.add(header);
        headerCounter++;
        headers.put(header,headerCounter);
        revalidate();
        return header ;
    }
        /**
         * add new header when clicking on last header
         */
        private class AddAndRemoveHeader extends MouseAdapter implements FocusListener, ItemListener, ActionListener {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (headers.get(e.getComponent().getParent()) == headers.size() && check.getState()) {
                    addHeader();
                    int n = 1;
                    for (Header i : headers.keySet()) {
                        headers.put(i, n);
                        n++;
                    }
                }
            }

            /**
             * clear text when focused and nothig is written
             * @param e focus on text field
             */
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getComponent().equals(key) && key.getText().equals(keyName) && check.getState()) {
                    key.setText("");
                } else if (e.getComponent().equals(value) && value.getText().equals("value") && check.getState()) {
                    value.setText("");
                }
            }

            /**
             * set tex boxes to default when nothing is written
             * @param e focus on text boxes
             */
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getComponent().equals(key)) {
                    if (key.getText().length() == 0) {
                        key.setText(keyName);
                    }
                } else {
                    if (value.getText().length() == 0) {
                        value.setText("value");
                    }
                }
            }

            /**
             * text fields become inactive when the checkBox is false
             *
             * @param e chick box is true or false
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 2) {
                    key.setEditable(false);
                    value.setEditable(false);
                } else {
                    key.setEditable(true);
                    value.setEditable(true);
                }
            }
            /**
             * delete header when clicking on trash
             *
             * @param e clicked on trash
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = 1;
                Iterator<Map.Entry<Header, Integer>> it = headers.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Header, Integer> h = it.next();
                    if (e.getSource().equals(h.getKey().trash) && headers.size() > 1) {
                        h.getKey().getParent().remove(h.getKey());
                        it.remove();
                        frame.revalidate();
                        frame.repaint();
                    }
                }
                for (Header i : headers.keySet()) {
                    headers.put(i, n);
                    n++;
                }
            }
        }
}
