// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LaunchDialog.java

package zombie.core;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import zombie.GameWindow;

public class LaunchDialog extends JDialog
{

    public LaunchDialog(Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        setLocation(x, y);
        setVisible(true);
    }

    private void initComponents()
    {
        jComboBox1 = new JComboBox();
        jLabel1 = new JLabel();
        jComboBox2 = new JComboBox();
        jLabel2 = new JLabel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        setDefaultCloseOperation(2);
        setTitle("Project Zomboid 0.2.0r Launch Options");
        setAlwaysOnTop(true);
        setResizable(false);
        jComboBox1.setModel(new DefaultComboBoxModel(new String[] {
            "Low", "Medium", "High"
        }));
        jComboBox1.setSelectedIndex(2);
        jLabel1.setText("Graphics:");
        jComboBox2.setModel(new DefaultComboBoxModel(new String[] {
            "Windowed", "1024 x 768", "1280 x 720", "1280 x 800", "1280 x 960", "1280 x 1024", "1366 x 768", "1400 x 1050", "1440 x 900", "1600 x 1200", 
            "1680 x 1050", "1920 x 1080", "1920 x 1200", "2560 x 1440"
        }));
        jLabel2.setText("Resolution:");
        jButton1.setText("Quit");
        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }

          

            
         
        }
);
        jButton2.setText("Run");
        jButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }

           

         
        }
);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(37, 32767).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel1).addComponent(jLabel2)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jComboBox1, 0, -1, 32767).addComponent(jComboBox2, 0, 161, 32767)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(50, 50, 50).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBox1, -2, -1, -2).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBox2, -2, -1, -2).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, 32767).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jButton2)).addContainerGap()));
        pack();
    }

    private void jButton2ActionPerformed(ActionEvent evt)
    {
        d = this;
        SwingUtilities.invokeLater(new Runnable() {

            public void run()
            {
                String split[] = LaunchDialog.d.jComboBox2.getSelectedItem().toString().split("x");
                dispose();
                LaunchDialog _tmp = LaunchDialog.d;
                LaunchDialog.split = split;
                LaunchDialog _tmp1 = LaunchDialog.d;
                LaunchDialog.lev = LaunchDialog.d.jComboBox1.getSelectedItem().toString();
            }

           
        }
);
    }

    private void jButton1ActionPerformed(ActionEvent evt)
    {
        dispose();
        System.exit(0);
    }

    public static void main(String args[])
    {
        try
        {
            javax.swing.UIManager.LookAndFeelInfo arr$[] = UIManager.getInstalledLookAndFeels();
            int len$ = arr$.length;
            int i$ = 0;
            do
            {
                if(i$ >= len$)
                    break;
                javax.swing.UIManager.LookAndFeelInfo info = arr$[i$];
                if("Nimbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
                i$++;
            } while(true);
        }
        catch(ClassNotFoundException ex)
        {
            Logger.getLogger(LaunchDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(InstantiationException ex)
        {
            Logger.getLogger(LaunchDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IllegalAccessException ex)
        {
            Logger.getLogger(LaunchDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(LaunchDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        LaunchDialog dialog = new LaunchDialog(new JFrame(), true);
        dialog.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

        }
);
        int level = 0;
        if("High".equals(lev))
            level = 5;
        if("Medium".equals(lev))
            level = 3;
        if(split.length == 1)
            try
            {
                GameWindow.main(false, 1024, 768, level);
            }
            catch(Exception ex)
            {
                Logger.getLogger(LaunchDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        else
            try
            {
                GameWindow.main(true, Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()), level);
            }
            catch(Exception ex)
            {
                Logger.getLogger(LaunchDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private static String lev;
    static LaunchDialog d;
    public static String split[] = null;
    private JButton jButton1;
    private JButton jButton2;
    private JComboBox jComboBox1;
    private JComboBox jComboBox2;
    private JLabel jLabel1;
    private JLabel jLabel2;






}
