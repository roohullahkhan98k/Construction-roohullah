import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TaskManager extends JFrame {

    private List<Task> tasks;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskNameField;
    private JTextArea taskDescriptionArea;
    private JButton addButton;
    private JButton editButton;
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
        JLabel taskDescriptionLabel = new JLabel("Description:");
        taskDescriptionArea = new JTextArea();
        JScrollPane descriptionScrollPane = new JScrollPane(taskDescriptionArea);

        // Create the add, edit, and remove buttons
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        removeButton = new JButton("Remove");

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        // Set up the main layout
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(taskNameLabel);
        formPanel.add(taskNameField);
        formPanel.add(taskDescriptionLabel);
        formPanel.add(descriptionScrollPane);
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

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTask();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTask();
            }
        });

        // Set up the task reminder timer
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkTaskReminders();
            }
        }, 0, 60000); // Check every minute

    }

    private void addTask() {
        String taskName = taskNameField.getText();
        String description = taskDescriptionArea.getText();

        if (!taskName.isEmpty()) {
            Task newTask = new Task(taskName, description);
            tasks.add(newTask);
            taskListModel.addElement(newTask);
            clearTaskInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter task name.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editTask() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            String newTaskName = JOptionPane.showInputDialog(this, "Enter a new task name:", selectedTask.getName());
            if (newTaskName != null && !newTaskName.isEmpty()) {
                String newDescription = JOptionPane.showInputDialog(this, "Enter the new description:", selectedTask.getDescription());

                if (newDescription != null) {
                    selectedTask.setName(newTaskName);
                    selectedTask.setDescription(newDescription);
                    taskListModel.set(taskList.getSelectedIndex(), selectedTask);
                }
            }
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
            taskListModel.remove(selectedIndex);
        }
    }

    private void checkTaskReminders() {
        // No changes made to this method
    }

    private void clearTaskInputFields() {
        taskNameField.setText("");
        taskDescriptionArea.setText("");
    }

    private class Task {
        private String name;
        private String description;

        public Task(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return name;
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
