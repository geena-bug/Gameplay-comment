package game;


// Abstract class representing a ship in a game
abstract class Ship {
    // Attributes common to all ships
    protected int nAttack;
    protected int nRepairRate;
    protected int nArmour;
    protected int nHitPoints;
    protected ShipType[] sPreferredTarget;
    protected String destroyedBy;

    // Constructor for a ship with specified preferred target, initial hit points, and initial armor
    Ship(ShipType sPreferredTarget, int initialHitPoints, int initialArmor) {
        this.sPreferredTarget = new ShipType[]{sPreferredTarget};
        this.destroyedBy = null;
        this.nHitPoints = initialHitPoints;
        this.nArmour = initialArmor;
    }

    // Constructor for a ship with a specified preferred target (defaulting hit points and armor to 0)
    Ship(ShipType sPreferredTarget) {
        this(sPreferredTarget, 0, 0);
    }

    // Getter method for the attack strength of the ship
    public int getAttack() {
        return nAttack;
    }

    // Getter method for the armor of the ship
    public int getArmour() {
        return nArmour;
    }

    // Checks if the ship is destroyed (hit points less than or equal to 0)
    public boolean isDestroyed() {
        return nHitPoints <= 0;
    }

    // Sets the ship as destroyed by a specified ship name
    public void setDestroyedBy(String shipName) {
        destroyedBy = shipName;
    }

    // Repairs the ship's hit points based on its type
    public void repair() {
        if (nHitPoints > 0) {
            nHitPoints += nRepairRate;
        }
        // Different types of ships have different maximum hit points after repair
        switch (this.getClass().getSimpleName()) {
            case "BattleShip" -> nHitPoints = Math.min(nHitPoints, 300);
            case "Carrier" -> nHitPoints = Math.min(nHitPoints, 500);
            case "PatrolBoat", "Destroyer" -> nHitPoints = Math.min(nHitPoints, 100);
            case "Submarine" -> nHitPoints = Math.min(nHitPoints, 50);
            default -> throw new IllegalStateException("Unexpected value: " + this.getClass().getSimpleName());
        }
    }

    // String representation of the ship, displaying its attributes
    @Override
    public String toString() {
        return " has an attack strength of " + nAttack + " health bar of " + nHitPoints +
               ", armour of " + nArmour + " can be repaired at " + nRepairRate + "HP " +
               "and prefers attacking " + sPreferredTarget[0];
    }

    // Abstract method to be implemented by subclasses for defending against an attack
    public abstract void defendAttack(Ship obAttacker);
}

