import java.util.ArrayList;
import java.util.List;

public class Election {
    private String electionName;
    private List<Candidate> candidates;
    private boolean isActive;

    public Election(String electionName) {
        this.electionName = electionName;
        this.candidates = new ArrayList<>();
        this.isActive = true;
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public String getElectionName() {
        return electionName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void closeElection() {
        this.isActive = false;
    }

    public boolean castVote(String candidateId) {
        if (!isActive) {
            System.out.println("Election is closed. Voting is not allowed.");
            return false;
        }
        for (Candidate c : candidates) {
            if (c.getCandidateId().equalsIgnoreCase(candidateId)) {
                c.incrementVote();
                return true;
            }
        }
        System.out.println("Invalid candidate ID.");
        return false;
    }

    public Candidate getWinner() {
        Candidate winner = null;
        int maxVotes = -1;
        for (Candidate c : candidates) {
            if (c.getVoteCount() > maxVotes) {
                maxVotes = c.getVoteCount();
                winner = c;
            }
        }
        return winner;
    }
}