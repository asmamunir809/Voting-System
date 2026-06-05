import java.util.List;
import java.util.Scanner;

public class VotingBooth {
    private Election election;
    private VoterDatabase voterDatabase;
    private Scanner scanner;

    public VotingBooth(Election election, VoterDatabase voterDatabase) {
        this.election = election;
        this.voterDatabase = voterDatabase;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.print("Enter Voter ID: ");
        String voterId = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        Voter voter = voterDatabase.authenticate(voterId, password);

        if (voter == null) {
            System.out.println("Authentication failed. Invalid ID or password.");
            return;
        }

        if (voter.hasVoted()) {
            System.out.println("You have already voted, " + voter.getName() + ".");
            return;
        }

        if (!election.isActive()) {
            System.out.println("The election is closed.");
            return;
        }

        System.out.println("\nWelcome, " + voter.getName() + "!");
        System.out.println("Election: " + election.getElectionName());
        System.out.println("\nCandidates:");

        List<Candidate> candidates = election.getCandidates();
        for (Candidate c : candidates) {
            System.out.println("  [" + c.getCandidateId() + "] "
                + c.getName() + " - " + c.getParty());
        }

        System.out.print("\nEnter Candidate ID to vote: ");
        String choice = scanner.nextLine().trim();

        boolean success = election.castVote(choice);
        if (success) {
            voterDatabase.markVoted(voterId);
            System.out.println("Vote cast successfully! Thank you, " + voter.getName() + ".");
        } else {
            System.out.println("Vote was not cast due to an error.");
        }
    }
}