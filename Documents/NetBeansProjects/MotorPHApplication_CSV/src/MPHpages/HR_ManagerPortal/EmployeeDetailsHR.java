/*

ADDED!
- Error pop up for that duplication code (duplicate employee number)
- Update button function restored
- Resizing is manual
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
     this.fullName = fullName; 
     this.employeeID = employeeID;
     this.userRole = userRole;
     loadCSV("src/CSV/MotorPH Employee Data UP.csv"); 
     
     // Add mouse listener to the table
     tblERecords.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int selectedRow = tblERecords.getSelectedRow();
            if (selectedRow != -1) {
                TFenum.setText(tblERecords.getValueAt(selectedRow, 0).toString());
                TFlastn.setText(tblERecords.getValueAt(selectedRow, 1).toString());
                TFfirstn.setText(tblERecords.getValueAt(selectedRow, 2).toString());
                
                // Get the date string from the table and convert it to Date
                String dateString = tblERecords.getValueAt(selectedRow, 3).toString(); // Assume the date is in column 3
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Adjust format as per your CSV data
                try {
                Date date = dateFormat.parse(dateString);
                jDateChooserBday.setDate(date);
                } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Invalid date format: " + dateString);
                }
                
                TFAddress.setText(tblERecords.getValueAt(selectedRow, 4).toString());
                TFphonenum.setText(tblERecords.getValueAt(selectedRow, 5).toString());
                TFsss.setText(tblERecords.getValueAt(selectedRow, 6).toString());
                TFphilh.setText(tblERecords.getValueAt(selectedRow, 7).toString());
                TFpagibig.setText(tblERecords.getValueAt(selectedRow, 8).toString());
                TFtin.setText(tblERecords.getValueAt(selectedRow, 9).toString());
                TFstatus.setSelectedItem(tblERecords.getValueAt(selectedRow, 10).toString());
                TFpos.setText(tblERecords.getValueAt(selectedRow, 11).toString());
                TFsupervisor.setText(tblERecords.getValueAt(selectedRow, 12).toString());
                TFbasicsalary.setText(tblERecords.getValueAt(selectedRow, 13).toString());
                TFricesub.setText(tblERecords.getValueAt(selectedRow, 14).toString());
                TFphoneallow.setText(tblERecords.getValueAt(selectedRow, 15).toString());
                TFclothingallow.setText(tblERecords.getValueAt(selectedRow, 16).toString());
                TFhourlyrate.setText(tblERecords.getValueAt(selectedRow, 17).toString());
            }
        }
    });
    }
    
    public EmployeeDetailsHR() {
     initComponents();
              
        backbuttondetailsPB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttondetailsPBActionPerformed(evt);
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
        Set<String> employeeID = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
           
            
            // Read the CSV header
            line = br.readLine();
            String[] headers = line.split(",");

            // Set table headers
            model.setColumnIdentifiers(headers);
            
            
            // Read remaining lines
            while ((line = br.readLine()) != null) {
                String[] data = parseCSVLine(line); // Use parseCSVLine to handle commas within fields
                String empID = data[0];
                
                if (employeeID.contains(empID)) {
                    JOptionPane.showMessageDialog(this, "Duplicate Employee ID found: " + empID, "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                employeeID.add(empID);
                model.addRow(data);
            }

            // Adjust row heights
            adjustRowHeight();
              // Adjust column widths
            adjustColumnWidths();

            // Make table non-editable
            for (int i = 0; i < model.getColumnCount(); i++) {
                TableColumn column = tblERecords.getColumnModel().getColumn(i);
                column.setCellEditor(null);
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

    // Adjust column widths to fit content
    private void adjustColumnWidths() {
        for (int column = 0; column < tblERecords.getColumnCount(); column++) {
            int width = getColumnWidth(column);
            tblERecords.getColumnModel().getColumn(column).setPreferredWidth(width);
        }
    }
    
    // Get the preferred width of a column based on the content
    private int getColumnWidth(int column) {
        int width = 0;
        TableCellRenderer headerRenderer = tblERecords.getTableHeader().getDefaultRenderer();
        Component headerComp = headerRenderer.getTableCellRendererComponent(tblERecords, tblERecords.getColumnModel().getColumn(column).getHeaderValue(), false, false, 0, column);
        width = Math.max(headerComp.getPreferredSize().width + tblERecords.getIntercellSpacing().width, width);
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
        TFstatus = new javax.swing.JComboBox<>();
        TFhourlyrate = new javax.swing.JTextField();
        lbhourlyrate = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jDateChooserBday = new com.toedter.calendar.JDateChooser();
        LBLphonenum = new javax.swing.JLabel();
        TFphonenum = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TFAddress = new javax.swing.JTextArea();

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
                "Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone #", "SSS #", "PhilHealth #", "Pag-Ibig #", "TIN", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Hourly Rate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
            tblERecords.getColumnModel().getColumn(12).setPreferredWidth(60);
            tblERecords.getColumnModel().getColumn(13).setMinWidth(80);
            tblERecords.getColumnModel().getColumn(13).setPreferredWidth(80);
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

        TFstatus.setBackground(new java.awt.Color(255, 255, 255));
        TFstatus.setEditable(true);
        TFstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Regular", "Probationary" }));
        TFstatus.setToolTipText("");

        TFhourlyrate.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFhourlyrate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFhourlyrateActionPerformed(evt);
            }
        });

        lbhourlyrate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbhourlyrate.setText("Hourly Rate");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Birthday:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Address:");

        LBLphonenum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LBLphonenum.setText("Phone Number:");

        TFphonenum.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TFphonenum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFphonenumActionPerformed(evt);
            }
        });

        TFAddress.setColumns(20);
        TFAddress.setRows(5);
        jScrollPane3.setViewportView(TFAddress);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(lblphoneallow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGap(65, 65, 65))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGap(128, 128, 128))
                                                    .addComponent(TFphoneallow)
                                                    .addComponent(TFphilh)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGap(49, 49, 49)))
                                                .addGap(47, 47, 47)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGap(155, 155, 155))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGap(31, 31, 31))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(lblclothingallow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGap(52, 52, 52))
                                                    .addComponent(TFsupervisor)
                                                    .addComponent(TFtin)
                                                    .addComponent(TFclothingallow, javax.swing.GroupLayout.Alignment.TRAILING)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(TFpos)
                                                .addGap(231, 231, 231)))
                                        .addGap(43, 43, 43)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblbasicsalary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(103, 103, 103))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblricesubsidy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(98, 98, 98))
                                            .addComponent(TFbasicsalary)
                                            .addComponent(TFpagibig)
                                            .addComponent(TFricesub)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(62, 62, 62))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TFenum)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(53, 53, 53)))
                                        .addGap(47, 47, 47)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(109, 109, 109))
                                            .addComponent(TFlastn))
                                        .addGap(43, 43, 43)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(107, 107, 107))
                                            .addComponent(TFfirstn))))
                                .addGap(46, 46, 46))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(96, 96, 96))
                            .addComponent(TFhourlyrate)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbhourlyrate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(101, 101, 101))
                            .addComponent(TFsss)
                            .addComponent(TFstatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(137, 137, 137))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(121, 121, 121))
                            .addComponent(jDateChooserBday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(180, 180, 180))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LBLphonenum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(132, 132, 132))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnReset1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate)
                                .addGap(59, 59, 59))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(TFphonenum)
                                    .addComponent(jScrollPane3))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jScrollPane1))
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFphilh)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFpos)
                                .addGap(14, 14, 14)
                                .addComponent(lblphoneallow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFphoneallow))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(36, 36, 36))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(TFtin)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFsupervisor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblclothingallow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(10, 10, 10)
                                .addComponent(TFclothingallow))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFpagibig)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblbasicsalary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFbasicsalary)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblricesubsidy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFricesub))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(TFfirstn)
                                    .addComponent(jDateChooserBday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(TFlastn)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(TFenum, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(4, 4, 4)
                                .addComponent(TFstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LBLphonenum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFsss)
                            .addComponent(TFphonenum))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbhourlyrate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TFhourlyrate)))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearch1)
                    .addComponent(btnSave)
                    .addComponent(btnDelete)
                    .addComponent(btnReset1)
                    .addComponent(btnUpdate))
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
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

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
                    if (validateInput()) {
        if (isDuplicateEmployeeID(TFenum.getText())) {
            JOptionPane.showMessageDialog(this, "Employee ID " + TFenum.getText() + " already exists.", "Duplicate Employee ID", JOptionPane.ERROR_MESSAGE);
        } else {
            addEmployee();
            saveToCSV();
        }
        } else {
        if (containsComma()) {
            JOptionPane.showMessageDialog(this, "Commas are not allowed in any field.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields to save the record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
            }
    
    private boolean isDuplicateEmployeeID(String empID) {
        DefaultTableModel model = (DefaultTableModel) tblERecords.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
        String existingID = model.getValueAt(row, 0).toString(); // Assuming Employee ID is in the first column
        if (existingID.equals(empID)) {
        return true; // Found a duplicate Employee ID
        }
    }
        return false; // No duplicate Employee ID found
}
    

            private boolean validateInput() {
                return !TFenum.getText().isEmpty() &&
                        !TFlastn.getText().isEmpty() &&
                        !TFfirstn.getText().isEmpty() &&  
                        jDateChooserBday.getDate() != null &&
                        !TFAddress.getText().isEmpty() &&
                        !TFphonenum.getText().isEmpty() &&
                        !TFsss.getText().isEmpty() &&
                        !TFphilh.getText().isEmpty() &&
                        !TFpagibig.getText().isEmpty() &&
                        !TFtin.getText().isEmpty() &&
                        TFstatus.getSelectedItem() != null &&
                        !TFpos.getText().isEmpty() &&
                        !TFsupervisor.getText().isEmpty() &&
                        !TFbasicsalary.getText().isEmpty() &&
                        !TFricesub.getText().isEmpty() &&
                        !TFphoneallow.getText().isEmpty() &&
                        !TFclothingallow.getText().isEmpty() &&
                        !TFhourlyrate.getText().isEmpty() &&
                        !containsComma();
            }

            private boolean containsComma() {
                String selectedStatus = TFstatus.getSelectedItem() != null ? TFstatus.getSelectedItem().toString() : "";
                String dateText = jDateChooserBday.getDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(jDateChooserBday.getDate()) : "";
                return TFenum.getText().contains(",") ||
                       TFlastn.getText().contains(",") ||
                       TFfirstn.getText().contains(",") ||
                       dateText.contains(",")|| //Birthday
                       TFAddress.getText().contains(",")||
                       TFphonenum.getText().contains(",")||
                       TFsss.getText().contains(",") ||
                       TFphilh.getText().contains(",") ||
                       TFpagibig.getText().contains(",") ||
                       TFtin.getText().contains(",") ||                      
                       selectedStatus.contains(",") ||
                       TFpos.getText().contains(",") ||
                       TFsupervisor.getText().contains(",")||
                       TFbasicsalary.getText().contains(",")||
                       TFricesub.getText().contains(",")||
                       TFphoneallow.getText().contains(",")||
                       TFclothingallow.getText().contains(",")||
                       TFhourlyrate.getText().contains(",");
                       
            }

            private void addEmployee() {
                DefaultTableModel model = (DefaultTableModel) tblERecords.getModel();
                model.addRow(new Object[]{
                    TFenum.getText(),
                    TFlastn.getText(),
                    TFfirstn.getText(),
                    jDateChooserBday.getDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(jDateChooserBday.getDate()) : "", // Birthday
                    TFAddress.getText(), // Address
                    TFphonenum.getText(),
                    TFsss.getText(),
                    TFphilh.getText(),
                    TFtin.getText(),
                    TFpagibig.getText(),
                    TFstatus.getSelectedItem() != null ? TFstatus.getSelectedItem().toString() : "", 
                    TFpos.getText(),
                    TFsupervisor.getText(),
                    TFbasicsalary.getText(),
                    TFricesub.getText(),
                    TFphoneallow.getText(),
                    TFclothingallow.getText(),
                    TFhourlyrate.getText()
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
                TFstatus.setSelectedItem(null);
                TFpos.setText("");
                TFsupervisor.setText("");
                TFbasicsalary.setText("");
                TFricesub.setText("");
                TFphoneallow.setText("");
                TFclothingallow.setText("");
                TFhourlyrate.setText("");
                jDateChooserBday.setDate(null); // Clear birthday field
                TFAddress.setText(""); // Clear address field
                TFphonenum.setText("");
            }

            private void saveToCSV() {
                try {
                    DefaultTableModel model = (DefaultTableModel) tblERecords.getModel();
                    File file = new File("src/CSV/MotorPH Employee Data UP.csv");

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
        model.setValueAt(jDateChooserBday.getDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(jDateChooserBday.getDate()) : "", selectedRow, 3); // Update birthday
        model.setValueAt(TFAddress.getText(), selectedRow, 4); // Update address
        model.setValueAt(TFphonenum.getText(), selectedRow, 5); // Update phone num
        model.setValueAt(TFsss.getText(), selectedRow, 6);
        model.setValueAt(TFphilh.getText(), selectedRow, 7);
        model.setValueAt(TFpagibig.getText(), selectedRow, 8);
        model.setValueAt(TFtin.getText(), selectedRow, 9);
        model.setValueAt(TFstatus.getSelectedItem() != null ? TFstatus.getSelectedItem().toString() : "", selectedRow, 10);
        model.setValueAt(TFpos.getText(), selectedRow, 11);
        model.setValueAt(TFsupervisor.getText(), selectedRow, 12);
        model.setValueAt(TFbasicsalary.getText(), selectedRow, 13);
        model.setValueAt(TFricesub.getText(), selectedRow, 14);
        model.setValueAt(TFphoneallow.getText(), selectedRow, 15);
        model.setValueAt(TFclothingallow.getText(), selectedRow, 16);
        model.setValueAt(TFhourlyrate.getText(), selectedRow, 17);
        
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

    private void TFhourlyrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFhourlyrateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFhourlyrateActionPerformed

    private void TFphonenumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFphonenumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFphonenumActionPerformed

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
    private javax.swing.JLabel LBLphonenum;
    private javax.swing.JTextArea TFAddress;
    private javax.swing.JTextField TFbasicsalary;
    private javax.swing.JTextField TFclothingallow;
    private javax.swing.JTextField TFenum;
    private javax.swing.JTextField TFfirstn;
    private javax.swing.JTextField TFhourlyrate;
    private javax.swing.JTextField TFlastn;
    private javax.swing.JTextField TFpagibig;
    private javax.swing.JTextField TFphilh;
    private javax.swing.JTextField TFphoneallow;
    private javax.swing.JTextField TFphonenum;
    private javax.swing.JTextField TFpos;
    private javax.swing.JTextField TFricesub;
    private javax.swing.JTextField TFsss;
    private javax.swing.JComboBox<String> TFstatus;
    private javax.swing.JTextField TFsupervisor;
    private javax.swing.JTextField TFtin;
    private javax.swing.JButton backbuttondetailsPB;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnReset1;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch1;
    private javax.swing.JButton btnUpdate;
    private com.toedter.calendar.JDateChooser jDateChooserBday;
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbhourlyrate;
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

       
