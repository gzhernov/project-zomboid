// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AuthenticatingFrame.java

package zombie.core.textures;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import javax.swing.*;
import zombie.FrameLoader;
import zombie.GameWindow;
import zombie.gameStates.LoginState;
import zombie.gameStates.MainScreenState;

// Referenced classes of package zombie.core.textures:
//            LoginForm

public class AuthenticatingFrame extends JFrame
{
    private class Authenticator extends AbstractAction
    {

        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().contains("ay Of"))
            {
                LoginState.LoggedIn = true;
                AuthenticatingFrame.frame.setVisible(false);
                AuthenticatingFrame.frame.dispose();
                return;
            }
            if(allow(loginForm.getUserName(), loginForm.getPassword(), loginForm.tfKey.getText()))
            {
                LoginState.LoggedIn = true;
                File makefile = new File((new StringBuilder()).append(GameWindow.getCacheDir()).append(File.separator).append("2133243254543.log").toString());
                try
                {
                    if(makefile.exists())
                    {
                        makefile.delete();
                        makefile.createNewFile();
                    } else
                    {
                        makefile.createNewFile();
                    }
                    FileWriter fwrite = new FileWriter(makefile);
                    String pass = new String(loginForm.getPassword());
                    fwrite.write((new StringBuilder()).append("username:").append(loginForm.getUserName()).toString());
                    fwrite.write((new StringBuilder()).append("\npassword:").append(pass).toString());
                    fwrite.write((new StringBuilder()).append("\nkey:").append(loginForm.tfKey.getText()).toString());
                    fwrite.flush();
                    fwrite.close();
                }
                catch(Exception ex) { }
                String filename = "couldnotm.txt";
                try
                {
                    String str = null;
                    if(k != null && !k.isEmpty())
                        str = (new StringBuilder()).append(k).append("48093248fdsfd").append(MainScreenState.Version).toString();
                    else
                        str = (new StringBuilder()).append(loginForm.getUserName()).append(new String(loginForm.getPassword())).append("48093248fdsfd").append(MainScreenState.Version).toString();
                    MessageDigest md = null;
                    md = MessageDigest.getInstance("MD5");
                    md.reset();
                    md.update(str.getBytes());
                    byte thedigest[] = md.digest();
                    BigInteger bigInt = new BigInteger(1, thedigest);
                    String hashtext;
                    for(hashtext = bigInt.toString(16); hashtext.length() < 32; hashtext = (new StringBuilder()).append("0").append(hashtext).toString());
                    filename = (new StringBuilder()).append(hashtext).append(".k").append("e").append("y").toString();
                }
                catch(Exception ex) { }
                File makefile2 = new File((new StringBuilder()).append(GameWindow.getCacheDir()).append(File.separator).append(filename).toString());
                try
                {
                    if(makefile2.exists())
                    {
                        makefile2.delete();
                        makefile2.createNewFile();
                    } else
                    {
                        makefile2.createNewFile();
                    }
                    FileWriter fwrite = new FileWriter(makefile2);
                    fwrite.flush();
                    fwrite.close();
                }
                catch(Exception ex) { }
                AuthenticatingFrame.frame.setVisible(false);
                AuthenticatingFrame.frame.dispose();
            } else
            {
                AuthenticatingFrame.tries++;
                if(AuthenticatingFrame.tries > 2)
                {
                    AuthenticatingFrame.frame.setVisible(false);
                    AuthenticatingFrame.frame.dispose();
                    FrameLoader.closeRequested = true;
                } else
                {
                    AuthenticatingFrame.frame.loginForm.clear();
                }
            }
        }

        

        private Authenticator()
        {
           
        }

    }


    public AuthenticatingFrame()
    {
        u = null;
        p = null;
        k = null;
    }

    public AuthenticatingFrame(String u, String p, String k)
    {
        this.u = null;
        this.p = null;
        this.k = null;
        this.u = u;
        this.p = p;
        this.k = k;
    }

    public void setGui()
    {
    	frame = this;
    	/*     */     try {
    	/*  47 */       setDefaultCloseOperation(3);
    	/*  48 */       this.cards = new JPanel(new CardLayout());
    	/*  49 */       setContentPane(this.cards);
    	/*  50 */       this.loginForm = new LoginForm(new Authenticator());
    	/*     */ 
    	/*  52 */       if (this.u != null)
    	/*     */       {
    	/*  54 */         this.loginForm.tfUserName.setText(this.u);
    	/*  55 */         this.loginForm.tfPassword.setText(this.p);
    	/*  56 */         this.loginForm.tfKey.setText(this.k);
    	/*     */         try
    	/*     */         {
    	/*  59 */           String str = null;
    	/*     */ 
    	/*  61 */           if ((this.k != null) && (!this.k.isEmpty()))
    	/*  62 */             str = this.k + "48093248fdsfd" + MainScreenState.Version;
    	/*     */           else {
    	/*  64 */             str = this.loginForm.getUserName() + new String(this.loginForm.getPassword()) + "48093248fdsfd" + MainScreenState.Version;
    	/*     */           }
    	/*  66 */           MessageDigest md = null;
    	/*     */ 
    	/*  68 */           md = MessageDigest.getInstance("MD5");
    	/*     */ 
    	/*  70 */           md.reset();
    	/*  71 */           md.update(str.getBytes());
    	/*  72 */           byte[] thedigest = md.digest();
    	/*     */ 
    	/*  74 */           BigInteger bigInt = new BigInteger(1, thedigest);
    	/*  75 */           String hashtext = bigInt.toString(16);
    	/*     */ 
    	/*  77 */           while (hashtext.length() < 32) {
    	/*  78 */             hashtext = "0" + hashtext;
    	/*     */           }
    	/*  80 */           String filename = hashtext + ".k" + "e" + "y";
    	/*     */ 
    	/*  82 */           File file = new File(GameWindow.getCacheDir() + File.separator + filename);
    	/*     */ 
    	/*  84 */           if (file.exists())
    	/*     */           {
    	/*  86 */             this.loginForm.bnOffline.setVisible(true);
    	/*  87 */             this.loginForm.bnLogin.setVisible(false);
    	/*  88 */             zombie.gameStates.LoginState.LoggedIn = true;
    	/*  89 */             frame.setVisible(false);
    	/*  90 */             frame.dispose();



    	/*  91 */             return;
    	/*     */           }
    	/*     */ 
    	/*     */         }
    	/*     */         catch (Exception ex)
    	/*     */         {
    	/*  97 */           ex.printStackTrace();
    	/*     */         }
    	/*     */       }




    	/* 100 */       this.contentPane = new JPanel();
    	/* 101 */       this.cards.add(this.loginForm, "login");
    	/* 102 */       this.cards.add(this.contentPane, "main");

    	/*     */     } catch (Exception e) {
    	/* 104 */       e.printStackTrace();
    	/*     */     }
    }

    public Container getContentPane()
    {
        return contentPane;
    }

    public boolean allow(String userName, char password[], String key)
    {
        return true;
    }

    public static final String MAIN_PANEL = "main";
    public static final String LOGIN_PANEL = "login";
    private JPanel cards;
    private JPanel contentPane;
    private LoginForm loginForm;
    String u;
    String p;
    String k;
    public static AuthenticatingFrame frame = null;
    public static int tries = 0;


}
