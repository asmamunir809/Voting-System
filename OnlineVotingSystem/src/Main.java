import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        VoterDatabase db = new VoterDatabase();
        db.registerVoter(new Voter("V001", "Ali Hassan",   "pass123"));
        db.registerVoter(new Voter("V002", "Sara Khan",    "sara456"));
        db.registerVoter(new Voter("V003", "Ahmed Raza",   "ahmed789"));
        db.registerVoter(new Voter("V004", "Fatima Malik", "fatima321"));

        Election election = new Election("General Election 2025");
        election.addCandidate(new Candidate("C1", "Imran Khan", "PTI"));
        election.addCandidate(new Candidate("C2", "Nawaz Sharif",     "PML-N"));
        election.addCandidate(new Candidate("C3", "Bilawal Bhutto",   "PPP"));

        SwingUtilities.invokeLater(() -> new MainFrame(election, db));
    }
}