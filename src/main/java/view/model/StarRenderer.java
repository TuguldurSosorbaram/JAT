package view.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * StarRenderer is a custom table cell renderer for displaying star ratings in a JTable.
 * It dynamically scales the star icons based on the cell size and renders filled and empty stars.
 */
public class StarRenderer extends DefaultTableCellRenderer {
    private final Icon filledStar; // Icon for a filled star
    private final Icon emptyStar;  // Icon for an empty star
    private int columnWidth = 0;   // Dynamic column width
    private int rowHeight = 0;     // Dynamic row height

    /**
     * Constructs the StarRenderer and initializes the star icons.
     */
    public StarRenderer() {
        // Load the original large icons
        ImageIcon filledIcon = new ImageIcon(getClass().getResource("/icons/filled_star.png"));
        ImageIcon emptyIcon = new ImageIcon(getClass().getResource("/icons/empty_star.png"));

        int targetSize = 24; // Default size for icons
        filledStar = new ImageIcon(filledIcon.getImage().getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH));
        emptyStar = new ImageIcon(emptyIcon.getImage().getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH));
    }

    /**
     * Configures the renderer for a specific cell in the table.
     *
     * @param table      the JTable that uses this renderer.
     * @param value      the value of the cell to be rendered.
     * @param isSelected true if the cell is selected.
     * @param hasFocus   true if the cell has focus.
     * @param row        the row of the cell.
     * @param column     the column of the cell.
     * @return the configured renderer component.
     */
    @Override
    public java.awt.Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        // Retrieve the current column width and row height dynamically
        columnWidth = table.getColumnModel().getColumn(column).getWidth();
        rowHeight = table.getRowHeight(row);

        // Apply default table rendering (selection background, etc.)
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Integer) {
            int excitement = (Integer) value;
            setText(""); // Clear text
            setHorizontalAlignment(SwingConstants.CENTER); // Center align
            setIcon(createStarIcon(excitement)); // Set the dynamic star icon
        }

        return this;
    }

    /**
     * Creates a composite star icon based on the rating and dynamically calculated star size.
     *
     * @param rating the number of filled stars (1-5).
     * @return an Icon representing the star rating.
     */
    private Icon createStarIcon(int rating) {
        // Calculate star size dynamically
        int starSize = Math.min(columnWidth / 5, rowHeight);
        int totalStars = 5;

        // Create a combined image for all stars
        BufferedImage starImage = new BufferedImage(starSize * totalStars, starSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = starImage.createGraphics();

        for (int i = 0; i < totalStars; i++) {
            Icon star = (i < rating) ? filledStar : emptyStar;

            // Scale the star to the dynamically calculated size
            Image scaledImage = ((ImageIcon) star).getImage().getScaledInstance(starSize, starSize, Image.SCALE_SMOOTH);
            Icon scaledStar = new ImageIcon(scaledImage);

            // Paint the star
            scaledStar.paintIcon(null, g2d, i * starSize, 0);
        }
        g2d.dispose();

        return new ImageIcon(starImage);
    }
}
