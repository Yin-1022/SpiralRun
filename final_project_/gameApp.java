import java.awt.KeyboardFocusManager;
import java.awt.im.InputContext;

import javax.swing.*;

public class gameApp 
{
    public static void main(String[] args) throws Exception 
    {    
        int windowWidth = 700;
        int windowHeight = 700;                   

        JFrame frame = new JFrame("Spiral Run");
        frame.setSize(windowWidth,windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
        SpiralRun spiralRun = new SpiralRun();        
        frame.add(spiralRun); 
        frame.pack();
        spiralRun.requestFocus();  
        frame.setVisible(true);
    }
}