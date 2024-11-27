package view.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.TableCellEditor;

public class StarEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    private int currentRating = 0;
    private int columnWidth = 0;
    private int rowHeight = 0;

    public StarEditor() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Calculate the star size dynamically
                int starSize = Math.min(columnWidth / 5, rowHeight);

                // Determine the new rating based on the click position
                int x = e.getX();
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

        // Dynamically fetch the column width and row height
        columnWidth = table.getColumnModel().getColumn(column).getWidth();
        rowHeight = table.getRowHeight(row);

        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return currentRating;
    }
}
