public class Voter {
    private String voterId;
    private String name;
    private String password;
    private boolean hasVoted;

    public Voter(String voterId, String name, String password) {
        this.voterId = voterId;
        this.name = name;
        this.password = password;
        this.hasVoted = false;
    }

    public String getVoterId() { return voterId; }
    public String getName()    { return name; }
    public String getPassword(){ return password; }
    public boolean hasVoted()  { return hasVoted; }
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }

    @Override
    public String toString() {
        return "Voter[ID=" + voterId + ", Name=" + name + ", Voted=" + hasVoted + "]";
    }
}