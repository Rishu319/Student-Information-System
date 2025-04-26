import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

interface Grading {
    String calculateGrade(double percentage);
}

class Person {
    String name;
    int age;

    Person() {
        this("Unknown", 0);
    }

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class StudentBase extends Person {
    String studentId;
    static int studentCount = 0;

    StudentBase(String name, int age, String studentId) {
        super(name, age);
        this.studentId = studentId;
        studentCount++;
    }

    static int getStudentCount() {
        return studentCount;
    }

    static void decrementStudentCount() {
        studentCount--;
    }
}

class GraduateStudent extends StudentBase implements Grading {
    double percentage;

    GraduateStudent(String name, int age, String studentId, double percentage) {
        super(name, age, studentId);
        this.percentage = percentage;
    }

    @Override
    public String calculateGrade(double percentage) {
        if (percentage >= 85)
            return "A";
        else if (percentage >= 70)
            return "B";
        else if (percentage >= 50)
            return "C";
        else
            return "F";
    }

    Object[] toTableRow() {
        return new Object[]{studentId, name, age, percentage, calculateGrade(percentage)};
    }
}

public class StudentInfoGUI extends JFrame implements ActionListener {

    JTextField nameField, ageField, idField, percentageField;
    JButton addButton, deleteButton;
    JTable studentTable;
    DefaultTableModel tableModel;
    ArrayList<GraduateStudent> studentList = new ArrayList<>();
    JLabel countLabel;

    public StudentInfoGUI() {
        setTitle("Student Information System");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel - Input Form
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Student Details"));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Percentage:"));
        percentageField = new JTextField();
        inputPanel.add(percentageField);

        addButton = new JButton("Add Student");
        addButton.addActionListener(this);
        inputPanel.add(addButton);

        deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center Panel - Table
        String[] columns = {"Student ID", "Name", "Age", "Percentage", "Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Bottom Panel - Student Count
        JPanel bottomPanel = new JPanel();
        countLabel = new JLabel("Total Students: 0");
        bottomPanel.add(countLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String studentId = idField.getText().trim();
            double percentage = Double.parseDouble(percentageField.getText().trim());

            GraduateStudent student = new GraduateStudent(name, age, studentId, percentage);
            studentList.add(student);
            tableModel.addRow(student.toTableRow());
            updateStudentCount();

            // Clear fields
            nameField.setText("");
            ageField.setText("");
            idField.setText("");
            percentageField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter correct values.");
        }
    }

    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            studentList.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            StudentBase.decrementStudentCount();
            updateStudentCount();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
        }
    }

    private void updateStudentCount() {
        countLabel.setText("Total Students: " + StudentBase.getStudentCount());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentInfoGUI::new);
    }
}