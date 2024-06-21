/*

NOTE:
- Database connected with Attendance Page for HR/Manager


*/
package MPHpages.EmployeePortal;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class EmpAttendancePage extends javax.swing.JFrame {

    /**
     * Creates new form Attendance Page
     */
    private String employeeID; // Add a field to store the employee ID
    private String fullName;
    private final String userRole; // Add a field to store the user role
   
     
    /**
     * Creates new form Attendance Page
     * @param fullName
     * @param employeeID
     * @param userRole
     * 
     */
    
    public EmpAttendancePage(String fullName, String employeeID, String userRole) {
        initComponents();
        this.fullName = fullName;
        this.employeeID = employeeID;
        this.userRole = userRole;
        
        // Display employee information in the labels
        inputIDLBL.setText(employeeID);
        inputempnameattLBL.setText(fullName);
    
        // Load the CSV and filter by employee ID
        loadCSV("src/CSV/attendance record.csv");
    }
    
     // Constructor without arguments for initializing components and adding action listener
    public EmpAttendancePage() {
        initComponents();
        backbuttonattendancePB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttonattendancePBActionPerformed(evt);
            }
        });
        loadCSV("src/CSV/attendance record.csv");
        this.userRole = null;
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
                
                // Assume the first column is the Employee ID
                if (data.length > 0 && data[0].equals(employeeID)) {
                model.addRow(data);
                }
            }

            // Adjust row heights
            adjustRowHeight();

             // Make table non-editable and adjust column sizes
             for (int i = 0; i < model.getColumnCount(); i++) {
                TableColumn column = TBattendance.getColumnModel().getColumn(i);
                column.setCellEditor(null);
        }

            TBattendance.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        attendancemainPNL = new javax.swing.JPanel();
        attendanceheaderPNL = new javax.swing.JPanel();
        attendanceheaderLBL = new javax.swing.JLabel();
        backbuttonattendancePB = new javax.swing.JButton();
        employeeinfoPNL1 = new javax.swing.JPanel();
        inputIDLBL = new javax.swing.JLabel();
        idnumLBL = new javax.swing.JLabel();
        attendanceTP = new javax.swing.JTabbedPane();
        attendanceSP = new javax.swing.JScrollPane();
        TBattendance = new javax.swing.JTable();
        employeeinfoPNL2 = new javax.swing.JPanel();
        inputempnameattLBL = new javax.swing.JLabel();
        empnameattLBL = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        attendancemainPNL.setBackground(new java.awt.Color(204, 204, 204));

        attendanceheaderPNL.setBackground(new java.awt.Color(102, 102, 102));

        attendanceheaderLBL.setBackground(new java.awt.Color(102, 102, 102));
        attendanceheaderLBL.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        attendanceheaderLBL.setForeground(new java.awt.Color(255, 255, 255));
        attendanceheaderLBL.setText("ATTENDANCE RECORD");

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
                .addGap(223, 223, 223)
                .addComponent(attendanceheaderLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        attendanceheaderPNLLayout.setVerticalGroup(
            attendanceheaderPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, attendanceheaderPNLLayout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(attendanceheaderPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attendanceheaderLBL)
                    .addComponent(backbuttonattendancePB, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        employeeinfoPNL1.setBackground(new java.awt.Color(153, 153, 153));

        inputIDLBL.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N

        idnumLBL.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        idnumLBL.setForeground(new java.awt.Color(51, 51, 51));
        idnumLBL.setText("ID#: ");

        javax.swing.GroupLayout employeeinfoPNL1Layout = new javax.swing.GroupLayout(employeeinfoPNL1);
        employeeinfoPNL1.setLayout(employeeinfoPNL1Layout);
        employeeinfoPNL1Layout.setHorizontalGroup(
            employeeinfoPNL1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeinfoPNL1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(idnumLBL)
                .addGap(30, 30, 30)
                .addComponent(inputIDLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        employeeinfoPNL1Layout.setVerticalGroup(
            employeeinfoPNL1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeeinfoPNL1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(employeeinfoPNL1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(idnumLBL, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(inputIDLBL, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addContainerGap())
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

        employeeinfoPNL2.setBackground(new java.awt.Color(153, 153, 153));

        inputempnameattLBL.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N

        empnameattLBL.setBackground(new java.awt.Color(51, 51, 51));
        empnameattLBL.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        empnameattLBL.setForeground(new java.awt.Color(51, 51, 51));
        empnameattLBL.setText("Name: ");

        javax.swing.GroupLayout employeeinfoPNL2Layout = new javax.swing.GroupLayout(employeeinfoPNL2);
        employeeinfoPNL2.setLayout(employeeinfoPNL2Layout);
        employeeinfoPNL2Layout.setHorizontalGroup(
            employeeinfoPNL2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeinfoPNL2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(empnameattLBL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inputempnameattLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        employeeinfoPNL2Layout.setVerticalGroup(
            employeeinfoPNL2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeinfoPNL2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(employeeinfoPNL2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(empnameattLBL, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(inputempnameattLBL, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout attendancemainPNLLayout = new javax.swing.GroupLayout(attendancemainPNL);
        attendancemainPNL.setLayout(attendancemainPNLLayout);
        attendancemainPNLLayout.setHorizontalGroup(
            attendancemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(attendanceheaderPNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(attendancemainPNLLayout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(employeeinfoPNL1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(employeeinfoPNL2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, attendancemainPNLLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(attendanceTP, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(147, 147, 147))
        );
        attendancemainPNLLayout.setVerticalGroup(
            attendancemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attendancemainPNLLayout.createSequentialGroup()
                .addComponent(attendanceheaderPNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(attendancemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employeeinfoPNL1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeinfoPNL2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attendanceTP, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(attendancemainPNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(attendancemainPNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backbuttonattendancePBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbuttonattendancePBActionPerformed

        // Create an instance of EmployeePortal
        EmployeePortal employeePortal = new EmployeePortal(fullName, employeeID, userRole);
        // Center the EmployeePortal
        employeePortal.setLocationRelativeTo(null);
        // Make the EmployeePortal visible
        employeePortal.setVisible(true);
        // Close the current Attendance Page
        this.dispose();
    }//GEN-LAST:event_backbuttonattendancePBActionPerformed

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
            java.util.logging.Logger.getLogger(EmpAttendancePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpAttendancePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpAttendancePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpAttendancePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpAttendancePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TBattendance;
    private javax.swing.JScrollPane attendanceSP;
    private javax.swing.JTabbedPane attendanceTP;
    private javax.swing.JLabel attendanceheaderLBL;
    private javax.swing.JPanel attendanceheaderPNL;
    private javax.swing.JPanel attendancemainPNL;
    private javax.swing.JButton backbuttonattendancePB;
    private javax.swing.JPanel employeeinfoPNL1;
    private javax.swing.JPanel employeeinfoPNL2;
    private javax.swing.JLabel empnameattLBL;
    private javax.swing.JLabel idnumLBL;
    private javax.swing.JLabel inputIDLBL;
    private javax.swing.JLabel inputempnameattLBL;
    // End of variables declaration//GEN-END:variables

}
