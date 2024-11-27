package view;

import view.model.JATableModel;
import model.JobApplication;
import view.model.StarRenderer;
import view.model.StarEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableRowSorter;

public class MainView {
    private JFrame frame;
    private JTable jobTable;
    private JATableModel tableModel;
    private TableRowSorter<JATableModel> sorter;
    private JButton addButton;
    private JButton editButton;
    private JScrollPane scrollPane;
    
    ActionListener excitementUpdateListener;
    private boolean isRendererEditorSet = false;
    
    public static final Comparator<Object> NUMERIC_COMPARATOR = (o1, o2) -> {
        if (o1 instanceof Integer && o2 instanceof Integer) {
            return ((Integer) o1).compareTo((Integer) o2);
        }
        return 0; // Default equality if types don't match
    };

    public static final Comparator<Object> DATE_COMPARATOR = (o1, o2) -> {
        if (o1 instanceof java.sql.Date && o2 instanceof java.sql.Date) {
            return ((java.sql.Date) o1).compareTo((java.sql.Date) o2);
        }
        return 0; // Default equality if types don't match
    };

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
        header.setResizingAllowed(false);
        header.setFont(new Font("Inter", Font.BOLD, 14));
        header.setBackground(new Color(44, 62, 80)); // Dark blue
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));
        setProportionalColumnWidths(table);
        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setProportionalColumnWidths(table);
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                
                if (row != -1) {
                    if (column == 9) {
                        table.editCellAt(row, column); // Force cell editing
                        Component editor = table.getEditorComponent();
                        if (editor != null) {
                            MouseEvent editorClickEvent = SwingUtilities.convertMouseEvent(table, e, editor);
                            editor.dispatchEvent(editorClickEvent);
                            editor.requestFocus(); 
                        }
                        //table.clearSelection();
                    } else if (column == 4) { // Status column
                        table.editCellAt(row, column);
                        Component editor = table.getEditorComponent();
                        if (editor != null) {
                            editor.requestFocus();
                        }
                        //table.clearSelection();
                    } else {
                        // Select the row for non-editable columns
                        table.setRowSelectionInterval(row, row);
                    }
                }
            }
        });
        
        
    }
    public void setJobApplications(List<JobApplication> applications) {
        tableModel = new JATableModel(applications);
        jobTable.setModel(tableModel);
        
        // Initialize and attach the TableRowSorter
        sorter = new TableRowSorter<>(tableModel);
        jobTable.setRowSorter(sorter);
        
        sorter.setComparator(2, NUMERIC_COMPARATOR); // Salary column
        sorter.setComparator(5, DATE_COMPARATOR);    // Date Saved column
        sorter.setComparator(6, DATE_COMPARATOR);    // Deadline column
        sorter.setComparator(7, DATE_COMPARATOR);   // date applied
        sorter.setComparator(8, DATE_COMPARATOR);   // follow-up date

        jobTable.getColumnModel().getColumn(9).setCellRenderer(new StarRenderer());
        jobTable.getColumnModel().getColumn(9).setCellEditor(new StarEditor());
        
        

        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                
                // Check if the updated column is "excitement"
                if (column == 9) {
                    JobApplication updatedJob = tableModel.getJobAt(row);
                    triggerTableEditListener(updatedJob);
                }
            }
        });
        
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
            int modelRow = jobTable.convertRowIndexToModel(selectedRow);
            return tableModel.getJobAt(modelRow);
        }
        return null;
    }

    public void addAddButtonListener(ActionListener listener) {
        this.addButton.addActionListener(listener);
    }

    public void addEditButtonListener(ActionListener listener) {
        this.editButton.addActionListener(listener);
    }
    public void addTableEditListener(ActionListener listener) {
        this.excitementUpdateListener = listener;
    }
    public void triggerTableEditListener(JobApplication job){
        if(excitementUpdateListener != null) {
            excitementUpdateListener.actionPerformed(new ActionEvent(job, ActionEvent.ACTION_PERFORMED, "UpdateExcitement"));
        }
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
