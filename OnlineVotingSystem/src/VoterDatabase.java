import java.util.HashMap;
import java.util.Map;

public class VoterDatabase {
    private Map<String, Voter> voters;

    public VoterDatabase() {
        voters = new HashMap<>();
    }

    public boolean registerVoter(Voter voter) {
        if (voters.containsKey(voter.getVoterId())) {
            System.out.println("Voter ID already registered.");
            return false;
        }
        voters.put(voter.getVoterId(), voter);
        System.out.println("Voter registered: " + voter.getName());
        return true;
    }

    public Voter authenticate(String voterId, String password) {
        Voter voter = voters.get(voterId);
        if (voter != null && voter.getPassword().equals(password)) {
            return voter;
        }
        return null;
    }

    public boolean hasVoted(String voterId) {
        Voter voter = voters.get(voterId);
        return voter != null && voter.hasVoted();
    }

    public void markVoted(String voterId) {
        Voter voter = voters.get(voterId);
        if (voter != null) {
            voter.setHasVoted(true);
        }
    }

    public int getTotalVoters() {
        return voters.size();
    }

    public int getTotalVoted() {
        int count = 0;
        for (Voter v : voters.values()) {
            if (v.hasVoted()) count++;
        }
        return count;
    }
}