/*
NEW UPDATES
- Changed Employee Data csv file to : MotorPH Employee Data UP.csv
- Added restriction if leave credits will be consumed. 
Regular : entitled for 20 vacation leaves and 10 sick leaves per year
Probationary: entitled for 17 vacation leaves and 10 sick leaves
- Submit leave request (uneditable once submitted but can be cancelled if status is still "Pending")
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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
        File file = new File("src/CSV/MotorPH Employee Data UP.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip the header row
            
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 5 && values[0].trim().equals(employeeID)) {
                    employeeDetails.put("EmployeeID", values[0].trim());
                    employeeDetails.put("FirstName", values[1].trim());
                    employeeDetails.put("LastName", values[2].trim());
                    employeeDetails.put("Status", values[10].trim());
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
        empstatusTF.setText(details.get("Status"));
        rlsupervisorTF.setText(details.get("Supervisor"));
        
        
        // Make fields uneditable
        empIDreqTF.setEditable(false);
        rlfirstnameTF.setEditable(false);
        rllastnameTF.setEditable(false);
        rlpositionTF.setEditable(false);
        empstatusTF.setEditable(false);
        
    } else {
        JOptionPane.showMessageDialog(this, "Employee details not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

   private void populateJTable(String employeeID) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        try {
            File file = new File("src/CSV/Leave Requests DB.csv");
            try (CSVReader reader = new CSVReader(new FileReader(file))) {
                reader.readNext(); // Read and discard the header row
                String[] line;
                while ((line = reader.readNext()) != null) {
                    if (line.length >= 12 && line[1].trim().equals(employeeID) && !line[10].equals("CANCELLED")) {
                        model.addRow(preserveCommas(line));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
   
   // Method to check if it's the first leave request for the year
   private boolean isFirstLeaveRequestForYear(String empID, int year) {
    try {
        File file = new File("src/CSV/Leave Requests DB.csv");
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            reader.readNext(); // Skip header
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 11 && line[1].trim().equals(empID)) {
                    Date leaveDate = dateFormat.parse(line[9]); // Start date of leave
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(leaveDate);
                    if (cal.get(Calendar.YEAR) == year) {
                        return false; // Found a leave request for this year
                    }
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return true; // No leave requests found for this year
}


// Method to calculate remaining leave balances based on the most recent leave request
private int[] calculateRemainingLeaves(String empID, int year) {
    int[] remainingLeaves = new int[2]; // Index 0: Vacation Leaves, Index 1: Sick Leaves

    // Check if it's the first leave request for the year
    if (isFirstLeaveRequestForYear(empID, year)) {
        // Get the employee's status
        String status = empstatusTF.getText().trim();
        
        // Initialize default leave counts based on status
        remainingLeaves[0] = status.equalsIgnoreCase("Regular") ? 20 : 17; // Default Vacation Leaves
        remainingLeaves[1] = 10; // Default Sick Leaves
    } else {
        // Get remaining leaves from the most recent leave request in the same year
        String[] mostRecentRequest = getMostRecentLeaveRequest(empID, year);

        if (mostRecentRequest != null) {
            remainingLeaves[0] = Integer.parseInt(mostRecentRequest[12]); // Remaining Vacation Leaves
            remainingLeaves[1] = Integer.parseInt(mostRecentRequest[13]); // Remaining Sick Leaves
        } else {
            // Handle case if no previous request found (though theoretically, it should not happen if isFirstLeaveRequestForYear returns false)
            String status = empstatusTF.getText().trim();
            remainingLeaves[0] = status.equalsIgnoreCase("Regular") ? 20 : 17; // Default Vacation Leaves
            remainingLeaves[1] = 10; // Default Sick Leaves
        }
    }

    return remainingLeaves;
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
        empstatusTF = new javax.swing.JTextField();
        empstatusLBL = new javax.swing.JLabel();
        empleavedetPanel = new javax.swing.JPanel();
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
        typeofleaveLBL = new javax.swing.JLabel();
        typeofleaveCB = new javax.swing.JComboBox<>();
        rlsupervisorTF = new javax.swing.JTextField();
        rlsupervisorLBL = new javax.swing.JLabel();

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

        rlemptableSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        rlemptableSP.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "EID", "First Name", "Last Name", "Position", "Emp Status", "Supervisor", "TOP", "Note", "Start", "End", "Leave Status", "VL Remaining ", "SL Remaining "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        rlemptableSP.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout rlemptablePNLLayout = new javax.swing.GroupLayout(rlemptablePNL);
        rlemptablePNL.setLayout(rlemptablePNLLayout);
        rlemptablePNLLayout.setHorizontalGroup(
            rlemptablePNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rlemptablePNLLayout.createSequentialGroup()
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

        empstatusTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empstatusTFActionPerformed(evt);
            }
        });

        empstatusLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        empstatusLBL.setForeground(new java.awt.Color(0, 0, 0));
        empstatusLBL.setText("Employement Status:");

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
                .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(empstatusTF, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(rldateofsubLBL1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(empIDreqTF, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                        .addComponent(rllastnameLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rllastnameTF))
                    .addComponent(empstatusLBL))
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
                .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rlpositionLBL)
                    .addComponent(empstatusLBL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(empdetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rlpositionTF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(empstatusTF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        empleavedetPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

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

        typeofleaveLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        typeofleaveLBL.setForeground(new java.awt.Color(0, 0, 0));
        typeofleaveLBL.setText("Type of Leave:");

        typeofleaveCB.setBackground(new java.awt.Color(255, 255, 255));
        typeofleaveCB.setEditable(true);
        typeofleaveCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Sick Leave ", "Vacation Leave ", "Paternity Leave ", "Maternity Leave " }));
        typeofleaveCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeofleaveCBActionPerformed(evt);
            }
        });

        rlsupervisorTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rlsupervisorTFActionPerformed(evt);
            }
        });

        rlsupervisorLBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rlsupervisorLBL.setForeground(new java.awt.Color(0, 0, 0));
        rlsupervisorLBL.setText("Immediate Supervisor:");

        javax.swing.GroupLayout empleavedetPanelLayout = new javax.swing.GroupLayout(empleavedetPanel);
        empleavedetPanel.setLayout(empleavedetPanelLayout);
        empleavedetPanelLayout.setHorizontalGroup(
            empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empleavedetPanelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startLeaveLBL)
                    .addComponent(startofleaveDC, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(empleavedetPanelLayout.createSequentialGroup()
                        .addComponent(submitleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(clearleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelLeavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rlsupervisorTF, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rlsupervisorLBL))
                .addGap(36, 36, 36)
                .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(typeofleaveLBL)
                    .addComponent(typeofleaveCB, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(noteLBL)
                        .addComponent(endofleaveDC, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                        .addComponent(endLeaveLBL)
                        .addComponent(jScrollPane1)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        empleavedetPanelLayout.setVerticalGroup(
            empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empleavedetPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeofleaveLBL)
                    .addComponent(rlsupervisorLBL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeofleaveCB, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rlsupervisorTF, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(empleavedetPanelLayout.createSequentialGroup()
                        .addComponent(noteLBL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(empleavedetPanelLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(empleavedetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(submitleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clearleavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cancelLeavePB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(39, Short.MAX_VALUE))
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
                    .addComponent(empleavedetPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(empdetPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(rlemptablePNL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
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
        Vacation Leave 
        Paternity Leave 
        Maternity Leave 
        */
    }//GEN-LAST:event_typeofleaveCBActionPerformed

    private void rlfirstnameTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rlfirstnameTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rlfirstnameTFActionPerformed
    
    private void submitleavePBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitleavePBActionPerformed
       // Capture and format the dates from the form fields
           Date submissionDateObj = dateofsubmissionDC.getDate();
    Calendar cal = Calendar.getInstance();
    cal.setTime(submissionDateObj);
    int year = cal.get(Calendar.YEAR);

    // Retrieve and trim input fields
    String empID = empIDreqTF.getText().trim();
    String firstName = rlfirstnameTF.getText().trim();
    String lastName = rllastnameTF.getText().trim();
    String position = rlpositionTF.getText().trim();
    String supervisor = rlsupervisorTF.getText().trim();
    String status = empstatusTF.getText().trim();
    String leaveType = typeofleaveCB.getSelectedItem().toString().trim();
    String note = noteTA.getText().trim();

    // Format start and end dates
    Date startDateObj = startofleaveDC.getDate();
    String startDate = startDateObj != null ? dateFormat.format(startDateObj) : "";
    Date endDateObj = endofleaveDC.getDate();
    String endDate = endDateObj != null ? dateFormat.format(endDateObj) : "";

    // Validate date selection and other required fields
    if (startDateObj == null || endDateObj == null ||
            empID.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
            position.isEmpty() || supervisor.isEmpty() ||
            leaveType.equals("Select") || startDate.isEmpty() || endDate.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check for commas in input fields to avoid CSV parsing issues
    if (containsComma(empID) || containsComma(firstName) || containsComma(lastName) ||
            containsComma(position) || containsComma(supervisor) || containsComma(leaveType) || containsComma(note)) {
        JOptionPane.showMessageDialog(this, "Input fields must not contain commas.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Prevent duplicate leave requests for the same employee on the same dates
    if (isDuplicateEntry(empID, startDate, endDate)) {
        JOptionPane.showMessageDialog(this, "Duplicate leave request found. Please check the existing entries.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Calculate the duration of the leave in days
    long leaveDuration = ChronoUnit.DAYS.between(
            startDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            endDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;

    // Get remaining leaves based on the most recent leave request in the same year
    int[] remainingLeaves = calculateRemainingLeaves(empID, year);

    // Check if the leave can be submitted based on remaining balances
    if (!canSubmitLeave(leaveType, leaveDuration, remainingLeaves[0], remainingLeaves[1])) {
        return;
    }

    // Format fields for CSV to handle special characters
    firstName = formatFieldWithQuotes(firstName);
    lastName = formatFieldWithQuotes(lastName);
    position = formatFieldWithQuotes(position);
    supervisor = formatFieldWithQuotes(supervisor);
    leaveType = formatFieldWithQuotes(leaveType);
    note = formatFieldWithQuotes(note);

    // Write the leave request to the CSV file
    try (FileWriter fileWriter = new FileWriter("src/CSV/Leave Requests DB.csv", true);
            BufferedWriter writer = new BufferedWriter(fileWriter)) {

        // Write the leave request to the CSV file with remaining leaves
        writer.write(String.join(",", dateFormat.format(submissionDateObj), empID, firstName, lastName, position, status, supervisor,
        leaveType, note, startDate, endDate, "Pending", String.valueOf(remainingLeaves[0]), String.valueOf(remainingLeaves[1])) + "\n");
        writer.flush();

        // Update the table to reflect the new leave request and clear input fields
        populateJTable(empID);
        JOptionPane.showMessageDialog(this, "Leave request submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFields();

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving leave request.", "File Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
                    

        // This method checks if a leave can be submitted based on remaining leave balances and duration
        private boolean canSubmitLeave(String leaveType, long leaveDuration, int remainingVacationLeaves, int remainingSickLeaves) {
            if (leaveType.equals("Vacation Leave")) {
                if (remainingVacationLeaves < leaveDuration) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot submit Vacation Leave request as it exceeds your remaining leave credits (" + remainingVacationLeaves + " days).",
                            "Leave Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else if (leaveType.equals("Sick Leave")) {
                if (remainingSickLeaves < leaveDuration) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot submit Sick Leave request as it exceeds your remaining leave credits (" + remainingSickLeaves + " days).",
                            "Leave Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return true;
        }

        // Method to get the most recent leave request for an employee in the same year
        private String[] getMostRecentLeaveRequest(String empID, int year) {
            String[] mostRecentRequest = null;

            try {
                File file = new File("src/CSV/Leave Requests DB.csv");
                try (CSVReader reader = new CSVReader(new FileReader(file))) {
                    reader.readNext(); // Skip header row
                    String[] line;
                    while ((line = reader.readNext()) != null) {
                        if (line.length >= 14 && line[1].trim().equals(empID)) {
                            Date leaveDate = dateFormat.parse(line[0]); // Date of submission
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(leaveDate);
                            if (cal.get(Calendar.YEAR) == year) {
                                mostRecentRequest = line;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mostRecentRequest;
        }

        // This method retrieves the remaining leave balances for an employee
        private int getRemainingLeaves(String empID, String leaveType, String status, int year) {
    int remainingLeaves = 0;
    int defaultVacationLeaves = status.equals("Regular") ? 20 : 17;
    int defaultSickLeaves = 10;

    // Check if it's the first leave request of the year for the employee
    if (isFirstLeaveRequestForYear(empID, year)) {
        return leaveType.equals("Vacation Leave") ? defaultVacationLeaves : defaultSickLeaves;
    }

    // Read the Leave Requests DB to find the most recent leave balance
    try {
        File file = new File("src/CSV/Leave Requests DB.csv");
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] header = reader.readNext(); // Read and discard header
            String[] line;
            String[] mostRecentEntry = null;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 14 && line[1].trim().equals(empID)) {
                    Date leaveDate = dateFormat.parse(line[0]); // Date of submission
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(leaveDate);
                    if (cal.get(Calendar.YEAR) == year) {
                        mostRecentEntry = line;
                    }
                }
            }
            if (mostRecentEntry != null) {
                remainingLeaves = leaveType.equals("Vacation Leave") ?
                        Integer.parseInt(mostRecentEntry[12]) :
                        Integer.parseInt(mostRecentEntry[13]);
            } else {
                // If no entries found for this year, return default values
                return leaveType.equals("Vacation Leave") ? defaultVacationLeaves : defaultSickLeaves;
            }
        }
    } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException | ParseException e) {
        e.printStackTrace(); // Improve error handling by logging or displaying a message
        // Return default values in case of error
        return leaveType.equals("Vacation Leave") ? defaultVacationLeaves : defaultSickLeaves;
    }

    return remainingLeaves;
}

        
        // This method checks if a leave request for the same dates already exists
        private boolean isDuplicateEntry(String empID, String startDate, String endDate) {
            boolean isDuplicate = false;

            // Read leave requests from the file to check for duplicates
            try {
                File file = new File("src/CSV/Leave Requests DB.csv");
                try (CSVReader reader = new CSVReader(new FileReader(file))) {
                    reader.readNext(); // Read and discard the header row
                    String[] line;
                    while ((line = reader.readNext()) != null) {
                        if (line.length >= 11 && line[1].trim().equals(empID) && line[9].trim().equals(startDate) && line[10].trim().equals(endDate)) {
                            isDuplicate = true;
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return isDuplicate;
        }

        // This method validates if a leave can be submitted based on remaining leave balances and duration
        private boolean canSubmitLeave(String empID, String leaveType, long leaveDuration, String status, int year) {
            int remainingLeaves = getRemainingLeaves(empID, leaveType, status, year);

            if (remainingLeaves < leaveDuration) {
                JOptionPane.showMessageDialog(this, 
                    "Cannot submit " + leaveType + " request as it exceeds your remaining leave credits (" + remainingLeaves + " days).", 
                    "Leave Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }

        // This method checks if a string contains a comma to prevent CSV parsing issues
        private boolean containsComma(String value) {
            return value.contains(",");
        }

        // This method formats a field to include quotes if it contains a comma or double quotes
        private String formatFieldWithQuotes(String field) {
            if (field.contains("\"")) {
                field = field.replace("\"", "\"\"");
            }
            if (field.contains(",")) {
                field = "\"" + field + "\"";
            }
            return field;
        }

        // This method clears all the input fields in the form after submission
        private void clearFields() {
            dateofsubmissionDC.setDate(null);
            empIDreqTF.setText("");
            rlfirstnameTF.setText("");
            rllastnameTF.setText("");
            rlpositionTF.setText("");
            empstatusTF.setText("");
            rlsupervisorTF.setText("");
            typeofleaveCB.setSelectedIndex(0);
            noteTA.setText("");
            startofleaveDC.setDate(null);
            endofleaveDC.setDate(null);        
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
        //Cancel button code
        int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a leave request to cancel.", "Cancel Request", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            String status = (String) model.getValueAt(selectedRow, 11); // LStatus (Leave Status) column index

            if (!status.equals("Pending")) {
                JOptionPane.showMessageDialog(this, "You can only cancel leave requests with 'Pending' status.", "Cancel Request", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel the selected leave request?", "Cancel Request", JOptionPane.YES_NO_OPTION);
            if (confirmResult == JOptionPane.YES_OPTION) {
                String empID = (String) model.getValueAt(selectedRow, 1); // ID column index
                String startDate = (String) model.getValueAt(selectedRow, 9); // Start date column index
                String endDate = (String) model.getValueAt(selectedRow, 10); // End date column index

                try {
                    if (removeLeaveRequest(empID, startDate, endDate)) {
                        model.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(this, "Leave request canceled successfully.", "Cancel Request", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel leave request. Please try again.", "Cancel Request", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error occurred while canceling leave request.", "Cancel Request", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace(); // Handle exception properly based on your application's error logging strategy
                }
            }
        
    }//GEN-LAST:event_cancelLeavePBActionPerformed
    
    private boolean removeLeaveRequest(String empID, String startDate, String endDate) throws IOException {
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
            if (!(line.length == 14 && line[1].equals(empID) && line[9].equals(startDate) && line[10].equals(endDate))) {
                bw.write(String.join(",", line) + "\n");
            }
        }
    }

    if (!file.delete()) {
        System.out.println("Could not delete original file.");
    }
    if (!tempFile.renameTo(file)) {
        System.out.println("Could not rename temp file to original file.");
    }

    populateJTable(employeeID); // Assuming this method updates your JTable with the latest data
    return true;
}
    
    private void rlsupervisorTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rlsupervisorTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rlsupervisorTFActionPerformed

    private void empstatusTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empstatusTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_empstatusTFActionPerformed
    
    

    
    
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
    private javax.swing.JLabel empstatusLBL;
    private javax.swing.JTextField empstatusTF;
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
    
