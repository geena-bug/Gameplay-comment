package game;

import java.util.Random;

import static game.GamePlay.getShipName;

class PatrolBoat extends Ship {
  // Name of the patrol boat
  private String sName;

  // Constructor for PatrolBoat, initializing attributes based on preferred target
  public PatrolBoat(String sName, ShipType preferredTarget) {
    super(preferredTarget);
    this.sName = sName;
    // Randomly generate attack strength between 10 and 20
    this.nAttack = new Random().nextInt(11) + 10;
    // Set initial hit points, armor, and repair rate for the PatrolBoat
    this.nHitPoints = 50;
    this.nArmour = 5;
    this.nRepairRate = 5;
  }

  // Getter method to retrieve the name of the patrol boat
  public String getName() {
    return sName;
  }

  // String representation of the PatrolBoat, including its name and other attributes
  @Override
  public String toString() {
    return "Name = '" + getName() + super.toString();
  }

  // Method to handle defending against an attack from another ship
  @Override
  public void defendAttack(Ship obAttacker) {
    // Check if the PatrolBoat is not destroyed
    if (!isDestroyed()) {
      // Generate a random probability for the success of the attack
      double attackSuccessProbability = Math.random();
      // Select a target based on the preferred target probabilities
      ShipType target = sPreferredTarget[new Random().nextInt(sPreferredTarget.length)];

      // Define the attack success probability for each target type
      double targetProbability =
              switch (target) {
                case BATTLESHIP -> 0.0;
                case CARRIER -> 0.1;
                case DESTROYER -> 0.05;
                case SUBMARINE -> 0.05;
                case PATROLBOAT -> 0.8;
              };

      // Check if the attack is successful based on probabilities
      if (attackSuccessProbability > targetProbability) {
        // Calculate damage inflicted by the attacker, considering armor
        int damage = Math.max(0, obAttacker.getAttack() - nArmour);
        // Reduce PatrolBoat's hit points by the calculated damage
        nHitPoints -= damage;
        // Ensure hit points do not go below 0
        if (this.nHitPoints < 0) {
          nHitPoints = 0;
        }
        // Check if the PatrolBoat is destroyed after the attack
        if (isDestroyed()) {
          // If destroyed, record the attacker's ship name as the destroyer
          String obAttackerShipName = getShipName(obAttacker);
          setDestroyedBy(obAttackerShipName);
        }
      }
    }
  }
}
