import java.util.List;

public class ElectionResults {
    private Election election;
    private VoterDatabase voterDatabase;

    public ElectionResults(Election election, VoterDatabase voterDatabase) {
        this.election = election;
        this.voterDatabase = voterDatabase;
    }

    public void displayResults() {
        System.out.println("\n===== ELECTION RESULTS: " + election.getElectionName() + " =====");
        System.out.println("Total Registered Voters : " + voterDatabase.getTotalVoters());
        System.out.println("Total Votes Cast        : " + voterDatabase.getTotalVoted());
        System.out.println("\nCandidate Results:");
        System.out.println("------------------------------------------");

        List<Candidate> candidates = election.getCandidates();
        int totalVotes = voterDatabase.getTotalVoted();

        for (Candidate c : candidates) {
            double pct = totalVotes > 0
                ? (c.getVoteCount() * 100.0 / totalVotes)
                : 0.0;
            System.out.println("Name     : " + c.getName());
            System.out.println("Party    : " + c.getParty());
            System.out.println("Votes    : " + c.getVoteCount());
            System.out.println("Percent  : " + String.format("%.1f", pct) + "%");
            System.out.println("------------------------------------------");
                   }

        System.out.println("------------------------------------------");
        Candidate winner = election.getWinner();
        if (winner != null && winner.getVoteCount() > 0) {
            System.out.println("WINNER: " + winner.getName() + " (" + winner.getParty() + ")");
        } else {
            System.out.println("No votes cast yet.");
        }
        System.out.println("==========================================\n");
    }
}