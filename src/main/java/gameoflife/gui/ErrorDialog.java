package gameoflife.gui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ErrorDialog extends JDialog{
    
    public ErrorDialog(String message){
    
        setModal(true);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        dispose();
    }
}
