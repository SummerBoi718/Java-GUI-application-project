import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotesManagerGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private String currentUser;

    // Constructor
    public NotesManagerGUI() {
        frame = new JFrame("Notes Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 400)); // Set preferred size of the frame

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

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
            openMainMenu();
        } else {
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


        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewLoginandSignIn();
			}
	    });

        panel.add(viewNotesButton);
        panel.add(createNotesButton);
        panel.add(backButton);

        frame.getContentPane().add(panel);
        frame.pack();
    }
    //view loginandsignin scene
    private void viewLoginandSignIn(){
		frame.getContentPane().removeAll();
        frame.repaint();

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

		usernameField = new JTextField();
		passwordField = new JPasswordField();
				        loginButton = new JButton("Login");
				        loginButton.addActionListener(new ActionListener() {
				            public void actionPerformed(ActionEvent e) {
				                login();
				            }
				        });
				        registerButton = new JButton("Register");
				        registerButton.addActionListener(new ActionListener() {
				            public void actionPerformed(ActionEvent e) {
				                register();
				            }
				        });


				        panel.add(new JLabel("Username:"));
				        panel.add(usernameField);
				        panel.add(new JLabel("Password:"));
				        panel.add(passwordField);
				        panel.add(loginButton);
				        panel.add(registerButton);

				        frame.getContentPane().add(panel);
		                frame.pack();

		}

    // Method to view notes
    private void viewNotes() {
        frame.getContentPane().removeAll();
        frame.repaint();

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea notesArea = new JTextArea();
        notesArea.setEditable(false); // Make the text area read-only

        JScrollPane scrollPane = new JScrollPane(notesArea);

        // Load and display existing notes for the current user
        String notes = loadNotes();
        notesArea.setText(notes);

        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openMainMenu();
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.pack();
    }

    // Method to create a note
    private void createNote() {
    frame.getContentPane().removeAll();
    frame.repaint();

    panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JTextField titleField = new JTextField();
    JTextArea contentArea = new JTextArea();
    contentArea.setRows(10); // Set rows for the content area
    contentArea.setColumns(40); // Set columns for the content area

    JButton saveButton = new JButton("Save Note");
    saveButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            saveNoteAndReturn(titleField.getText(), contentArea.getText());
        }
    });

    JButton backButton = new JButton("Back");
    backButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            openMainMenu();
        }
    });

    // Panel for buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
    buttonPanel.add(backButton);
    buttonPanel.add(saveButton);

    panel.add(new JLabel("Title:"), BorderLayout.NORTH);
    panel.add(titleField, BorderLayout.NORTH);
    panel.add(new JLabel("Content:"), BorderLayout.CENTER);
    panel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the SOUTH position

    frame.getContentPane().add(panel);
    frame.pack();
   }

    // Method to delete a note
    private void deleteNote() {
        JFileChooser fileChooser = new JFileChooser(new File("notes/" + currentUser));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.delete()) {
                JOptionPane.showMessageDialog(frame, "Note deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Error deleting note.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to load notes
    private String loadNotes() {
        StringBuilder notes = new StringBuilder();
        File userDir = new File("notes/" + currentUser);
        if (userDir.exists()) {
            File[] noteFiles = userDir.listFiles();
            if (noteFiles != null) {
                for (File noteFile : noteFiles) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(noteFile));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            notes.append(line).append("\n");
                        }
                        reader.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error reading note file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        return notes.toString();
    }

    // Method to save a note and return to the main menu
    private void saveNoteAndReturn(String title, String content) {
        // Validate title
        if (title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Title cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create directory for user's notes if it doesn't exist
        File userDir = new File("notes/" + currentUser);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        // Create the note file
        File noteFile = new File(userDir, title + ".txt");
        try {
            FileWriter writer = new FileWriter(noteFile);
            writer.write(content);
            writer.close();
            JOptionPane.showMessageDialog(frame, "Note created successfully.");
            openMainMenu(); // Return to the main menu after saving the note
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error creating note: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to validate username
    private boolean isValidUsername(String username) {
        // Check if username contains only alphanumeric characters
        return username.matches("[a-zA-Z0-9]+");
    }

    // Method to validate password
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

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NotesManagerGUI();
            }
        });
    }
}
