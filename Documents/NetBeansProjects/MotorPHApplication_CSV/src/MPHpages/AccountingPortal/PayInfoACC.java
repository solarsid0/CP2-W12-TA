package MPHpages.AccountingPortal;  

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


public class PayInfoACC extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private final String fullName; // Add a field to store the full name
    private final String employeeID; // Add a field to store the employee ID from login page
    private final String userRole; // Add a field to store the user role
    
    /**
     * Creates new form EmployeeDetails
     * @param fullName
     * @param employeeID
     * @param userRole
     */
    
    public PayInfoACC(String fullName, String employeeID, String userRole) {
        this.fullName = fullName; // Store full name
        this.employeeID = employeeID;
        this.userRole = userRole;
        initComponents();
        initializeTable();
        setupActionListeners();
        
    }

    private PayInfoACC(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void initializeTable() {
        tableModel = new DefaultTableModel();
        empinfotable.setModel(tableModel);
    }

    private void setupActionListeners() {
        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });

        yearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });

        empnumberlist.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateTable();
            }
        });

        generatePDFbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePayslip();
            }
        });
    }
    
     private void updateTable() {
        String month = (String) monthComboBox.getSelectedItem();
        String year = (String) yearComboBox.getSelectedItem();
        String employeeId = empnumberlist.getSelectedValue();

        if (month != null && year != null && employeeId != null) {
            loadDataFromCSV(month, year, employeeId);
        }
    }

    private void loadDataFromCSV(String month, String year, String employeeId) {
        String fileName = "src/CSV/Payslip Info v2.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            Vector<String> columnNames = new Vector<>();
            Vector<Vector<Object>> data = new Vector<>();

            if ((line = br.readLine()) != null) {
                String[] headers = line.split(",");
                for (String header : headers) {
                    columnNames.add(header);
                }
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String csvMonth = values[values.length - 2];
                String csvYear = values[values.length - 1];

                if (values[0].equals(employeeId) && csvMonth.equals(month) && csvYear.equals(year)) {
                    Vector<Object> row = new Vector<>();
                    for (String value : values) {
                        row.add(value);
                    }
                    data.add(row);
                }
            }

            tableModel.setDataVector(data, columnNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generatePayslip() {
         Document document = new Document();

        try {
            if (empinfotable.getRowCount() > 0) {
                int row = empinfotable.getSelectedRow() == -1 ? 0 : empinfotable.getSelectedRow();

                String empID = empinfotable.getValueAt(row, 0).toString();
                String month = empinfotable.getValueAt(row, 11).toString();
                String year = empinfotable.getValueAt(row, 12).toString();

                String defaultFileName = "Payslip_" + empID + "_" + month + "_" + year + ".pdf"; 

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Payslip");
                fileChooser.setSelectedFile(new java.io.File(defaultFileName));
                int userSelection = fileChooser.showSaveDialog(this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!filePath.endsWith(".pdf")) {
                        filePath += ".pdf";
                    }

                    PdfWriter.getInstance(document, new FileOutputStream(filePath));
                    document.open();
                
                    // Add logo and header text
                    PdfPTable headerTable = new PdfPTable(2);
                    headerTable.setWidthPercentage(100);
                    headerTable.setWidths(new int[]{1, 4}); // Adjust the width of the columns

                    Image logo = Image.getInstance(getClass().getResource("/media/MPH LOGO 200 X 112.png"));
                    logo.scaleToFit(90, 90); // Adjust the size of the logo
                    PdfPCell logoCell = new PdfPCell(logo);
                    logoCell.setBorder(PdfPCell.NO_BORDER);
                    headerTable.addCell(logoCell);

                    PdfPCell textCell = new PdfPCell(new Paragraph("MotorPH - Country's Top Motorcycle Online Dealership", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLDITALIC)));
                    textCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    textCell.setBorder(PdfPCell.NO_BORDER);
                    headerTable.addCell(textCell);

                    document.add(headerTable);
                    document.add(new Paragraph("\n"));
                    document.add(new Paragraph("=========================================================================="));
                    document.add(new Paragraph("\n"));

                    // Title
                    Paragraph title = new Paragraph("Payslip Information", new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD));
                    title.setAlignment(Paragraph.ALIGN_CENTER);
                    document.add(title);
                    document.add(new Paragraph("\n"));

                    // Employee Details
                    String lastName = empinfotable.getValueAt(row, 1).toString();
                    String firstName = empinfotable.getValueAt(row, 2).toString();
                    String supervisor = empinfotable.getValueAt(row, 3).toString();

                    document.add(new Paragraph("Employee ID: " + empID, new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("Last Name: " + lastName, new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("First Name: " + firstName, new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("Supervisor: " + supervisor, new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("\n"));

                    // Payslip Information
                    document.add(new Paragraph("---Payslip Information---", new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD)));
                    document.add(new Paragraph("Total Allowance: " + empinfotable.getValueAt(row, 4).toString(), new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("Gross Pay: " + empinfotable.getValueAt(row, 5).toString(), new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("Net Pay: " + empinfotable.getValueAt(row, 6).toString(), new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("\n"));

                    // Contributions
                    document.add(new Paragraph("---Contributions---", new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD)));
                    document.add(new Paragraph("SSS Contribution: " + empinfotable.getValueAt(row, 7).toString(), new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("HMO Contribution: " + empinfotable.getValueAt(row, 8).toString(), new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("Pagibig Contribution: " + empinfotable.getValueAt(row, 9).toString(), new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("\n"));

                    // Taxable Income
                    document.add(new Paragraph("Taxable Income: " + empinfotable.getValueAt(row, 10).toString(), new Font(Font.FontFamily.HELVETICA, 9)));
                    document.add(new Paragraph("\n"));

                    // Month and Year
                    document.add(new Paragraph("---Covers the following month and year---", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
                    document.add(new Paragraph("Month: " + month, new Font(Font.FontFamily.HELVETICA, 8)));
                    document.add(new Paragraph("Year: " + year, new Font(Font.FontFamily.HELVETICA, 8)));

                    document.close();

                    JOptionPane.showMessageDialog(this, "Payslip saved successfully!");
                }
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }


         
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        monthComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        yearComboBox = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        empinfotable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        empnumberlist = new javax.swing.JList<>();
        backbuttondetailsPB = new javax.swing.JButton();
        generatePDFbutton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("WELCOME TO THE PAYROLL INFORMATION PAGE!");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(264, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(283, 283, 283))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Select Month");

        monthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Select Year");

        yearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2020", "2021", "2022", "2023", "2024" }));

        empinfotable.setFont(new java.awt.Font("Helvetica", 1, 12)); // NOI18N
        empinfotable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Emp Number", "Last Name", "First Name", "Supervisor", "Total Allow.", "Gross Pay", "Net Pay", "SSS Contr.", "HMO Cont.", "Pagibig Cont.", "Tax. income", "With. Tax"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(empinfotable);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Select Employee");

        empnumberlist.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "10001", "10002", "10003", "10004", "10005", "10006", "10007", "10008", "10009", "10010", "10011", "10012", "10013", "10014", "10015", "10016", "10017", "10018", "10019", "10020", "10021", "10022", "10023", "10024", "10025" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(empnumberlist);

        backbuttondetailsPB.setBackground(new java.awt.Color(204, 0, 0));
        backbuttondetailsPB.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        backbuttondetailsPB.setForeground(new java.awt.Color(255, 255, 255));
        backbuttondetailsPB.setText("Back");
        backbuttondetailsPB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttondetailsPBActionPerformed(evt);
            }
        });

        generatePDFbutton.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        generatePDFbutton.setText("Generate Payslip");
        generatePDFbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generatePDFbuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backbuttondetailsPB, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(generatePDFbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(backbuttondetailsPB, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(generatePDFbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backbuttondetailsPBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbuttondetailsPBActionPerformed
        // Create an instance of Accounting Portal Page
        AccountingPortal accountingPage = new AccountingPortal(fullName, employeeID, userRole);
        // Center the Accounting Portal Page
        accountingPage.setLocationRelativeTo(null);
        // Make the Accounting Portal visible
        accountingPage.setVisible(true);
        // Close the current Employee Details Acc Page
       this.dispose();
    }//GEN-LAST:event_backbuttondetailsPBActionPerformed

    private void generatePDFbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generatePDFbuttonActionPerformed
        
          generatePayslip();                                    
    }//GEN-LAST:event_generatePDFbuttonActionPerformed


    
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PayInfoACC frame = new PayInfoACC(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backbuttondetailsPB;
    private javax.swing.JTable empinfotable;
    private javax.swing.JList<String> empnumberlist;
    private javax.swing.JButton generatePDFbutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> monthComboBox;
    private javax.swing.JComboBox<String> yearComboBox;
    // End of variables declaration//GEN-END:variables
}