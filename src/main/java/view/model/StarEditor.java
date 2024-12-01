package view.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.TableCellEditor;

/**
 * StarEditor provides a custom cell editor for rendering and updating star ratings in a JTable.
 * Users can click on the stars to set a new rating, which updates the cell value.
 */
public class StarEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Panel for rendering stars
    private int currentRating = 0; // Current star rating
    private int columnWidth = 0; // Width of the column
    private int rowHeight = 0; // Height of the row

    /**
     * Constructs the StarEditor and sets up mouse interaction for updating ratings.
     */
    public StarEditor() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Calculate the size of each star dynamically
                int starSize = Math.min(columnWidth / 5, rowHeight);

                // Determine the new rating based on the click position
                int x = e.getX();
                currentRating = Math.min(5, Math.max(1, x / starSize + 1));

                // Stop editing to commit the new value
                stopCellEditing();
            }
        });
    }

    /**
     * Returns the component for editing the cell.
     *
     * @param table      the JTable that is asking the editor to edit.
     * @param value      the value of the cell to be edited.
     * @param isSelected true if the cell is to be rendered with highlighting.
     * @param row        the row of the cell being edited.
     * @param column     the column of the cell being edited.
     * @return the editor component for the cell.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Integer) {
            currentRating = (Integer) value;
        }

        // Get the dimensions of the column and row for dynamic rendering
        columnWidth = table.getColumnModel().getColumn(column).getWidth();
        rowHeight = table.getRowHeight(row);

        return panel;
    }

    /**
     * Returns the current value of the cell being edited.
     *
     * @return the current star rating as an integer.
     */
    @Override
    public Object getCellEditorValue() {
        return currentRating;
    }
}
