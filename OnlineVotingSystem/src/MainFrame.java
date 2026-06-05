import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private Election election;
    private VoterDatabase db;
    private Color green  = new Color(39, 174, 96);
    private Color red    = new Color(192, 57, 43);
    private Color blue   = new Color(41, 128, 185);
    private Color dark   = new Color(44, 62, 80);
    private Color light  = new Color(236, 240, 241);

    public MainFrame(Election election, VoterDatabase db) {
        this.election = election;
        this.db = db;

        setTitle("Online Voting System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top title bar
        JLabel title = new JLabel("ONLINE VOTING SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(dark);
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Center buttons panel
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(light);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(15, 0, 15, 0);
        g.gridx = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.ipadx = 200;
        g.ipady = 20;

        JButton voteBtn    = makeButton("🗳  Vote Now",       green);
        JButton resultBtn  = makeButton("📊  View Results",   blue);
        JButton closeBtn   = makeButton("🔒  Close Election", red);
        JButton exitBtn    = makeButton("❌  Exit",           dark);

        g.gridy = 0; center.add(voteBtn,   g);
        g.gridy = 1; center.add(resultBtn, g);
        g.gridy = 2; center.add(closeBtn,  g);
        g.gridy = 3; center.add(exitBtn,   g);

        add(center, BorderLayout.CENTER);

        // Status bar
        JLabel status = new JLabel("  Welcome! Please login to vote.", SwingConstants.LEFT);
        status.setOpaque(true);
        status.setBackground(dark);
        status.setForeground(Color.LIGHT_GRAY);
        status.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 0));
        add(status, BorderLayout.SOUTH);

        // Button actions
        voteBtn.addActionListener(e -> showVoteDialog(status));
        resultBtn.addActionListener(e -> showResults());
        closeBtn.addActionListener(e -> {
            election.closeElection();
            status.setText("  Election has been closed.");
            closeBtn.setEnabled(false);
            voteBtn.setEnabled(false);
        });
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showVoteDialog(JLabel status) {
        if (!election.isActive()) {
            JOptionPane.showMessageDialog(this,
                "Election is closed!", "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Login panel
        JPanel loginPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField idField   = new JTextField();
        JPasswordField passField = new JPasswordField();
        loginPanel.add(new JLabel("Voter ID:"));
        loginPanel.add(idField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passField);

        int res = JOptionPane.showConfirmDialog(this, loginPanel,
            "Voter Login", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        String id   = idField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        Voter voter = db.authenticate(id, pass);

        if (voter == null) {
            JOptionPane.showMessageDialog(this,
                "Invalid ID or Password!", "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (voter.hasVoted()) {
            JOptionPane.showMessageDialog(this,
                voter.getName() + ", you have already voted!",
                "Already Voted", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Candidate selection
        List<Candidate> candidates = election.getCandidates();
        String[] options = new String[candidates.size()];
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            options[i] = c.getCandidateId() + " — " + c.getName() + " (" + c.getParty() + ")";
        }

        String choice = (String) JOptionPane.showInputDialog(
            this,
            "Welcome " + voter.getName() + "!\nSelect your candidate:",
            "Cast Your Vote",
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

        if (choice == null) return;

        String candidateId = choice.split(" — ")[0];
        boolean success = election.castVote(candidateId);
        if (success) {
            db.markVoted(id);
            status.setText("  " + voter.getName() + " voted successfully!");
            JOptionPane.showMessageDialog(this,
                "✅ Vote cast successfully!\nThank you, " + voter.getName(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showResults() {
        List<Candidate> candidates = election.getCandidates();
        int totalVoted = db.getTotalVoted();

        // Table columns
        String[] cols = {"Candidate", "Party", "Votes", "Percentage"};
        Object[][] data = new Object[candidates.size()][4];

        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            double pct = totalVoted > 0
                ? (c.getVoteCount() * 100.0 / totalVoted) : 0.0;
            data[i][0] = c.getName();
            data[i][1] = c.getParty();
            data[i][2] = c.getVoteCount();
            data[i][3] = String.format("%.1f%%", pct);
        }

        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        // Table styling
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(dark);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(blue);
        table.setGridColor(Color.LIGHT_GRAY);

        // Color rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) {
                    setBackground(row % 2 == 0
                        ? Color.WHITE : new Color(214, 234, 248));
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 150));

        // Winner label
        Candidate winner = election.getWinner();
        String winnerText = (winner != null && winner.getVoteCount() > 0)
            ? "🏆  Winner: " + winner.getName() + " (" + winner.getParty() + ")"
            : "No votes cast yet.";

        JLabel winnerLabel = new JLabel(winnerText, SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        winnerLabel.setForeground(new Color(39, 174, 96));
        winnerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        JLabel statsLabel = new JLabel(
            "Total Voters: " + db.getTotalVoters() +
            "   |   Total Voted: " + totalVoted,
            SwingConstants.CENTER);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        statsLabel.setForeground(Color.GRAY);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.add(statsLabel,  BorderLayout.NORTH);
        panel.add(scroll,      BorderLayout.CENTER);
        panel.add(winnerLabel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel,
            "Election Results — " + election.getElectionName(),
            JOptionPane.PLAIN_MESSAGE);
    }
}