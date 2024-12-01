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

/**
 * MainView is the primary user interface for managing job applications.
 * It displays job applications in a table and provides functionality for adding,
 * editing, deleting, and sorting applications.
 */
public class MainView {
    private JFrame frame;
    private JTable jobTable;
    private JATableModel tableModel;
    private TableRowSorter<JATableModel> sorter;
    private JButton addButton;
    private JButton editButton;
    private JButton logOutButton;
    private JButton deleteButton;
    private JScrollPane scrollPane;

    ActionListener excitementUpdateListener;
    private boolean isRendererEditorSet = false;

    /**
     * Comparator for sorting numeric values.
     */
    public static final Comparator<Object> NUMERIC_COMPARATOR = (o1, o2) -> {
        if (o1 instanceof Integer && o2 instanceof Integer) {
            return ((Integer) o1).compareTo((Integer) o2);
        }
        return 0;
    };

    /**
     * Comparator for sorting dates.
     */
    public static final Comparator<Object> DATE_COMPARATOR = (o1, o2) -> {
        if (o1 instanceof java.sql.Date && o2 instanceof java.sql.Date) {
            return ((java.sql.Date) o1).compareTo((java.sql.Date) o2);
        }
        return 0;
    };

    /**
     * Comparator for sorting job application statuses based on predefined order.
     */
    public static final Comparator<Object> STATUS_COMPARATOR = (o1, o2) -> {
        String[] statusOptions = {"Saved", "Applying", "Applied", "Interviewing",
                "Negotiating", "Accepted", "I withdrew",
                "No response", "Rejected"};

        int index1 = java.util.Arrays.asList(statusOptions).indexOf(o1);
        int index2 = java.util.Arrays.asList(statusOptions).indexOf(o2);

        if (index1 == -1 && index2 == -1) {
            return 0;
        } else if (index1 == -1) {
            return 1;
        } else if (index2 == -1) {
            return -1;
        }

        return Integer.compare(index1, index2);
    };

    /**
     * Constructs the MainView, setting up the frame, table, and buttons.
     */
    public MainView() {
        // Frame setup
        frame = new JFrame("Job Application Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        // North panel with header and buttons
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel headerLabel = new JLabel("Job Application Tracker");
        headerLabel.setFont(new Font("Inter", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        northPanel.add(headerPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(236, 240, 241));

        addButton = createButton("Add a New Job", "/icons/add_icon.png", new Color(6, 64, 43), Color.WHITE);
        editButton = createButton("Edit Selected Job", "/icons/edit_icon.png", Color.WHITE, new Color(6, 64, 43));
        deleteButton = createButton("Delete Selected Job", "/icons/delete_icon.png", Color.WHITE, new Color(6, 64, 43));
        logOutButton = createButton("Log Out", null, Color.WHITE, new Color(6, 64, 43));
        logOutButton.setPreferredSize(new Dimension(100, 30));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logOutButton);

        northPanel.add(buttonPanel);
        frame.add(northPanel, BorderLayout.NORTH);

        // Table panel
        jobTable = new JTable();
        jobTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jobTable.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(jobTable);
        customizeTableAppearance(jobTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(false);
    }

    /**
     * Creates a custom button with the specified properties.
     *
     * @param text       the button text
     * @param iconPath   the path to the icon (can be null)
     * @param bgColor    the background color
     * @param textColor  the text color
     * @return the customized JButton
     */
    private JButton createButton(String text, String iconPath, Color bgColor, Color textColor) {
        JButton button = new MyButton(text);
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            button.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        }
        button.setPreferredSize(new Dimension(200, 30));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        return button;
    }

    /**
     * Customizes the appearance of the job application table.
     *
     * @param table the JTable to customize
     */
    private void customizeTableAppearance(JTable table) {
        table.setRowHeight(60);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setFont(new Font("Inter", Font.PLAIN, 16));
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);

                if (column == 0 || column == 1) {
                    label.setText("<html><body style='width: 100%'>" + (value == null ? "-" : value.toString()) + "</body></html>");
                } else {
                    label.setText(value == null ? "-" : value.toString());
                }

                if (!isSelected) {
                    label.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                }

                return label;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setResizingAllowed(false);
        header.setFont(new Font("Inter", Font.BOLD, 16));
        header.setBackground(new Color(200, 200, 200));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));
        setProportionalColumnWidths(table);

        scrollPane.setBackground(Color.WHITE);
        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setProportionalColumnWidths(table);
            }
        });
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // Other methods for managing the view (unchanged)

    public void setJobApplications(List<JobApplication> applications) {
        tableModel = new JATableModel(applications);
        jobTable.setModel(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        jobTable.setRowSorter(sorter);

        sorter.setComparator(2, NUMERIC_COMPARATOR);
        sorter.setComparator(4, STATUS_COMPARATOR);
        sorter.setComparator(5, DATE_COMPARATOR);
        sorter.setComparator(6, DATE_COMPARATOR);
        sorter.setComparator(7, DATE_COMPARATOR);
        sorter.setComparator(8, DATE_COMPARATOR);

        jobTable.getColumnModel().getColumn(9).setCellRenderer(new StarRenderer());
        jobTable.getColumnModel().getColumn(9).setCellEditor(new StarEditor());

        JComboBox<String> statusDropdown = new JComboBox<>(new String[]{
            "Saved", "Applying", "Applied", "Interviewing", "Negotiating", 
            "Accepted", "I withdrew", "No response", "Rejected"
        });
        jobTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusDropdown));

        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == 9 || column == 4) {
                    JobApplication updatedJob = tableModel.getJobAt(row);
                    triggerTableEditListener(updatedJob);
                }
            }
        });

        jobTable.clearSelection();
        scrollPane.getVerticalScrollBar().setValue(0);
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

        for (int i = 0; i < columnCount; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(Math.round(columnWidthPercentage[i] * totalWidth));
        }
    }

    // Button and table listeners (unchanged)

    public void addAddButtonListener(ActionListener listener) {
        this.addButton.addActionListener(listener);
    }

    public void addEditButtonListener(ActionListener listener) {
        this.editButton.addActionListener(listener);
    }

    public void addLogOutButtonListener(ActionListener listener) {
        this.logOutButton.addActionListener(listener);
    }

    public void addDeleteButtonListener(ActionListener listener) {
        this.deleteButton.addActionListener(listener);
    }

    public void addTableEditListener(ActionListener listener) {
        this.excitementUpdateListener = listener;
    }

    public void triggerTableEditListener(JobApplication job) {
        if (excitementUpdateListener != null) {
            excitementUpdateListener.actionPerformed(new ActionEvent(job, ActionEvent.ACTION_PERFORMED, "UpdateExcitement"));
        }
    }

    public void showMessage(JFrame parent, String text) {
        JOptionPane.showMessageDialog(parent, text);
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

    /**
     * Custom button with rounded corners and a border.
     */
    public class MyButton extends JButton {
        public MyButton(String text) {
            super(text);
            setOpaque(false);
            setFont(new Font("Inter", Font.PLAIN, 16));
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            g2.setColor(new Color(6, 64, 63));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

            super.paintComponent(g2);
            g2.dispose();
        }
    }
}
