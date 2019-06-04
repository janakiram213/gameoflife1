package gameoflife.gui;

import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

//for displaying filenames in the combobox, not full path

public class ComboBoxRenderer  extends DefaultListCellRenderer{

    @Override
    public Component getListCellRendererComponent(JList<?> list,Object value,int index, boolean isSelected,boolean cellHasFocus) {
        
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) { setText(((File) value).getName()); }
        return this;
    }
}
