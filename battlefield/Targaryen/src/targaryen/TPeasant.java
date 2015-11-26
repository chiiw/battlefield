package targaryen;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.sf.battlefieldjava.engine.Location;
import net.sf.battlefieldjava.engine.Peasant;
import net.sf.battlefieldjava.ruler.IRuler;
import net.sf.battlefieldjava.ruler.World;

public class TPeasant extends Peasant {
	
	private static final Set<Integer> POSSIBLE_DIRECTIONS = new HashSet<>();
	static {
		for (int i = 1; i <= 8; i++) {
			POSSIBLE_DIRECTIONS.add(i);
		}
	}
	
	private Set<Location> history;
	private Random random;

	public TPeasant(IRuler owner) {
		super(owner);
		history = new HashSet<>();
		random = new Random();
	}
	
	public void thinkAndDo() {
		Set<Integer> directions = new HashSet<>(POSSIBLE_DIRECTIONS);
		int dir = getDirectionUsingDecisionTree(directions);
		if (dir > 0){
			history.add(getLocation());
			move(dir);
		}
	}

	private int getDirectionUsingDecisionTree(Set<Integer> directions) {
		
		directions = verifyFreeMovimentsAround(directions);
		if (directions.isEmpty()) {
			
			return 0;
			
		} else {
			
			Set<Integer> directionsAux = getDirectionsToEnemyLand(directions);	
			if (directionsAux.isEmpty()) {
				
				directionsAux = getDirectionsToFreeLand(directions);
				if (directionsAux.isEmpty()) {
					
					directionsAux = getDirectionsNotVisited(directions);
					if (directionsAux.isEmpty()) {

						return getRandomDir(directions);
						
					} else {
						return getRandomDir(directionsAux);
						
					}
				} else {
					return getRandomDir(directionsAux);
					
				}
			} else {
				return getRandomDir(directionsAux);
				
			}
		}
	}
	
	private Set<Integer> verifyFreeMovimentsAround(Set<Integer> directions) {
		Set<Integer> result = new HashSet<>();
		for (Integer i : directions) {
			Location l = World.getLocationAfterMove(getLocation(), i);
			if (l != null && l.getOccupant() == null) {
				result.add(i);
			}
		}
		return result;
	}

	private Set<Integer> getDirectionsToEnemyLand(Set<Integer> directions) {
		Set<Integer> result = new HashSet<>();
		for (Integer i : directions) {
			Location l = World.getLocationAfterMove(getLocation(), i);
			if (l.getOwner() == null || l.getOwner() == getRuler()) {
			} else {
				result.add(i);
			}
		}
		return result;
	}
	
	private Set<Integer> getDirectionsToFreeLand(Set<Integer> directions) {
		Set<Integer> result = new HashSet<>();
		for (Integer i : directions) {
			Location l = World.getLocationAfterMove(getLocation(), i);
			if (l.getOwner() == null) {
				result.add(i);
			}
		}
		return result;
	}

	private Set<Integer> getDirectionsNotVisited(Set<Integer> directions) {
		Set<Integer> result = new HashSet<>();
		for (Integer i : directions) {
			Location l = World.getLocationAfterMove(getLocation(), i);
			if (!history.contains(l)) {
				result.add(i);
			}
		}
		return result;
	}
	
	private int getRandomDir(Set<Integer> directions) {
		int i = random.nextInt(directions.size());
		return directions.toArray(new Integer[directions.size()])[i];
	}

}
