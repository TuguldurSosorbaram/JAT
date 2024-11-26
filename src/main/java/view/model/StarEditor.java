package view.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.TableCellEditor;

public class StarEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    private int currentRating = 0;
    
    public StarEditor() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int starSize = 24; // Same size as renderer
                currentRating = Math.min(5, Math.max(1, x / starSize + 1));
                stopCellEditing();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Integer) {
            currentRating = (Integer) value;
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return currentRating;
    }
}
