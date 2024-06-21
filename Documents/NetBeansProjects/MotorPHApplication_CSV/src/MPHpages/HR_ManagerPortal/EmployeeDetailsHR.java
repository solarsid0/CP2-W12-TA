/*
TO FIX:
- resize table columns (header labels can't be seen fully)

TO ADD:
- No duplication code for employee ID
- Error pop up for that duplication code
*/
package MPHpages.HR_ManagerPortal;

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
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;



public class EmployeeDetailsHR extends javax.swing.JFrame {
    
    private final String fullName; // Add a field to store the full name
    private final String employeeID; // Add a field to store the employee ID from login page
    private final String userRole; // Add a field to store the user role
    
    /**
     * Creates new form EmployeeDetails
     * @param fullName
     * @param employeeID
     */
    public EmployeeDetailsHR(String fullName, String employeeID, String userRole) {
     initComponents();
     this.fullName = fullName; // Store full name
     this.employeeID = employeeID;
     this.userRole = userRole;
     loadCSV("src/CSV/MotorPH Employees_empdetails.csv");
    }
    
    public EmployeeDetailsHR() {
     initComponents();
        
        
        
    // Add mouse listener to the table
     tblERecords.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int selectedRow = tblERecords.getSelectedRow();
            if (selectedRow != -1) {
                TFenum.setText(tblERecords.getValueAt(selectedRow, 0).toString());
                TFlastn.setText(tblERecords.getValueAt(selectedRow, 1).toString());
                TFfirstn.setText(tblERecords.getValueAt(selectedRow, 2).toString());
                TFsss.setText(tblERecords.getValueAt(selectedRow, 3).toString());
                TFphilh.setText(tblERecords.getValueAt(selectedRow, 4).toString());
                TFtin.setText(tblERecords.getValueAt(selectedRow, 5).toString());
                TFpagibig.setText(tblERecords.getValueAt(selectedRow, 6).toString());
                TFstatus.setText(tblERecords.getValueAt(selectedRow, 7).toString());
                TFpos.setText(tblERecords.getValueAt(selectedRow, 8).toString());
                TFsupervisor.setText(tblERecords.getValueAt(selectedRow, 9).toString());
                TFbasicsalary.setText(tblERecords.getValueAt(selectedRow, 10).toString());
                TFricesub.setText(tblERecords.getValueAt(selectedRow, 11).toString());
                TFphoneallow.setText(tblERecords.getValueAt(selectedRow, 12).toString());
                TFclothingallow.setText(tblERecords.getValueAt(selectedRow, 13).toString());
            }
        }
    });
        
     // Initialize fullName to an empty string or any default value
        this.fullName = "";
        this.employeeID = null;
        this.userRole = null;
    }
    
    private void loadCSV(String filename) {
        String line;
        DefaultTableModel model = (DefaultTableModel) tblERecords.getModel();

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
                TableColumn column = tblERecords.getColumnModel().getColumn(i);
                column.setCellEditor(null);
            }

            // Make table non-resizable
            tblERecords.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            // Resize table columns to fit content
            for (int i = 0; i < model.getColumnCount(); i++) {
                TableColumn column = tblERecords.getColumnModel().getColumn(i);
                int width = getColumnWidth(i);
                column.setPreferredWidth(width);
                column.setMaxWidth(width);
                column.setMinWidth(width);
            }

        } catch (Exception e) {
        }
    }

    // Parse CSV line
    private String[] parseCSVLine(String line) {
        // Pattern to match fields enclosed in double quotes, allowing for commas within them
        Pattern pattern = Pattern.compile("\"([^\"]*?)\"|(?<=,|^)([^,]*?)(?:,|$)");

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
        for (int row = 0; row < tblERecords.getRowCount(); row++) {
            int rowHeight = tblERecords.getRowHeight();
            for (int column = 0; column < tblERecords.getColumnCount(); column++) {
                Component comp = tblERecords.prepareRenderer(tblERecords.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            tblERecords.setRowHeight(row, rowHeight);
        }
    }

    // Get the preferred width of a column based on the content
    private int getColumnWidth(int column) {
        int width = 0;
        for (int row = 0; row < tblERecords.getRowCount(); row++) {
            TableCellRenderer renderer = tblERecords.getCellRenderer(row, column);
            Component comp = tblERecords.prepareRenderer(renderer, row, column);
            width = Math.max(comp.getPreferredSize().width + tblERecords.getIntercellSpacing().width, width);
        }
        return width;
        
    }
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        TFenum = new javax.swing.JTextField();
        TFpos = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TFlastn = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        TFtin = new javax.swing.JTextField();
        TFsupervisor = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        TFfirstn = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        TFsss = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        TFpagibig = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        TFphilh = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        TFstatus = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnReset1 = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblERecords = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        backbuttondetailsPB = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        btnSearch1 = new javax.swing.JButton();
        lblbasicsalary = new javax.swing.JLabel();
        TFbasicsalary = new javax.swing.JTextField();
        lblricesubsidy = new javax.swing.JLabel();
        TFricesub = new javax.swing.JTextField();
        lblphoneallow = new javax.swing.JLabel();
        TFphoneallow = new javax.swing.JTextField();
        lblclothingallow = new javax.swing.JLabel();
        TFclothingallow = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Employee Number:");

        TFenum.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFenum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFenumActionPerformed(evt);
            }
        });

        TFpos.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFpos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFposActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Position");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Last Name:");

        TFlastn.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFlastn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFlastnActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("TIN:");

        TFtin.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFtin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFtinActionPerformed(evt);
            }
        });

        TFsupervisor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFsupervisor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFsupervisorActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Immediate Supervisor:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("First Name:");

        TFfirstn.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFfirstn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFfirstnActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("SSS Number:");

        TFsss.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFsss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFsssActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("PAGIBIG Number:");

        TFpagibig.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFpagibig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFpagibigActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("PhilHealth Number:");

        TFphilh.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFphilh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFphilhActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Status:");

        TFstatus.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFstatusActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(102, 102, 102));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(102, 102, 102));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnReset1.setBackground(new java.awt.Color(102, 102, 102));
        btnReset1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnReset1.setForeground(new java.awt.Color(255, 255, 255));
        btnReset1.setText("Reset");
        btnReset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReset1ActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(102, 102, 102));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        tblERecords.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee #", "Last Name", "First Name", "SSS #", "PhilHealth #", "TIN", "Pag-Ibig #", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblERecords.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(tblERecords);
        if (tblERecords.getColumnModel().getColumnCount() > 0) {
            tblERecords.getColumnModel().getColumn(0).setMinWidth(60);
            tblERecords.getColumnModel().getColumn(0).setPreferredWidth(60);
            tblERecords.getColumnModel().getColumn(9).setPreferredWidth(60);
            tblERecords.getColumnModel().getColumn(10).setMinWidth(80);
            tblERecords.getColumnModel().getColumn(10).setPreferredWidth(80);
        }

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        backbuttondetailsPB.setBackground(new java.awt.Color(204, 0, 0));
        backbuttondetailsPB.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        backbuttondetailsPB.setForeground(new java.awt.Color(255, 255, 255));
        backbuttondetailsPB.setText("Back");
        backbuttondetailsPB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttondetailsPBActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("EMPLOYEE DETAILS (HR VIEW)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(backbuttondetailsPB, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(343, 343, 343))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backbuttondetailsPB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(21, 21, 21))
        );

        btnSearch1.setBackground(new java.awt.Color(102, 102, 102));
        btnSearch1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSearch1.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch1.setText("Search employee");
        btnSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearch1ActionPerformed(evt);
            }
        });

        lblbasicsalary.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblbasicsalary.setText("Basic Salary");

        TFbasicsalary.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFbasicsalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFbasicsalaryActionPerformed(evt);
            }
        });

        lblricesubsidy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblricesubsidy.setText("Rice Subsidy");

        TFricesub.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFricesub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFricesubActionPerformed(evt);
            }
        });

        lblphoneallow.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblphoneallow.setText("Phone Allowance");

        TFphoneallow.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFphoneallow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFphoneallowActionPerformed(evt);
            }
        });

        lblclothingallow.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblclothingallow.setText("Clothing Allowance");

        TFclothingallow.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFclothingallow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFclothingallowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addComponent(TFphilh, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(TFenum, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)
                                .addComponent(btnSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(TFpos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 451, Short.MAX_VALUE)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnReset1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(TFlastn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TFtin, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11)
                                            .addComponent(TFsupervisor, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel16)
                                            .addComponent(TFclothingallow, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblclothingallow)))
                                    .addComponent(jLabel4))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(TFfirstn, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                        .addComponent(jLabel12)
                                        .addComponent(TFpagibig))
                                    .addComponent(TFbasicsalary, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblbasicsalary)
                                    .addComponent(jLabel5))
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TFricesub, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblricesubsidy)
                                    .addComponent(jLabel13)
                                    .addComponent(TFstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(TFsss, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(24, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFphoneallow, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblphoneallow)
                            .addComponent(jLabel15)
                            .addComponent(jScrollPane1))
                        .addGap(24, 24, 24))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel9))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TFlastn)
                        .addComponent(TFenum)
                        .addComponent(TFfirstn))
                    .addComponent(TFsss, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFphilh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFtin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFpagibig, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFpos, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFsupervisor, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblbasicsalary)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TFbasicsalary, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblricesubsidy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TFricesub, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblphoneallow)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TFphoneallow, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblclothingallow)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TFclothingallow, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearch1)
                    .addComponent(btnSave)
                    .addComponent(btnDelete)
                    .addComponent(btnReset1)
                    .addComponent(btnUpdate))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void TFenumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFenumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFenumActionPerformed

    private void TFposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFposActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFposActionPerformed

    private void TFlastnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFlastnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFlastnActionPerformed

    private void TFtinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFtinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFtinActionPerformed

    private void TFsupervisorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFsupervisorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFsupervisorActionPerformed

    private void TFfirstnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFfirstnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFfirstnActionPerformed

    private void TFsssActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFsssActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFsssActionPerformed

    private void TFpagibigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFpagibigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFpagibigActionPerformed

    private void TFphilhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFphilhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFphilhActionPerformed

    private void TFstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFstatusActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
                    if (validateInput()) {
                    addEmployee();
                    saveToCSV();
                } else {
                    if (containsComma()) {
                        JOptionPane.showMessageDialog(this, "Commas are not allowed in any field.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Please fill in all fields to save the record.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            private boolean validateInput() {
                return !TFenum.getText().isEmpty() &&
                        !TFlastn.getText().isEmpty() &&
                        !TFfirstn.getText().isEmpty() &&  
                        !TFsss.getText().isEmpty() &&
                        !TFphilh.getText().isEmpty() &&
                        !TFtin.getText().isEmpty() &&
                        !TFpagibig.getText().isEmpty() &&
                        !TFstatus.getText().isEmpty() &&
                        !TFpos.getText().isEmpty() &&
                        !TFsupervisor.getText().isEmpty() &&
                        !TFbasicsalary.getText().isEmpty() &&
                        !TFricesub.getText().isEmpty() &&
                        !TFphoneallow.getText().isEmpty() &&
                        !TFclothingallow.getText().isEmpty() &&
                        !containsComma();
            }

            private boolean containsComma() {
                return TFenum.getText().contains(",") ||
                       TFlastn.getText().contains(",") ||
                       TFfirstn.getText().contains(",") ||
                       TFsss.getText().contains(",") ||
                       TFphilh.getText().contains(",") ||
                       TFtin.getText().contains(",") ||
                       TFpagibig.getText().contains(",") ||
                       TFstatus.getText().contains(",") ||
                       TFpos.getText().contains(",") ||
                       TFsupervisor.getText().contains(",")||
                       TFbasicsalary.getText().contains(",")||
                       TFricesub.getText().contains(",")||
                       TFphoneallow.getText().contains(",")||
                       TFclothingallow.getText().contains(",");
                        
            }

            private void addEmployee() {
                DefaultTableModel model = (DefaultTableModel) tblERecords.getModel();
                model.addRow(new Object[]{
                    TFenum.getText(),
                    TFlastn.getText(),
                    TFfirstn.getText(),
                    TFsss.getText(),
                    TFphilh.getText(),
                    TFtin.getText(),
                    TFpagibig.getText(),
                    TFstatus.getText(),
                    TFpos.getText(),
                    TFsupervisor.getText(),
                    TFbasicsalary.getText(),
                    TFricesub.getText(),
                    TFphoneallow.getText(),
                    TFclothingallow.getText(),
                });
                clearFields();
            }

            private void clearFields() {
                TFenum.setText("");
                TFlastn.setText("");
                TFfirstn.setText("");
                TFsss.setText("");
                TFphilh.setText("");
                TFtin.setText("");
                TFpagibig.setText("");
                TFstatus.setText("");
                TFpos.setText("");
                TFsupervisor.setText("");
                TFbasicsalary.setText("");
                TFricesub.setText("");
                TFphoneallow.setText("");
                TFclothingallow.setText("");
            }

            private void saveToCSV() {
                try {
                    DefaultTableModel model = (DefaultTableModel) tblERecords.getModel();
                    File file = new File("src/CSV/MotorPH Employees_empdetails.csv");

                    try (FileWriter output = new FileWriter(file);
                         CSVWriter writer = new CSVWriter(output,
                                 CSVWriter.DEFAULT_SEPARATOR,
                                 CSVWriter.NO_QUOTE_CHARACTER,
                                 CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                                 CSVWriter.DEFAULT_LINE_END)) {

                        // Write the headers to the CSV file
                        List<String> headers = new ArrayList<>();
                        for (int i = 0; i < model.getColumnCount(); i++) {
                            headers.add(model.getColumnName(i));
                        }
                        writer.writeNext(headers.toArray(String[]::new));

                        // Write the data to the CSV file
                        for (int i = 0; i < model.getRowCount(); i++) {
                            List<String> data = new ArrayList<>();
                            for (int j = 0; j < model.getColumnCount(); j++) {
                                Object value = model.getValueAt(i, j);
                                data.add(value != null ? value.toString() : "");
                            }
                            writer.writeNext(data.stream().map(str -> str.isEmpty() ? "" : str).toArray(String[]::new));
                        }
                    }
                } catch (IOException e) {
                }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblERecords.getModel();
            int selectedRow = tblERecords.getSelectedRow();
    
        // Remove the selected row from the table model
             if (selectedRow != -1) {
            model.removeRow(selectedRow);
             // Save the updated data to the CSV file
            saveToCSV();
         } else {
        JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnReset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReset1ActionPerformed
        // clear all the text fields
        clearFields();
    }//GEN-LAST:event_btnReset1ActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // Get the selected row index
        int selectedRow = tblERecords.getSelectedRow();

        // Ensure a row is selected
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) tblERecords.getModel();

            // Update the values in the table model
            model.setValueAt(TFenum.getText(), selectedRow, 0);
            model.setValueAt(TFlastn.getText(), selectedRow, 1);
            model.setValueAt(TFfirstn.getText(), selectedRow, 2);
            model.setValueAt(TFsss.getText(), selectedRow, 3);
            model.setValueAt(TFphilh.getText(), selectedRow, 4);
            model.setValueAt(TFtin.getText(), selectedRow, 5);
            model.setValueAt(TFpagibig.getText(), selectedRow, 6);
            model.setValueAt(TFstatus.getText(), selectedRow, 7);
            model.setValueAt(TFpos.getText(), selectedRow, 8);
            model.setValueAt(TFsupervisor.getText(), selectedRow, 9);
            model.setValueAt(TFbasicsalary.getText(), selectedRow, 10);
            model.setValueAt(TFricesub.getText(), selectedRow, 11);
            model.setValueAt(TFphoneallow.getText(), selectedRow, 12);
            model.setValueAt(TFclothingallow.getText(), selectedRow, 13);
       
            saveToCSV(); // Save changes after updating
            
        } else {
            // Show a message if no row is selected
            JOptionPane.showMessageDialog(this, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void backbuttondetailsPBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbuttondetailsPBActionPerformed
        // Redirect to HR/Manager Portal
        HRManagerPortal hrmanagerPage = new HRManagerPortal(fullName, employeeID, userRole);
        // Center the HRManager Portal
        hrmanagerPage.setLocationRelativeTo(null);
        // Make the HRManager Portal visible
        hrmanagerPage.setVisible(true);
        // Close the current Attendance Page
        this.dispose();
    }//GEN-LAST:event_backbuttondetailsPBActionPerformed

    private void btnSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearch1ActionPerformed
        // Get the search query from the text field
    String searchQuery = JOptionPane.showInputDialog(this, "Enter search query:");

    // Check if the search query is not empty
    if (searchQuery != null && !searchQuery.isEmpty()) {
        
    // Create a TableRowSorter to filter the table
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tblERecords.getModel());
        tblERecords.setRowSorter(sorter);

        // Set the RowFilter based on the search query
        var rowFilter = RowFilter.regexFilter("(?i)" + searchQuery);
        sorter.setRowFilter(rowFilter);
        
        
    } else {
        // If search query is empty, remove any existing row filter
        tblERecords.setRowSorter(null);
    }
    }//GEN-LAST:event_btnSearch1ActionPerformed

    private void TFbasicsalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFbasicsalaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFbasicsalaryActionPerformed

    private void TFricesubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFricesubActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFricesubActionPerformed

    private void TFphoneallowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFphoneallowActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFphoneallowActionPerformed

    private void TFclothingallowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFclothingallowActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFclothingallowActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeDetailsHR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new EmployeeDetailsHR().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TFbasicsalary;
    private javax.swing.JTextField TFclothingallow;
    private javax.swing.JTextField TFenum;
    private javax.swing.JTextField TFfirstn;
    private javax.swing.JTextField TFlastn;
    private javax.swing.JTextField TFpagibig;
    private javax.swing.JTextField TFphilh;
    private javax.swing.JTextField TFphoneallow;
    private javax.swing.JTextField TFpos;
    private javax.swing.JTextField TFricesub;
    private javax.swing.JTextField TFsss;
    private javax.swing.JTextField TFstatus;
    private javax.swing.JTextField TFsupervisor;
    private javax.swing.JTextField TFtin;
    private javax.swing.JButton backbuttondetailsPB;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnReset1;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch1;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblbasicsalary;
    private javax.swing.JLabel lblclothingallow;
    private javax.swing.JLabel lblphoneallow;
    private javax.swing.JLabel lblricesubsidy;
    private javax.swing.JTable tblERecords;
    // End of variables declaration//GEN-END:variables

    private boolean isDuplicateEmployee(DefaultTableModel model, String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void saveToCSV(DefaultTableModel model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

       
