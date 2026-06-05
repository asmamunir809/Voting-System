
public class Candidate {
    private String candidateId;
    private String name;
    private String party;
    private int voteCount;

    public Candidate(String candidateId, String name, String party) {
        this.candidateId = candidateId;
        this.name = name;
        this.party = party;
        this.voteCount = 0;
    }

    public String getCandidateId() { return candidateId; }
    public String getName()        { return name; }
    public String getParty()       { return party; }
    public int getVoteCount()      { return voteCount; }
    public void incrementVote()    { voteCount++; }

    @Override
    public String toString() {
        return candidateId + ". " + name + " (" + party + ") - Votes: " + voteCount;
    }
}