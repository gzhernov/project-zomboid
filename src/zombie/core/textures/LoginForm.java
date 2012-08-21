// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LoginForm.java

package zombie.core.textures;

import javax.swing.*;

public class LoginForm extends JPanel
{

    public LoginForm(AbstractAction loginAction)
    {
        instance = this;
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        labKey = new JLabel("Desura CD-KEY");
        setBounds(0, 0, 600, 200);
        labUserName = new JLabel("User name");
        tfUserName = new JTextField(15);
        tfKey = new JTextField(22);
        labPassword = new JLabel("Password");
        tfPassword = new JPasswordField(15);
        labEnterDesura = new JLabel("Enter your Desura Key:");
        labOrPassword = new JLabel("Or Project Zomboid username/password:");
        bnLogin = new JButton(loginAction);
        bnLogin.setDefaultCapable(true);
        bnOffline = new JButton(loginAction);
        bnOffline.setDefaultCapable(true);
        String command = (String)loginAction.getValue("ActionCommandKey");
        if(command == null || (command = command.trim()).equals(""))
            command = "Log in";
        bnLogin.setText(command);
        bnOffline.setText("Play Offline");
        bnOffline.setVisible(false);
        add(labEnterDesura);
        add(tfKey);
        add(labOrPassword);
        add(labUserName);
        add(tfUserName);
        add(labPassword);
        add(tfPassword);
        add(bnLogin);
        add(bnOffline);
        layout.putConstraint("North", labEnterDesura, 15, "North", this);
        layout.putConstraint("West", labEnterDesura, 10, "West", this);
        layout.putConstraint("North", labOrPassword, 75, "North", this);
        layout.putConstraint("West", labOrPassword, 10, "West", this);
        layout.putConstraint("North", labKey, 40, "North", this);
        layout.putConstraint("North", tfKey, 40, "North", this);
        layout.putConstraint("West", tfKey, 10, "West", this);
        layout.putConstraint("West", labUserName, 10, "West", this);
        layout.putConstraint("North", labUserName, 100, "North", this);
        layout.putConstraint("North", tfUserName, 100, "North", this);
        layout.putConstraint("West", tfUserName, 20, "East", labUserName);
        layout.putConstraint("West", labPassword, 10, "West", this);
        layout.putConstraint("North", labPassword, 125, "North", this);
        layout.putConstraint("North", tfPassword, 125, "North", this);
        layout.putConstraint("West", tfPassword, 20, "East", labUserName);
        layout.putConstraint("North", bnLogin, 150, "North", this);
        layout.putConstraint("West", bnLogin, 0, "West", labUserName);
        layout.putConstraint("North", bnOffline, 150, "North", this);
        layout.putConstraint("West", bnOffline, 0, "West", labUserName);
        layout.putConstraint("South", this, 20, "South", bnLogin);
        layout.putConstraint("East", this, 5, "East", tfPassword);
    }

    public String getUserName()
    {
        return tfUserName.getText();
    }

    public char[] getPassword()
    {
        return tfPassword.getPassword();
    }

    public void clear()
    {
        tfUserName.setText("");
        tfPassword.setText("");
    }

    public static String version = "0.2.0q";
    private static final String LOGIN_COMMAND = "Log in";
    private JLabel labUserName;
    public JTextField tfUserName;
    public JTextField tfKey;
    private JLabel labPassword;
    private JLabel labEnterDesura;
    private JLabel labOrPassword;
    private JLabel labKey;
    public JPasswordField tfPassword;
    public JButton bnLogin;
    public JButton bnOffline;
    public static LoginForm instance;

}
