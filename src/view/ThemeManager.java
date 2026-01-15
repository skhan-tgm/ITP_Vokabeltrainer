package src.view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class ThemeManager {

    public enum Theme { LIGHT, DARK }

    private Theme current = Theme.LIGHT;

    public Theme getCurrent() {
        return current;
    }

    public void apply(JFrame frame, Theme theme) {
        this.current = theme;

        Color bg, fg, panel, fieldBg, buttonBg, buttonFg;

        if (theme == Theme.DARK) {
            bg = new Color(30, 30, 30);
            panel = new Color(40, 40, 40);
            fg = new Color(230, 230, 230);
            fieldBg = new Color(55, 55, 55);

            buttonBg = new Color(70, 70, 70);
            buttonFg = Color.WHITE; // gut lesbar
        } else {
            bg = UIManager.getColor("Panel.background");
            panel = UIManager.getColor("Panel.background");
            fg = Color.BLACK;
            fieldBg = Color.WHITE;

            buttonBg = UIManager.getColor("Button.background");
            buttonFg = Color.BLACK;
        }

        applyRecursive(frame.getContentPane(), bg, panel, fg, fieldBg, buttonBg, buttonFg);

        SwingUtilities.updateComponentTreeUI(frame);
        frame.repaint();
    }

    private void applyRecursive(Component comp,
                                Color bg,
                                Color panel,
                                Color fg,
                                Color fieldBg,
                                Color buttonBg,
                                Color buttonFg) {

        if (comp instanceof JPanel) {
            comp.setBackground(panel);
            comp.setForeground(fg);
        } else if (comp instanceof JScrollPane) {
            comp.setBackground(panel);
            comp.setForeground(fg);
        } else if (comp instanceof JTable table) {
            table.setBackground(bg);
            table.setForeground(fg);
            table.setGridColor(panel);

            JTableHeader header = table.getTableHeader();
            if (header != null) {
                header.setBackground(panel);
                header.setForeground(fg);
            }
        } else if (comp instanceof JTextComponent tc) {
            tc.setBackground(fieldBg);
            tc.setForeground(fg);
            tc.setCaretColor(fg);
        } else if (comp instanceof JComboBox<?> box) {
            box.setBackground(fieldBg);
            box.setForeground(fg);
        } else if (comp instanceof JButton || comp instanceof JToggleButton) {
            comp.setBackground(buttonBg);
            comp.setForeground(buttonFg);
        } else if (comp instanceof JLabel || comp instanceof JCheckBox) {
            comp.setForeground(fg);
        } else {
            comp.setBackground(bg);
            comp.setForeground(fg);
        }

        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyRecursive(child, bg, panel, fg, fieldBg, buttonBg, buttonFg);
            }
        }
    }
}
