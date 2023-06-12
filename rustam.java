import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskManager extends JFrame {

    private List<Task> tasks;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskNameField;
    private JComboBox<String> priorityComboBox;
    private JButton addButton;
    private JButton removeButton;

    public TaskManager() {
        // Set up the main window
        setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        // Initialize the tasks list
        tasks = new ArrayList<>();

        // Create the task list model
        taskListModel = new DefaultListModel<>();

        // Create the task list
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        // Create the task input fields
        JLabel taskNameLabel = new JLabel("Task Name:");
        taskNameField = new JTextField();
        JLabel priorityLabel = new JLabel("Priority:");
        priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});

        // Create the add and remove buttons
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        // Set up the main layout
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(taskNameLabel);
        formPanel.add(taskNameField);
        formPanel.add(priorityLabel);
        formPanel.add(priorityComboBox);
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTask();
            }
        });
    }

    private void addTask() {
        String taskName = taskNameField.getText().trim();
        String priority = (String) priorityComboBox.getSelectedItem();

        if (!taskName.isEmpty()) {
            Task newTask = new Task(taskName, priority);
            tasks.add(newTask);
            sortTasksByPriority();
            taskListModel.addElement(newTask);
            clearTaskInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a task name.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
            taskListModel.remove(selectedIndex);
        }
    }

    private void clearTaskInputFields() {
        taskNameField.setText("");
        priorityComboBox.setSelectedIndex(0);
    }

    private void sortTasksByPriority() {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getPriority().compareTo(task2.getPriority());
            }
        });
    }

    private class Task {
        private String name;
        private String priority;

        public Task(String name, String priority) {
            this.name = name;
            this.priority = priority;
        }

        public String getName() {
            return name;
        }

        public String getPriority() {
            return priority;
        }

        @Override
        public String toString() {
            return name + " (" + priority + ")";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TaskManager().setVisible(true);
            }
        });
    }
}
