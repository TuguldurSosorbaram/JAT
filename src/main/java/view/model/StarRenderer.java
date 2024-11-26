package view.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class StarRenderer extends DefaultTableCellRenderer {
    private final Icon filledStar;
    private final Icon emptyStar;

    public StarRenderer() {
        // Load the original large icons
        ImageIcon filledIcon = new ImageIcon(getClass().getResource("/icons/filled_star.png"));
        ImageIcon emptyIcon = new ImageIcon(getClass().getResource("/icons/empty_star.png"));

        // Resize icons to fit in the table cell
        int targetSize = 24; // Desired size in pixels
        filledStar = new ImageIcon(filledIcon.getImage().getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH));
        emptyStar = new ImageIcon(emptyIcon.getImage().getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH));
    }

    @Override
    protected void setValue(Object value) {
        if (value instanceof Integer) {
            int excitement = (Integer) value;
            setText(""); // Clear text
            setHorizontalAlignment(SwingConstants.CENTER); // Center align
            setIcon(createStarIcon(excitement));
        } else {
            super.setValue(value);
        }
    }

    private Icon createStarIcon(int rating) {
        int starSize = filledStar.getIconWidth();
        int totalStars = 5;

        // Create a combined image for all stars
        BufferedImage starImage = new BufferedImage(starSize * totalStars, starSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = starImage.createGraphics();

        for (int i = 0; i < totalStars; i++) {
            Icon star = (i < rating) ? filledStar : emptyStar;
            star.paintIcon(null, g2d, i * starSize, 0);
        }
        g2d.dispose();

        return new ImageIcon(starImage);
    }
}
