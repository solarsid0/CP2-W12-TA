
package MPHpages.HR_ManagerPortal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowFilter;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellRenderer;

public class LeaveManagement extends javax.swing.JFrame {
    
    private final String fullName; // Add a field to store the full name
    private final String employeeID; // Add a field to store the employee ID from login page
    private final String userRole; // Add a field to store the user role
    
    /**
     * Creates new form EmployeeDetails
     * @param fullName
     * @param employeeID
     * @param userRole
     */
    
    public LeaveManagement(String fullName, String employeeID, String userRole) {
        initComponents();
        this.fullName = fullName; // Store full name
        this.employeeID = employeeID;
        this.userRole = userRole;

    // Populate the table
    populateJTable();
    
    // Adjust column widths
    resizeColumnWidths();
        
    }
    
    // Method to calculate leave days between start and end dates
        public int calculateLeaveDays(String startDateStr, String endDateStr) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Corrected date format
            int leaveDays = 0;

            try {
                Date startDate = dateFormat.parse(startDateStr);
                Date endDate = dateFormat.parse(endDateStr);

                // Calculate leave days (assuming 1 day = 24 hours)
                long diffMilliseconds = endDate.getTime() - startDate.getTime();
                long diffDays = diffMilliseconds / (24 * 60 * 60 * 1000); // Convert milliseconds to days
                leaveDays = (int) diffDays + 1; // Include both start and end days
            } catch (ParseException e) {
                // Handle parsing exception
                e.printStackTrace(); // Or log the error
            }

            return leaveDays;
}
    
    private LeaveManagement() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Method to populate JTable with data from CSV
    private void populateJTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        File file = new File("src/CSV/Leave Requests DB.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Read the header line
            if (line != null) {
                while ((line = reader.readLine()) != null) {
                    String[] data = parseCSVLine(line);
                    if (data.length >= 14) {
                        model.addRow(new Object[]{
                            data[0],  // Date
                            data[1],  // EID
                            data[2],  // First Name
                            data[3],  // Last Name
                            data[4],  // Position
                            data[5],  // Employment Status
                            data[6],  // Supervisor
                            data[7],  // TOP
                            data[8],  // Note
                            data[9],  // Start
                            data[10], // End
                            data[11], // Leave Status
                            data[12], // Remaining Vacation Leave
                            data[13]  // Remaining Sick Leave
                        });
                    } else {
                        System.out.println("Skipping malformed line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

       // Sort rows based on the "Leave Status" column
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        jTable1.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(11, SortOrder.ASCENDING)); // Sort by "Leave Status" column
        sorter.setSortKeys(sortKeys);

        // Set custom cell renderer for the "Leave Status" column
        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(11).setCellRenderer(new StatusCellRenderer());
    }

    // Method to parse CSV line into array of fields
    private String[] parseCSVLine(String line) {
        String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String[] fields = line.split(regex);
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].replaceAll("^\"|\"$", "").replace("\"\"", "\"");
        }
        return fields;
    }

    // Custom cell renderer for the "Leave Status" column
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) value;
            switch (status) {
                case "Pending":
                    cell.setBackground(Color.YELLOW);
                    break;
                case "Accepted":
                    cell.setBackground(Color.GREEN);
                    break;
                case "Denied":
                    cell.setBackground(Color.RED);
                    break;
                default:
                    cell.setBackground(table.getBackground());
                    break;
            }
            return cell;
        }
    }

    // Method to resize column widths based on content
    private void resizeColumnWidths() {
        TableColumnModel columnModel = jTable1.getColumnModel();
        int totalWidth = 0;

        for (int column = 0; column < jTable1.getColumnCount(); column++) {
            int width = 50; // Minimum width for safety
            TableCellRenderer headerRenderer = jTable1.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(jTable1, columnModel.getColumn(column).getHeaderValue(), false, false, 0, column);
            width = Math.max(headerComp.getPreferredSize().width, width);

            for (int row = 0; row < jTable1.getRowCount(); row++) {
                TableCellRenderer renderer = jTable1.getCellRenderer(row, column);
                Component comp = jTable1.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }

            columnModel.getColumn(column).setPreferredWidth(width);
            totalWidth += width; // Sum up the total width
        }

        // Set a preferred size that forces horizontal scrolling if necessary
        Dimension viewportSize = jScrollPane1.getViewport().getSize();
        int preferredWidth = Math.max(viewportSize.width, totalWidth);
        jTable1.setPreferredScrollableViewportSize(new Dimension(preferredWidth, jTable1.getPreferredScrollableViewportSize().height));
    }

    // Method to update CSV file with current table data including leave credits
private void updateCSVFile() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    File file = new File("src/CSV/Leave Requests DB.csv");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        // Write the header line
        writer.write("Date,EID,First Name,Last Name,Position,Employment Status,Supervisor,TOP,Note,Start,End,Leave Status,Remaining Vacation Leave,Remaining Sick Leave");
        writer.newLine();

        // Write the table data
        for (int i = 0; i < model.getRowCount(); i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < model.getColumnCount(); j++) {
                String value = model.getValueAt(i, j).toString();
                // Escape commas and quotes in the value
                value = value.replace("\"", "\"\"");
                if (value.contains(",") || value.contains("\"")) {
                    value = "\"" + value + "\"";
                }
                line.append(value);
                if (j < model.getColumnCount() - 1) {
                    line.append(",");
                }
            }
            writer.write(line.toString());
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error updating CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        backbuttonreqleavePB = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LEAVE MANAGEMENT");

        backbuttonreqleavePB.setBackground(new java.awt.Color(204, 0, 0));
        backbuttonreqleavePB.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        backbuttonreqleavePB.setForeground(new java.awt.Color(255, 255, 255));
        backbuttonreqleavePB.setText("Back");
        backbuttonreqleavePB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttonreqleavePBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(backbuttonreqleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(298, 298, 298)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(423, 423, 423))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(backbuttonreqleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(33, 33, 33))
        );

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Search by Employee ID:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Search by Status:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Pending", "Denied", "Accepted" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 204, 0));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Accept");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(204, 0, 0));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Deny");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setHorizontalScrollBar(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "EID", "First Name", "Last Name", "Position", "Emp Status", "Supervisor", "TOP", "Note", "Start", "End", "Leave Status", "VL Remaining", "SL Remaining"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setAutoscrolls(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jComboBox1, 0, 112, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 528, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
     {
    String searchText = jTextField1.getText().trim();
    if (!searchText.isEmpty()) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        jTable1.setRowSorter(sorter);
        
        RowFilter<Object, Object> filter = RowFilter.regexFilter(searchText, 1); // 1 corresponds to the column index of 'EID'
        sorter.setRowFilter(filter);
    } else {
        jTable1.setRowSorter(null); // Reset row sorter to display all rows when search text is empty
    }
}
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        { String selectedStatus = jComboBox1.getSelectedItem().toString();
    if (selectedStatus.equals("Select")) {
        // If "Select" is chosen, reset the row sorter to display all rows
        jTable1.setRowSorter(null);
    } else {
        // Create a row filter to display only rows with the selected status
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) jTable1.getModel());
        RowFilter<Object, Object> filter = RowFilter.regexFilter(selectedStatus, 11); // 11 is the column index of "Status"
        sorter.setRowFilter(filter);
        jTable1.setRowSorter(sorter);
    }
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      // Accept button action performed
       int selectedRow = jTable1.getSelectedRow();
    if (selectedRow != -1) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int modelRow = jTable1.convertRowIndexToModel(selectedRow);
        if ("Pending".equals(model.getValueAt(modelRow, 11))) { // 11 is the column index of "Leave Status"
            String typeOfLeave = model.getValueAt(modelRow, 7).toString(); // 7 is the column index of "TOP"
            String startDate = model.getValueAt(modelRow, 9).toString(); // 9 is the column index of "Start"
            String endDate = model.getValueAt(modelRow, 10).toString(); // 10 is the column index of "End"
            int leaveDays = calculateLeaveDays(startDate, endDate);

            if (typeOfLeave.equalsIgnoreCase("Vacation Leave")) {
                int remainingVL = Integer.parseInt(model.getValueAt(modelRow, 12).toString()); // 12 is the column index of "Remaining Vacation Leave"
                if (remainingVL >= leaveDays) {
                    model.setValueAt("Accepted", modelRow, 11); // Update leave status to "Accepted"
                    model.setValueAt(remainingVL - leaveDays, modelRow, 12); // Deduct vacation leave days in model
                    updateCSVFile(); // Update CSV file with updated model
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient Vacation Leave credits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (typeOfLeave.equalsIgnoreCase("Sick Leave")) {
                int remainingSL = Integer.parseInt(model.getValueAt(modelRow, 13).toString()); // 13 is the column index of "Remaining Sick Leave"
                if (remainingSL >= leaveDays) {
                    model.setValueAt("Accepted", modelRow, 11); // Update leave status to "Accepted"
                    model.setValueAt(remainingSL - leaveDays, modelRow, 13); // Deduct sick leave days in model
                    updateCSVFile(); // Update CSV file with updated model
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient Sick Leave credits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (typeOfLeave.equalsIgnoreCase("Maternity Leave") || typeOfLeave.equalsIgnoreCase("Paternity Leave")) {
                model.setValueAt("Accepted", modelRow, 11); // Update leave status to "Accepted"
                updateCSVFile(); // Update CSV file with updated model
            }
        } else {
            JOptionPane.showMessageDialog(this, "Status cannot be changed. Only 'Pending' requests can be accepted or denied.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select a row to change its status.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    // Deny button action performed
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow != -1) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int modelRow = jTable1.convertRowIndexToModel(selectedRow);
        // Check if the selected row has a "Pending" status
        if ("Pending".equals(model.getValueAt(modelRow, 11))) {
            // Change the status to "Denied"
            model.setValueAt("Denied", modelRow, 11); // 11 is the column index of Leave "Status"
        updateCSVFile();
        } else {
            // Display error message if the selected row does not have a "Pending" status
            JOptionPane.showMessageDialog(this, "Status cannot be changed. Only 'Pending' requests can be accepted or denied.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        // Display error message if no row is selected
        JOptionPane.showMessageDialog(this, "Please select a row to change its status.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void backbuttonreqleavePBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbuttonreqleavePBActionPerformed
        // Redirect to HR/Manager Portal
        HRManagerPortal hrmanagerPage = new HRManagerPortal(fullName, employeeID, userRole);
        // Center the HRManager Portal
        hrmanagerPage.setLocationRelativeTo(null);
        // Make the HRManager Portal visible
        hrmanagerPage.setVisible(true);
        // Close the current Attendance Page
        this.dispose();
    }//GEN-LAST:event_backbuttonreqleavePBActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LeaveManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaveManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaveManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaveManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LeaveManagement().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backbuttonreqleavePB;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
