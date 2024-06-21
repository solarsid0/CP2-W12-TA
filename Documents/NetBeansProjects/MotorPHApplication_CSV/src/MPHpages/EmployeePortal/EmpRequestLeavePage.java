/*
NEW UPDATES
- Added prompt for consumed leave (15 leaves per year ; not sure if I'll make a leave balance restriction per type of leave as it might cause us to add more columns to show remaining leaves per type of leave)
- Submit leave request (no edit)
- Automatically fills in emp id (uneditable), first name (uneditable, last name (uneditable, position, and imemdiate supervisor fields
- Has current time and date
- Has duplicate handling for same exact entries
- Option to cancel leave request submitted if status is still "Pending"
- I revised the csv file "Leave Requests DB" and removed all commas to avoid the error on formatting.

*/
package MPHpages.EmployeePortal;

import com.opencsv.CSVReader;
import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class EmpRequestLeavePage extends javax.swing.JFrame {

    /**
     * Creates new form Request Leave Page
     */
    private String employeeID; // Add a field to store the employee ID
    private String fullName;
    private final String userRole; // Add a field to store the user role
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
     
    /**
     * Creates new form Request Leave Page
     * @param fullName
     * @param employeeID
     * @param userRole
     */
    
    //Constructor with arguments
    public EmpRequestLeavePage(String fullName, String employeeID, String userRole) {
        this.fullName = fullName;
        this.employeeID = employeeID;
        this.userRole = userRole;
        initComponents();
        populateEmployeeDetails(employeeID); // Populate fields based on employeeID
        populateJTable(employeeID); // Call the populateJTable() method and pass the employeeID
        startLiveClock(); // Start the live clock
        
         
}
    // Constructor without arguments for initializing components and adding action listener
    public EmpRequestLeavePage() {
        initComponents();
        backbuttonreqleavePB.addActionListener((java.awt.event.ActionEvent evt) -> {
            backbuttonreqleavePBActionPerformed(evt);
        });
        submitleavePB.addActionListener((java.awt.event.ActionEvent evt) -> {
            submitleavePBActionPerformed(evt);
        });
        clearleavePB.addActionListener((java.awt.event.ActionEvent evt) -> {
            clearleavePBActionPerformed(evt);
        });
        cancelLeavePB = new javax.swing.JButton("Cancel Leave");
        cancelLeavePB.addActionListener((java.awt.event.ActionEvent evt) -> {
            cancelLeavePBActionPerformed(evt);
        });
        cancelLeavePB.setEnabled(false);
        empleavedetPanel.add(cancelLeavePB);
        
        this.userRole = null;
        startLiveClock(); // Start the live clock
    }
    
    // Method to update the dateTime label with the current date and time
    private void updateDateTime() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Define a date-time formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        // Format the current date and time
        String formattedDateTime = now.format(formatter);
        // Set the formatted date and time to the label
        dateTime.setText(formattedDateTime);
    }

    // Method to start the live clock using a Timer
    private void startLiveClock() {
        // Create a timer that calls updateDateTime() every 1000 milliseconds (1 second)
        Timer timer = new Timer(1000, e -> updateDateTime());
        // Start the timer
        timer.start();
    }

    
     // Method to fetch employee details from CSV based on employeeID
    private Map<String, String> getEmployeeDetails(String employeeID) {
    Map<String, String> employeeDetails = new HashMap<>();

    try {
        File file = new File("src/CSV/MotorPH Employees 2.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip the header row
            
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 5 && values[0].trim().equals(employeeID)) {
                    employeeDetails.put("EmployeeID", values[0].trim());
                    employeeDetails.put("FirstName", values[1].trim());
                    employeeDetails.put("LastName", values[2].trim());
                    employeeDetails.put("Position", values[11].trim());
                    employeeDetails.put("Supervisor", values[12].trim());
                    break; // Exit the loop once the employee is found
                }
            }
        }
    } catch (IOException e) {
    }

    return employeeDetails;
}
     
   
    // Method to populate employee fields
    private void populateEmployeeDetails(String employeeID) {
    Map<String, String> details = getEmployeeDetails(employeeID);
    if (!details.isEmpty()) {
        empIDreqTF.setText(details.get("EmployeeID"));
        rlfirstnameTF.setText(details.get("FirstName"));
        rllastnameTF.setText(details.get("LastName"));
        rlpositionTF.setText(details.get("Position"));
        rlsupervisorTF.setText(details.get("Supervisor"));
        
        
        // Make fields uneditable
        empIDreqTF.setEditable(false);
        rlfirstnameTF.setEditable(false);
        rllastnameTF.setEditable(false);
        
    } else {
        JOptionPane.showMessageDialog(this, "Employee details not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

   private void populateJTable(String employeeID) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // Clear the existing rows

    try {
        File file = new File("src/CSV/Leave Requests DB.csv");
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            reader.readNext(); // Read and discard the header row

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 12 && line[1].trim().equals(employeeID) && !line[10].equals("CANCELLED")) {
                    System.out.println("Adding row for Employee ID: " + employeeID);
                    model.addRow(preserveCommas(line));
                }
            }
        }
    } catch (IOException e) {
        // Handle the exception
    }
}
   
   private Object[] preserveCommas(String[] line) {
    Object[] row = new Object[line.length];
    for (int i = 0; i < line.length; i++) {
        if (line[i].startsWith("\"") && line[i].endsWith("\"")) {
            row[i] = line[i].substring(1, line[i].length() - 1).replace("\"\"", "\"");
        } else {
            row[i] = line[i];
        }
    }
    return row;
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leavemainPNL = new javax.swing.JPanel();
        leaveheaderPNL = new javax.swing.JPanel();
        leaveheaderLBL = new javax.swing.JLabel();
        backbuttonreqleavePB = new javax.swing.JButton();
        dateTime = new javax.swing.JLabel();
        rlemptablePNL = new javax.swing.JPanel();
        rlemptableSP = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        empdetPanel = new javax.swing.JPanel();
        rldateofsubLBL = new javax.swing.JLabel();
        dateofsubmissionDC = new com.toedter.calendar.JDateChooser();
        rldateofsubLBL1 = new javax.swing.JLabel();
        empIDreqTF = new javax.swing.JTextField();
        rlfirstnameLBL = new javax.swing.JLabel();
        rlfirstnameTF = new javax.swing.JTextField();
        rllastnameLBL = new javax.swing.JLabel();
        rllastnameTF = new javax.swing.JTextField();
        rlpositionLBL = new javax.swing.JLabel();
        rlpositionTF = new javax.swing.JTextField();
        rlsupervisorLBL = new javax.swing.JLabel();
        rlsupervisorTF = new javax.swing.JTextField();
        empleavedetPanel = new javax.swing.JPanel();
        typeofleaveLBL = new javax.swing.JLabel();
        typeofleaveCB = new javax.swing.JComboBox<>();
        startLeaveLBL = new javax.swing.JLabel();
        startofleaveDC = new com.toedter.calendar.JDateChooser();
        endLeaveLBL = new javax.swing.JLabel();
        endofleaveDC = new com.toedter.calendar.JDateChooser();
        noteLBL = new javax.swing.JLabel();
        submitleavePB = new javax.swing.JButton();
        clearleavePB = new javax.swing.JButton();
        cancelLeavePB = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        noteTA = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        leavemainPNL.setBackground(new java.awt.Color(153, 153, 153));

        leaveheaderPNL.setBackground(new java.awt.Color(102, 102, 102));

        leaveheaderLBL.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        leaveheaderLBL.setForeground(new java.awt.Color(255, 255, 255));
        leaveheaderLBL.setText("EMPLOYEE REQUEST LEAVE MANAGEMENT");

        backbuttonreqleavePB.setBackground(new java.awt.Color(204, 0, 0));
        backbuttonreqleavePB.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        backbuttonreqleavePB.setForeground(new java.awt.Color(255, 255, 255));
        backbuttonreqleavePB.setText("Back");
        backbuttonreqleavePB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttonreqleavePBActionPerformed(evt);
            }
        });

        dateTime.setBackground(new java.awt.Color(255, 255, 255));
        dateTime.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        dateTime.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout leaveheaderPNLLayout = new javax.swing.GroupLayout(leaveheaderPNL);
        leaveheaderPNL.setLayout(leaveheaderPNLLayout);
        leaveheaderPNLLayout.setHorizontalGroup(
            leaveheaderPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leaveheaderPNLLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(backbuttonreqleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(286, 286, 286)
                .addComponent(leaveheaderLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(dateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        leaveheaderPNLLayout.setVerticalGroup(
            leaveheaderPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leaveheaderPNLLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(leaveheaderPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(leaveheaderPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(backbuttonreqleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(leaveheaderLBL)))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "ID", "First Name", "Last Name", "Position", "Supervisor", "TOP", "Note", "Start", "End", "LStatus", "Remaining L "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        rlemptableSP.setViewportView(jTable1);

        javax.swing.GroupLayout rlemptablePNLLayout = new javax.swing.GroupLayout(rlemptablePNL);
        rlemptablePNL.setLayout(rlemptablePNLLayout);
        rlemptablePNLLayout.setHorizontalGroup(
            rlemptablePNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rlemptablePNLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rlemptableSP)
                .addContainerGap())
        );
        rlemptablePNLLayout.setVerticalGroup(
            rlemptablePNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rlemptablePNLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rlemptableSP, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        empdetPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        rldateofsubLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rldateofsubLBL.setForeground(new java.awt.Color(0, 0, 0));
        rldateofsubLBL.setText("Date of Submission:");

        dateofsubmissionDC.setBackground(new java.awt.Color(255, 255, 255));

        rldateofsubLBL1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rldateofsubLBL1.setForeground(new java.awt.Color(0, 0, 0));
        rldateofsubLBL1.setText("Employee ID:");

        rlfirstnameLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rlfirstnameLBL.setForeground(new java.awt.Color(0, 0, 0));
        rlfirstnameLBL.setText("First Name:");

        rlfirstnameTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rlfirstnameTFActionPerformed(evt);
            }
        });

        rllastnameLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rllastnameLBL.setForeground(new java.awt.Color(0, 0, 0));
        rllastnameLBL.setText("Last Name:");

        rlpositionLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rlpositionLBL.setForeground(new java.awt.Color(0, 0, 0));
        rlpositionLBL.setText("Position:");

        rlsupervisorLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rlsupervisorLBL.setForeground(new java.awt.Color(0, 0, 0));
        rlsupervisorLBL.setText("Immediate Supervisor:");

        rlsupervisorTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rlsupervisorTFActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout empdetPanelLayout = new javax.swing.GroupLayout(empdetPanel);
        empdetPanel.setLayout(empdetPanelLayout);
        empdetPanelLayout.setHorizontalGroup(
            empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empdetPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(rlpositionLBL)
                        .addComponent(rlpositionTF, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(rlfirstnameTF, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rldateofsubLBL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateofsubmissionDC, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                        .addComponent(rlfirstnameLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31)
                .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(rldateofsubLBL1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(empIDreqTF, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                        .addComponent(rllastnameLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rllastnameTF))
                    .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(rlsupervisorLBL)
                        .addComponent(rlsupervisorTF, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        empdetPanelLayout.setVerticalGroup(
            empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empdetPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(empdetPanelLayout.createSequentialGroup()
                        .addComponent(rldateofsubLBL1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(empIDreqTF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(empdetPanelLayout.createSequentialGroup()
                        .addComponent(rldateofsubLBL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateofsubmissionDC, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(empdetPanelLayout.createSequentialGroup()
                        .addComponent(rlfirstnameLBL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rlfirstnameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(empdetPanelLayout.createSequentialGroup()
                        .addComponent(rllastnameLBL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rllastnameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(empdetPanelLayout.createSequentialGroup()
                        .addComponent(rlpositionLBL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rlpositionTF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(empdetPanelLayout.createSequentialGroup()
                        .addComponent(rlsupervisorLBL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rlsupervisorTF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        empleavedetPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        typeofleaveLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        typeofleaveLBL.setForeground(new java.awt.Color(0, 0, 0));
        typeofleaveLBL.setText("Type of Leave:");

        typeofleaveCB.setBackground(new java.awt.Color(255, 255, 255));
        typeofleaveCB.setEditable(true);
        typeofleaveCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Sick Leave ", "Medical Leave", "Annual Leave ", "Emergency Leave ", "Paternity Leave ", "Maternity Leave ", "Vacation Leave ", "Bereavement Leave " }));
        typeofleaveCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeofleaveCBActionPerformed(evt);
            }
        });

        startLeaveLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        startLeaveLBL.setForeground(new java.awt.Color(0, 0, 0));
        startLeaveLBL.setText("Start of Leave:");

        startofleaveDC.setBackground(new java.awt.Color(255, 255, 255));

        endLeaveLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        endLeaveLBL.setForeground(new java.awt.Color(0, 0, 0));
        endLeaveLBL.setText("End of Leave:");

        endofleaveDC.setBackground(new java.awt.Color(255, 255, 255));

        noteLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        noteLBL.setForeground(new java.awt.Color(0, 0, 0));
        noteLBL.setText("Note:");

        submitleavePB.setText("Submit");
        submitleavePB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitleavePBActionPerformed(evt);
            }
        });

        clearleavePB.setText("Clear");
        clearleavePB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearleavePBActionPerformed(evt);
            }
        });

        cancelLeavePB.setText("Cancel");
        cancelLeavePB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelLeavePBActionPerformed(evt);
            }
        });

        noteTA.setColumns(20);
        noteTA.setRows(5);
        jScrollPane1.setViewportView(noteTA);

        javax.swing.GroupLayout empleavedetPanelLayout = new javax.swing.GroupLayout(empleavedetPanel);
        empleavedetPanel.setLayout(empleavedetPanelLayout);
        empleavedetPanelLayout.setHorizontalGroup(
            empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empleavedetPanelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startLeaveLBL)
                            .addComponent(startofleaveDC, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(typeofleaveLBL)
                        .addComponent(typeofleaveCB, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(empleavedetPanelLayout.createSequentialGroup()
                        .addComponent(submitleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(clearleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelLeavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36)
                .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(noteLBL)
                    .addComponent(endofleaveDC, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                    .addComponent(endLeaveLBL)
                    .addComponent(jScrollPane1))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        empleavedetPanelLayout.setVerticalGroup(
            empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empleavedetPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(empleavedetPanelLayout.createSequentialGroup()
                        .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(startLeaveLBL)
                            .addComponent(endLeaveLBL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startofleaveDC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(empleavedetPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(endofleaveDC, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(empleavedetPanelLayout.createSequentialGroup()
                        .addComponent(typeofleaveLBL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(typeofleaveCB, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(submitleavePB, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(clearleavePB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cancelLeavePB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(empleavedetPanelLayout.createSequentialGroup()
                        .addComponent(noteLBL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout leavemainPNLLayout = new javax.swing.GroupLayout(leavemainPNL);
        leavemainPNL.setLayout(leavemainPNLLayout);
        leavemainPNLLayout.setHorizontalGroup(
            leavemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leavemainPNLLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(leavemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(leavemainPNLLayout.createSequentialGroup()
                        .addComponent(empdetPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(empleavedetPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rlemptablePNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(leavemainPNLLayout.createSequentialGroup()
                .addComponent(leaveheaderPNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        leavemainPNLLayout.setVerticalGroup(
            leavemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leavemainPNLLayout.createSequentialGroup()
                .addComponent(leaveheaderPNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(leavemainPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(empdetPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(empleavedetPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43)
                .addComponent(rlemptablePNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leavemainPNL, javax.swing.GroupLayout.PREFERRED_SIZE, 1246, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leavemainPNL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backbuttonreqleavePBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbuttonreqleavePBActionPerformed

       // Create an instance of EmployeePortal
        EmployeePortal employeePage = new EmployeePortal(fullName, employeeID, userRole);
        // Center the EmployeePortal
        employeePage.setLocationRelativeTo(null);
        // Make the EmployeePortal visible
        employeePage.setVisible(true);
        // Close the current Request Leave Page
        this.dispose();
    }//GEN-LAST:event_backbuttonreqleavePBActionPerformed

    private void typeofleaveCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeofleaveCBActionPerformed
        /* Types of Leaves:
        Select
        Sick Leave
        Medical Leave
        Annual Leave 
        Emergency Leave
        Paternity Leave
        Maternity Leave
        Vacation Leave
        Bereavement Leave
        */
    }//GEN-LAST:event_typeofleaveCBActionPerformed

    private void rlfirstnameTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rlfirstnameTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rlfirstnameTFActionPerformed

    private void submitleavePBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitleavePBActionPerformed
       // Capture the user input
           
            Date dateOfSubmissionObj = dateofsubmissionDC.getDate();
            String dateOfSubmission = dateOfSubmissionObj != null ? dateFormat.format(dateOfSubmissionObj) : "";

            String empID = empIDreqTF.getText().trim();
            String firstName = rlfirstnameTF.getText().trim();
            String lastName = rllastnameTF.getText().trim();
            String position = rlpositionTF.getText().trim();
            String supervisor = rlsupervisorTF.getText().trim();
            String leaveType = typeofleaveCB.getSelectedItem().toString().trim();
            String note = noteTA.getText().trim();

            Date startDateObj = startofleaveDC.getDate();
            String startDate = startDateObj != null ? dateFormat.format(startDateObj) : "";

            Date endDateObj = endofleaveDC.getDate();
            String endDate = endDateObj != null ? dateFormat.format(endDateObj) : "";
            
            // Check for null dates
            if (startDateObj == null || endDateObj == null) {
                JOptionPane.showMessageDialog(this, "Please select both start and end dates.", "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

    
            // Validate the inputs
            if (empID.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || position.isEmpty() ||
                    supervisor.isEmpty() || leaveType.equals("Select") || startDate.isEmpty() || endDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check for commas in inputs
            if (containsComma(empID) || containsComma(firstName) || containsComma(lastName) ||
                containsComma(position) || containsComma(supervisor) || containsComma(leaveType) || containsComma(note)) {
                JOptionPane.showMessageDialog(this, "Input fields must not contain commas.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Check for duplicate entry
            if (isDuplicateEntry(empID, startDate, endDate)) {
                JOptionPane.showMessageDialog(this, "Duplicate leave request found. Please check the existing entries.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calculate leave duration
            long leaveDuration = ChronoUnit.DAYS.between(startDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 
                                                  endDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;

            // Get remaining leaves
            int remainingLeaves = getRemainingLeaves(empID);
            if (remainingLeaves < leaveDuration) {  // Check leave balance
                JOptionPane.showMessageDialog(this, "Leave cannot be submitted. Remaining leave balance is insufficient.", "Leave Balance Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ensure fields with commas are quoted
            firstName = formatFieldWithQuotes(firstName);
            lastName = formatFieldWithQuotes(lastName);
            position = formatFieldWithQuotes(position);
            supervisor = formatFieldWithQuotes(supervisor);
            leaveType = formatFieldWithQuotes(leaveType);
            note = formatFieldWithQuotes(note);

            // Build data string, using quotes around fields that may contain commas
            String data = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"Pending\",\"%s\"",
                dateOfSubmission, empID, firstName, lastName, position, supervisor, leaveType, note, startDate, endDate, remainingLeaves);

            File file = new File("src/CSV/Leave Requests DB.csv");
            String tempFileName = "src/CSV/Leave Requests DB_temp.csv";
            File tempFile = new File(tempFileName);

            try (CSVReader reader = new CSVReader(new FileReader(file));
                 FileWriter fw = new FileWriter(tempFile);
                 BufferedWriter bw = new BufferedWriter(fw)) {

            // Read the header and write it to the temp file
            String[] headerRow = reader.readNext();
            bw.write(String.join(",", (CharSequence[]) (Object[]) headerRow) + "\n");

            // Write each line from the original file to the temp file
            String[] line;
            while ((line = reader.readNext()) != null) {
                bw.write(String.join(",", (CharSequence[]) (Object[]) line) + "\n");
            }

            // Append the new request data to a new line in the temp file
            bw.write(data + "\n");
            bw.flush(); // Ensure data is written immediately

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving leave request.", "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
            

            // Delete the original file
            if (!file.delete()) {
                System.out.println("Could not delete original file.");
            }

            // Rename the temporary file to the original file
            if (!tempFile.renameTo(file)) {
                System.out.println("Could not rename temp file to original file.");
            }

            // Update remaining leaves
            updateRemainingLeaves(empID, remainingLeaves - (int) leaveDuration);

            JOptionPane.showMessageDialog(this, "Leave request submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh the JTable with the updated data
            populateJTable(employeeID);

            // Clear the form after submission
            clearleavePBActionPerformed(evt);
}


        // Helper function to check if a string contains a comma
        private boolean containsComma(String input) {
            return input != null && input.contains(",");
        }
        
        //Duplicate Handling
        private boolean isDuplicateEntry(String empID, String startDate, String endDate) {
            try (CSVReader reader = new CSVReader(new FileReader("src/CSV/Leave Requests DB.csv"))) {
                String[] line;
                reader.readNext(); // Skip the header row
                while ((line = reader.readNext()) != null) {
                    if (line.length == 12 && line[1].equals(empID) && line[8].equals(startDate) && line[9].equals(endDate) && line[10].equals("Pending")) {
                        return true;
                    }
                }
            } catch (IOException e) {
                // Handle the exception
            }
            return false;
        }

    // Function to check remaining sick leaves from the CSV file
    private int getRemainingLeaves(String empID) {
        File file = new File("src/CSV/Leave Requests DB.csv");
        int totalLeaves = 15; // Maximum allowed leaves per year (sick leave: https://www.skuad.io/leave-policy/philippines#:~:text=Sick%20leave%20in%20the%20Philippines,-Employers%20are%20required&text=Employees%20are%20entitled%20to%2012,days%20of%20annual%20sick%20leave. )
        int takenLeaves = 0;

        int currentYear = LocalDate.now().getYear(); // Get current year

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length == 12 && line[1].trim().equals(empID)) {
                    String[] startDateParts = line[8].split("/");
                    String[] endDateParts = line[9].split("/");
                    if (startDateParts.length == 3 && endDateParts.length == 3) {
                        int startYear = Integer.parseInt(startDateParts[2]);
                        int endYear = Integer.parseInt(endDateParts[2]);

                        if (startYear == currentYear && endYear == currentYear) {
                            if (line[10].equals("Accepted")) { //only subtracts remaining leaves if the status is "Accepted" by admin
                                // Calculate days for leave in the current year
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date startDate = sdf.parse(line[8]);
                                Date endDate = sdf.parse(line[9]);
                                long duration = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) + 1;
                                takenLeaves += duration;
                            }
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException | java.text.ParseException e) {
        }
        return totalLeaves - takenLeaves;
    }

    // Function to update remaining leaves in the CSV file
    private void updateRemainingLeaves(String empID, int newRemainingLeaves) {
        File file = new File("src/CSV/Leave Requests DB.csv");
            String tempFileName = "src/CSV/Leave Requests DB_temp.csv";
            File tempFile = new File(tempFileName);

            try (CSVReader reader = new CSVReader(new FileReader(file));
                 FileWriter fw = new FileWriter(tempFile);
                 BufferedWriter bw = new BufferedWriter(fw)) {

                String[] headerRow = reader.readNext();
                bw.write(String.join(",", headerRow) + "\n");

                String[] line;
                while ((line = reader.readNext()) != null) {
                    if (line.length == 13 && line[2].trim().equals(empID)) {
                        line[12] = String.valueOf(newRemainingLeaves);
                    }
                    bw.write(String.join(",", line) + "\n");
                }
            } catch (IOException e) {
            }

            if (!file.delete()) {
                System.out.println("Could not delete original file.");
            }
            if (!tempFile.renameTo(file)) {
                System.out.println("Could not rename temp file to original file.");
            }
    }

    // Function to format fields with quotes if they contain commas
    private String formatFieldWithQuotes(String field) {
        String escapedField = field.replace("\"", "\"\""); // Escape existing double quotes
        if (escapedField.contains(",")) {
            escapedField = "\"" + escapedField + "\""; // Enclose the field with double quotes if it contains commas
        }
        return escapedField;
        
    }//GEN-LAST:event_submitleavePBActionPerformed

    
    private void clearleavePBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearleavePBActionPerformed
        // Clear the text fields
        dateofsubmissionDC.setDate(null);
        empIDreqTF.setText("");
        rlfirstnameTF.setText("");
        rllastnameTF.setText("");
        rlpositionTF.setText("");
        rlsupervisorTF.setText("");
        typeofleaveCB.setSelectedIndex(0);
        noteTA.setText("");
        startofleaveDC.setDate(null);
        endofleaveDC.setDate(null);
    }//GEN-LAST:event_clearleavePBActionPerformed

    private void cancelLeavePBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelLeavePBActionPerformed
          int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a leave request to cancel.", "Cancel Request", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            String status = (String) model.getValueAt(selectedRow, 10);

            if (!status.equals("Pending")) {
                JOptionPane.showMessageDialog(this, "You can only cancel leave requests with 'Pending' status.", "Cancel Request", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel the selected leave request?", "Cancel Request", JOptionPane.YES_NO_OPTION);
            if (confirmResult == JOptionPane.YES_OPTION) {
                String empID = (String) model.getValueAt(selectedRow, 1);
                String startDate = (String) model.getValueAt(selectedRow, 8);
                String endDate = (String) model.getValueAt(selectedRow, 9);

                removeLeaveRequest(empID, startDate, endDate);
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Leave request canceled successfully.", "Cancel Request", JOptionPane.INFORMATION_MESSAGE);
            }
        
    }//GEN-LAST:event_cancelLeavePBActionPerformed

    private void rlsupervisorTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rlsupervisorTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rlsupervisorTFActionPerformed
    
    //Use case when user cancelled the submitted leave request
     private void removeLeaveRequest(String empID, String startDate, String endDate) {
        File file = new File("src/CSV/Leave Requests DB.csv");
            String tempFileName = "src/CSV/Leave Requests DB_temp.csv";
            File tempFile = new File(tempFileName);

            try (CSVReader reader = new CSVReader(new FileReader(file));
                 FileWriter fw = new FileWriter(tempFile);
                 BufferedWriter bw = new BufferedWriter(fw)) {

                String[] headerRow = reader.readNext();
                bw.write(String.join(",", headerRow) + "\n");

                String[] line;
                while ((line = reader.readNext()) != null) {
                    if (!(line.length == 13 && line[2].equals(empID) && line[9].equals(startDate) && line[10].equals(endDate))) {
                        bw.write(String.join(",", line) + "\n");
                    }
                }
            } catch (IOException e) {
            }

            if (!file.delete()) {
                System.out.println("Could not delete original file.");
            }
            if (!tempFile.renameTo(file)) {
                System.out.println("Could not rename temp file to original file.");
            }

            populateJTable(employeeID);
}

    
    
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
            java.util.logging.Logger.getLogger(EmpRequestLeavePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new EmpRequestLeavePage().setVisible(true);
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backbuttonreqleavePB;
    private javax.swing.JButton cancelLeavePB;
    private javax.swing.JButton clearleavePB;
    private javax.swing.JLabel dateTime;
    private com.toedter.calendar.JDateChooser dateofsubmissionDC;
    private javax.swing.JTextField empIDreqTF;
    private javax.swing.JPanel empdetPanel;
    private javax.swing.JPanel empleavedetPanel;
    private javax.swing.JLabel endLeaveLBL;
    private com.toedter.calendar.JDateChooser endofleaveDC;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel leaveheaderLBL;
    private javax.swing.JPanel leaveheaderPNL;
    private javax.swing.JPanel leavemainPNL;
    private javax.swing.JLabel noteLBL;
    private javax.swing.JTextArea noteTA;
    private javax.swing.JLabel rldateofsubLBL;
    private javax.swing.JLabel rldateofsubLBL1;
    private javax.swing.JPanel rlemptablePNL;
    private javax.swing.JScrollPane rlemptableSP;
    private javax.swing.JLabel rlfirstnameLBL;
    private javax.swing.JTextField rlfirstnameTF;
    private javax.swing.JLabel rllastnameLBL;
    private javax.swing.JTextField rllastnameTF;
    private javax.swing.JLabel rlpositionLBL;
    private javax.swing.JTextField rlpositionTF;
    private javax.swing.JLabel rlsupervisorLBL;
    private javax.swing.JTextField rlsupervisorTF;
    private javax.swing.JLabel startLeaveLBL;
    private com.toedter.calendar.JDateChooser startofleaveDC;
    private javax.swing.JButton submitleavePB;
    private javax.swing.JComboBox<String> typeofleaveCB;
    private javax.swing.JLabel typeofleaveLBL;
    // End of variables declaration//GEN-END:variables
}
    
