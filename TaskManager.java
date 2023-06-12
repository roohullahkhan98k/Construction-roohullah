import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Calendar;

class Task {
    private String name;
    private String description;
    private LocalDateTime dueDateTime;
    private String priority;
    private boolean reminderShown;

    public Task(String name, String description, LocalDateTime dueDateTime, String priority) {
        this.name = name;
        this.description = description;
        this.dueDateTime = dueDateTime;
        this.priority = priority;
        this.reminderShown = false;
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

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isReminderShown() {
        return reminderShown;
    }

    public void setReminderShown(boolean reminderShown) {
        this.reminderShown = reminderShown;
    }

    @Override
    public String toString() {
        return name;
    }
}

public class TaskManager extends JFrame {

    private JTextField taskNameField;
    private JTextArea taskDescriptionArea;
    private JSpinner taskDueDateSpinner;
    private JSpinner taskDueTimeSpinner;
    private JComboBox<String> priorityComboBox;
    private JList<Task> taskList;
    private DefaultListModel<Task> taskListModel;
    private List<Task> tasks;

    public TaskManager() {
        setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tasks = new ArrayList<>();
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);

        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton removeButton = new JButton("Remove Task");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

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

        checkReminders();
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Task Name:");
        taskNameField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description:");
        taskDescriptionArea = new JTextArea();
        JScrollPane descriptionScrollPane = new JScrollPane(taskDescriptionArea);

        JLabel dueDateLabel = new JLabel("Due Date:");
        taskDueDateSpinner = new JSpinner(new SpinnerDateModel());
        taskDueDateSpinner.setEditor(new JSpinner.DateEditor(taskDueDateSpinner, "yyyy-MM-dd"));

        JLabel dueTimeLabel = new JLabel("Due Time:");
        taskDueTimeSpinner = new JSpinner(new SpinnerDateModel());
        taskDueTimeSpinner.setEditor(new JSpinner.DateEditor(taskDueTimeSpinner, "HH:mm"));

        JLabel priorityLabel = new JLabel("Priority:");
        String[] priorities = {"Low", "Medium", "High"};
        priorityComboBox = new JComboBox<>(priorities);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        inputPanel.add(descriptionLabel, gbc);

        gbc.gridy = 2;
        inputPanel.add(dueDateLabel, gbc);

        gbc.gridy = 3;
        inputPanel.add(dueTimeLabel, gbc);

        gbc.gridy = 4;
        inputPanel.add(priorityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(taskNameField, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(descriptionScrollPane, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(taskDueDateSpinner, gbc);

        gbc.gridy = 3;
        inputPanel.add(taskDueTimeSpinner, gbc);

        gbc.gridy = 4;
        inputPanel.add(priorityComboBox, gbc);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        return buttonPanel;
    }

    private void addTask() {
        String name = taskNameField.getText();
        String description = taskDescriptionArea.getText();
        LocalDateTime dueDateTime = getSelectedDateTime();
        String priority = priorityComboBox.getSelectedItem().toString();

        Task task = new Task(name, description, dueDateTime, priority);
        tasks.add(task);
        taskListModel.addElement(task);

        clearFields();
        sortTasksByPriority();
    }

    private void editTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = taskListModel.getElementAt(selectedIndex);
            String name = taskNameField.getText();
            String description = taskDescriptionArea.getText();
            LocalDateTime dueDateTime = getSelectedDateTime();
            String priority = priorityComboBox.getSelectedItem().toString();

            boolean isOverdue = task.getDueDateTime().isBefore(LocalDateTime.now());
            boolean isReminderShown = task.isReminderShown();

            task.setName(name);
            task.setDescription(description);
            task.setDueDateTime(dueDateTime);
            task.setPriority(priority);

            if (isOverdue && !isReminderShown) {
                task.setReminderShown(true);
            }

            taskList.repaint();
            clearFields();
            sortTasksByPriority();
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = taskListModel.getElementAt(selectedIndex);
            tasks.remove(task);
            taskListModel.remove(selectedIndex);
            clearFields();
        }
    }

    private LocalDateTime getSelectedDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((java.util.Date) taskDueDateSpinner.getValue());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime((java.util.Date) taskDueTimeSpinner.getValue());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    private void clearFields() {
        taskNameField.setText("");
        taskDescriptionArea.setText("");
        taskDueDateSpinner.setValue(new java.util.Date());
        taskDueTimeSpinner.setValue(new java.util.Date());
        priorityComboBox.setSelectedIndex(0);
    }

    private void sortTasksByPriority() {
        tasks.sort(Comparator.comparing(Task::getPriority));
        taskListModel.clear();
        for (Task task : tasks) {
            taskListModel.addElement(task);
        }
    }

    private void checkReminders() {
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Task task : tasks) {
                    if (!task.isReminderShown() && task.getDueDateTime().isBefore(LocalDateTime.now())) {
                        JOptionPane.showMessageDialog(TaskManager.this, "Reminder: Task '" + task.getName() + "' is overdue!");
                        task.setReminderShown(true);
                    }
                }
            }
        });
        timer.start();
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
