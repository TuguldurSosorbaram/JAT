package view.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class StarRenderer extends DefaultTableCellRenderer {
    private final Icon filledStar;
    private final Icon emptyStar;
    private int columnWidth = 0;
    private int rowHeight = 0;


    public StarRenderer() {
        // Load the original large icons
        ImageIcon filledIcon = new ImageIcon(getClass().getResource("/icons/filled_star.png"));
        ImageIcon emptyIcon = new ImageIcon(getClass().getResource("/icons/empty_star.png"));
        
        int targetSize = 24; // Desired size in pixels
        filledStar = new ImageIcon(filledIcon.getImage().getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH));
        emptyStar = new ImageIcon(emptyIcon.getImage().getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH));
    }
    
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
            setIcon(createStarIcon(excitement));
        }

        return this;
    }

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
