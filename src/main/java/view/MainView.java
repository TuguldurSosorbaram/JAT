package view;

import view.model.JATableModel;
import model.JobApplication;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class MainView {
    private JFrame frame;
    private JTable jobTable;
    private JATableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JScrollPane scrollPane;

    public MainView() {
        // Frame setup
        frame = new JFrame("Job Application Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        
        //Parent panel for header and buttons
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        
        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(52, 152, 219)); // Light blue color
        JLabel headerLabel = new JLabel("Job Application Tracker");
        headerLabel.setFont(new Font("Inter", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        // Add headerPanel to the top of the northPanel
        northPanel.add(headerPanel);

        // Button panel (placed above the table)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(236, 240, 241)); // Light grey background

        addButton = new JButton("Add Job");
        addButton.setFont(new Font("Inter", Font.PLAIN, 16));
        addButton.setIcon(new ImageIcon("icons/add-icon.png")); // Replace with actual icon path

        editButton = new JButton("Edit Job");
        editButton.setFont(new Font("Inter", Font.PLAIN, 16));
        editButton.setIcon(new ImageIcon("icons/edit-icon.png")); // Replace with actual icon path

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);

        // Add buttonPanel below the header
        northPanel.add(buttonPanel);
        
        // Add the northPanel to the frame
        frame.add(northPanel, BorderLayout.NORTH);

        // Table panel
        jobTable = new JTable();
        jobTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jobTable.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(jobTable);
        // Customize table appearance
        customizeTableAppearance(jobTable);
        
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    private void customizeTableAppearance(JTable table) {
        table.getTableHeader().setResizingAllowed(false);
        table.setRowHeight(50); 

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            label.setFont(new Font("Inter", Font.PLAIN, 14)); // Bigger font
            label.setOpaque(true);

            if (column == 0 || column == 1) { // Position or Company Name
                label.setText("<html><body style='width: 100%'>" + (value == null ? "-" : value.toString()) + "</body></html>");
            } else {
                label.setText(value == null ? "-" : value.toString());
            }
            
            // Alternate row colors
            if (!isSelected) {
                label.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
            }

            return label;
        }
        });

        // Header customization
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Inter", Font.BOLD, 14));
        header.setBackground(new Color(44, 62, 80)); // Dark blue
        header.setForeground(Color.WHITE);
        
        setProportionalColumnWidths(table);
        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setProportionalColumnWidths(table);
            }
        });
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 60));
        
        
    }

    public void setJobApplications(List<JobApplication> applications) {
        tableModel = new JATableModel(applications);
        jobTable.setModel(tableModel);
        jobTable.clearSelection(); // Deselect any selected row
        scrollPane.getVerticalScrollBar().setValue(0); // Scroll to the top
    }
    
    private void setProportionalColumnWidths(JTable table) {
        int totalWidth = table.getWidth();
        int columnCount = table.getColumnModel().getColumnCount();

        float[] columnWidthPercentage = {
            0.15f, // Position
            0.15f, // Company Name
            0.1f,  // Salary
            0.1f,  // Location
            0.1f,  // Status
            0.1f,  // Date Saved
            0.1f,  // Deadline
            0.1f,  // Date Applied
            0.1f,  // Follow-Up
            0.1f   // Excitement
        };

        // Set each column's preferred width proportionally
        for (int i = 0; i < columnCount; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(Math.round(columnWidthPercentage[i] * totalWidth));
        }
    }

    public int getSelectedRow() {
        return jobTable.getSelectedRow();
    }

    public JobApplication getSelectedJobApplication() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getJobAt(selectedRow);
        }
        return null;
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    public void showView() {
        frame.setVisible(true);
    }

    public void hideView() {
        frame.setVisible(false);
    }

    public void disposeView() {
        frame.dispose();
    }

    public JFrame getFrame() {
        return this.frame;
    }
}
