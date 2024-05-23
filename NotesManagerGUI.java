import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

// Class declaration
public class NotesManagerGUI {
    // Instance variables
    private JFrame frame; // Main window frame
    private JPanel panel; // Main panel for holding components
    private JTextField usernameField; // Text field for entering username
    private JPasswordField passwordField; // Password field for entering password
    private JButton loginButton; // Button for login
    private JButton registerButton; // Button for registering new user
    private String currentUser; // Store current user's username

    // Constructor
    public NotesManagerGUI() {
        // Create the main window frame
        frame = new JFrame("Notes Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application when window is closed
        frame.setPreferredSize(new Dimension(600, 400)); // Set preferred size of the frame
        
        // Create the main panel with grid layout
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        // Initialize GUI components
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Add action listeners to buttons
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login(); // Call login method when login button is clicked
            }
        });
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register(); // Call register method when register button is clicked
            }
        });

        // Add components to the panel
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        // Add panel to the frame and display the frame
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    // Method to handle login process
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // Authenticate user
        if (authenticate(username, password)) {
            currentUser = username;
            openMainMenu(); // Open main menu after successful login
        } else {
            // Show error message if authentication fails
            JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle user registration
    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // Validate username and password
        if (!isValidUsername(username)) {
            JOptionPane.showMessageDialog(frame, "Invalid username. Please enter alphanumeric characters only.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(frame, "Invalid password. Please enter at least 6 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create new user account
        File accountFile = new File("accounts/" + username + ".txt");
        if (accountFile.exists()) {
            JOptionPane.showMessageDialog(frame, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            FileWriter writer = new FileWriter(accountFile);
            writer.write(username + "\n" + password);
            writer.close();
            JOptionPane.showMessageDialog(frame, "Account created successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error creating account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to authenticate user
    private boolean authenticate(String username, String password) {
        // Check if user's account file exists and contains correct credentials
        File accountFile = new File("accounts/" + username + ".txt");
        if (accountFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(accountFile));
                String storedUsername = reader.readLine();
                String storedPassword = reader.readLine();
                reader.close();
                return username.equals(storedUsername) && password.equals(storedPassword);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error reading account file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    // Method to open main menu
    private void openMainMenu() {
        frame.getContentPane().removeAll();
        frame.repaint();

        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton viewNotesButton = new JButton("View Notes");
        viewNotesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewNotes();
            }
        });

        JButton createNotesButton = new JButton("Create Note");
        createNotesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNote();
            }
        });

        JButton deleteNotesButton = new JButton("Delete Note");
        deleteNotesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteNote();
            }
        });

        panel.add(viewNotesButton);
        panel.add(createNotesButton);
        panel.add(deleteNotesButton);

        frame.getContentPane().add(panel);
        frame.pack();
    }

    // Other methods for managing notes (viewing, creating, deleting)...

    // Helper method to validate username
    private boolean isValidUsername(String username) {
        // Check if username contains only alphanumeric characters
        return username.matches("[a-zA-Z0-9]+");
    }

    // Helper method to validate password
    private boolean isValidPassword(String password) {
        // Check if password has at least 6 characters
        return password.length() >= 6;
    }

    // Main method
    public static void main(String[] args) {
        // Create directories for accounts and notes if they don't exist
        File accountsDir = new File("accounts");
        if (!accountsDir.exists()) {
            boolean created = accountsDir.mkdir();
            if (!created) {
                System.out.println("Failed to create accounts directory.");
                return;
            }
        }

        File notesDir = new File("notes");
        if (!notesDir.exists()) {
            boolean created = notesDir.mkdir();
            if (!created) {
                System.out.println("Failed to create notes directory.");
                return;
            }
        }

        // Launch the GUI on the event dispatch thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NotesManagerGUI();
            }
        });
    }
}
