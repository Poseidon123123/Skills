package poseidon.skills;

public enum KillXP {
    //TODO add All Hostile Mobs
    Zombie(10);

    private final int xp;

    KillXP(int ex){
        this.xp = ex;
    }

    public int getXp() {
        return xp;
    }
}
