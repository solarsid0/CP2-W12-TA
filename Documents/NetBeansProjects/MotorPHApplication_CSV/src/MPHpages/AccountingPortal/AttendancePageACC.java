
package MPHpages.AccountingPortal;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class AttendancePageACC extends javax.swing.JFrame {
    
    private final String fullName; // Add a field to store the full name
    private final String employeeID; // Add a field to store the employee ID from login page
    private final String userRole; // Add a field to store the user role
    /**
     * Creates new form EmployeeDetails
     * @param fullName
     * @param employeeID
     * @param userRole
     */
    public AttendancePageACC(String fullName, String employeeID, String userRole) {
        initComponents();
        this.fullName = fullName; // Store full name
        this.employeeID = employeeID;
        this.userRole = userRole;
        loadCSV("src/CSV/attendance record.csv");
        }

    private AttendancePageACC() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
     private void loadCSV(String filename) {
        String line;
        DefaultTableModel model = (DefaultTableModel) TBattendance.getModel();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // Read the CSV header
            line = br.readLine();
            String[] headers = line.split(",");

            // Set table headers
            model.setColumnIdentifiers(headers);

            // Read remaining lines
            while ((line = br.readLine()) != null) {
                String[] data = parseCSVLine(line); // Use parseCSVLine to handle commas within fields
                model.addRow(data);
            }

            // Adjust row heights
            adjustRowHeight();

            // Make table non-editable
            for (int i = 0; i < model.getColumnCount(); i++) {
                TableColumn column = TBattendance.getColumnModel().getColumn(i);
                column.setCellEditor(null);
            }

            // Make table non-resizable
            TBattendance.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            // Resize table columns to fit content
            for (int i = 0; i < model.getColumnCount(); i++) {
                TableColumn column = TBattendance.getColumnModel().getColumn(i);
                int width = getColumnWidth(i);
                column.setPreferredWidth(width);
                column.setMinWidth(width);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Parse CSV line
    private String[] parseCSVLine(String line) {
        // Pattern to match fields enclosed in double quotes, allowing for commas within them
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(?<=,|^)([^,]*)(?:,|$)");

        Matcher matcher = pattern.matcher(line);
        List<String> fields = new ArrayList<>();
        while (matcher.find()) {
            String field = matcher.group(1); // Quoted value
            if (field == null) {
                field = matcher.group(2); // Unquoted value
            }
            fields.add(field);
        }

        return fields.toArray(String[]::new);
    }

    // Adjust row heights to fit content
    private void adjustRowHeight() {
        for (int row = 0; row < TBattendance.getRowCount(); row++) {
            int rowHeight = TBattendance.getRowHeight();
            for (int column = 0; column < TBattendance.getColumnCount(); column++) {
                Component comp = TBattendance.prepareRenderer(TBattendance.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            TBattendance.setRowHeight(row, rowHeight);
        }
    }

    // Get the preferred width of a column based on the content
    private int getColumnWidth(int column) {
        int width = 150;
        for (int row = 0; row < TBattendance.getRowCount(); row++) {
            TableCellRenderer renderer = TBattendance.getCellRenderer(row, column);
            Component comp = TBattendance.prepareRenderer(renderer, row, column);
            width = Math.max(comp.getPreferredSize().width + TBattendance.getIntercellSpacing().width, width);
        }
        return width;
   
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        attendancemainPNL = new javax.swing.JPanel();
        attendanceheaderPNL = new javax.swing.JPanel();
        attendanceheaderLBL = new javax.swing.JLabel();
        backbuttonattendancePB = new javax.swing.JButton();
        employeeinfoPNL = new javax.swing.JPanel();
        idnumLBL = new javax.swing.JLabel();
        TFinputid = new javax.swing.JTextField();
        attendanceTP = new javax.swing.JTabbedPane();
        attendanceSP = new javax.swing.JScrollPane();
        TBattendance = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        attendancemainPNL.setBackground(new java.awt.Color(204, 204, 204));

        attendanceheaderPNL.setBackground(new java.awt.Color(102, 102, 102));

        attendanceheaderLBL.setFont(new java.awt.Font("Helvetica", 1, 24)); // NOI18N
        attendanceheaderLBL.setForeground(new java.awt.Color(255, 255, 255));
        attendanceheaderLBL.setText("ATTENDANCE RECORDS");

        backbuttonattendancePB.setBackground(new java.awt.Color(204, 0, 0));
        backbuttonattendancePB.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        backbuttonattendancePB.setForeground(new java.awt.Color(255, 255, 255));
        backbuttonattendancePB.setText("Back");
        backbuttonattendancePB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttonattendancePBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout attendanceheaderPNLLayout = new javax.swing.GroupLayout(attendanceheaderPNL);
        attendanceheaderPNL.setLayout(attendanceheaderPNLLayout);
        attendanceheaderPNLLayout.setHorizontalGroup(
            attendanceheaderPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, attendanceheaderPNLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backbuttonattendancePB, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(181, 181, 181)
                .addComponent(attendanceheaderLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        attendanceheaderPNLLayout.setVerticalGroup(
            attendanceheaderPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, attendanceheaderPNLLayout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(attendanceheaderPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(backbuttonattendancePB, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(attendanceheaderLBL))
                .addGap(34, 34, 34))
        );

        idnumLBL.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        idnumLBL.setText("ID#: ");

        TFinputid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFinputidActionPerformed(evt);
            }
        });
        TFinputid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TFinputidKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout employeeinfoPNLLayout = new javax.swing.GroupLayout(employeeinfoPNL);
        employeeinfoPNL.setLayout(employeeinfoPNLLayout);
        employeeinfoPNLLayout.setHorizontalGroup(
            employeeinfoPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeeinfoPNLLayout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addComponent(TFinputid, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
            .addGroup(employeeinfoPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(employeeinfoPNLLayout.createSequentialGroup()
                    .addGap(36, 36, 36)
                    .addComponent(idnumLBL)
                    .addContainerGap(242, Short.MAX_VALUE)))
        );
        employeeinfoPNLLayout.setVerticalGroup(
            employeeinfoPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeinfoPNLLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(TFinputid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(employeeinfoPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(employeeinfoPNLLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(idnumLBL, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        TBattendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee ID", "Date", "Time In", "Time Out"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        attendanceSP.setViewportView(TBattendance);

        attendanceTP.addTab("Log", attendanceSP);

        javax.swing.GroupLayout attendancemainPNLLayout = new javax.swing.GroupLayout(attendancemainPNL);
        attendancemainPNL.setLayout(attendancemainPNLLayout);
        attendancemainPNLLayout.setHorizontalGroup(
            attendancemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(attendanceheaderPNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(attendancemainPNLLayout.createSequentialGroup()
                .addGroup(attendancemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(attendancemainPNLLayout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(employeeinfoPNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(attendancemainPNLLayout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(attendanceTP, javax.swing.GroupLayout.PREFERRED_SIZE, 629, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(145, Short.MAX_VALUE))
        );
        attendancemainPNLLayout.setVerticalGroup(
            attendancemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attendancemainPNLLayout.createSequentialGroup()
                .addComponent(attendanceheaderPNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(employeeinfoPNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attendanceTP, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(attendancemainPNL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(attendancemainPNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backbuttonattendancePBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbuttonattendancePBActionPerformed
        // Create an instance of Accounting Portal Page
        AccountingPortal accountingPage = new AccountingPortal(fullName, employeeID, userRole);
        // Center the Accounting Portal Page
        accountingPage.setLocationRelativeTo(null);
        // Make the Accounting Portal visible
        accountingPage.setVisible(true);
        // Close the current Employee Details Acc Page
        this.dispose();
    }//GEN-LAST:event_backbuttonattendancePBActionPerformed

    private void TFinputidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFinputidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFinputidActionPerformed

    private void TFinputidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFinputidKeyReleased
    
        DefaultTableModel obj=(DefaultTableModel) TBattendance.getModel();
        TableRowSorter<DefaultTableModel> obj1=new TableRowSorter<>(obj);
        TBattendance.setRowSorter(obj1);
        obj1.setRowFilter(RowFilter.regexFilter(TFinputid.getText()));
        
    }//GEN-LAST:event_TFinputidKeyReleased

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
            java.util.logging.Logger.getLogger(AttendancePageACC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AttendancePageACC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AttendancePageACC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AttendancePageACC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AttendancePageACC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TBattendance;
    private javax.swing.JTextField TFinputid;
    private javax.swing.JScrollPane attendanceSP;
    private javax.swing.JTabbedPane attendanceTP;
    private javax.swing.JLabel attendanceheaderLBL;
    private javax.swing.JPanel attendanceheaderPNL;
    private javax.swing.JPanel attendancemainPNL;
    private javax.swing.JButton backbuttonattendancePB;
    private javax.swing.JPanel employeeinfoPNL;
    private javax.swing.JLabel idnumLBL;
    // End of variables declaration//GEN-END:variables
}
