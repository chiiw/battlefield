package targaryen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.battlefieldjava.engine.Knight;
import net.sf.battlefieldjava.engine.Location;
import net.sf.battlefieldjava.ruler.ICastle;
import net.sf.battlefieldjava.ruler.IKnight;
import net.sf.battlefieldjava.ruler.IObject;
import net.sf.battlefieldjava.ruler.IPeasant;
import net.sf.battlefieldjava.ruler.IPiece;
import net.sf.battlefieldjava.ruler.IRuler;
import net.sf.battlefieldjava.ruler.World;

public class TKnight extends Knight {
	
	private final int[] scapes = {+1, -2, +3, -4, +5, -6, +7};

	public TKnight(IRuler owner) {
		super(owner);
	}
	
	public void thinkAndDo(ICastle[] castles, IKnight[] knights, IPeasant[] peasants) {
		
		int dir = getDirectionUsingHeuristicSearch(castles, knights, peasants);
		
//		if (dir == 0) dir = new Random().nextInt(8) + 1;
		
		if (dir > 0) {
			for (int scape : scapes) {
				Location l = World.getLocationAfterMove(getLocation(), dir);
				if (l != null) {
					IObject obj = l.getOccupant();
					if (obj == null) {
						move(dir);
						return;
					} else if (obj.getRuler() != getRuler()) {
						capture(dir);
						return;
					}
				}
				dir = getNextDirection(dir, scape);
			}
		}
	}

	private int getDirectionUsingHeuristicSearch(ICastle[] castles, IKnight[] knights, IPeasant[] peasants) {
		Map<Integer, IPiece> heuristics = getHeuristics(castles, knights, peasants);
		
		if (heuristics == null || heuristics.isEmpty()) {
			return 0;
		}
		
		Integer[] keys = heuristics.keySet().toArray(new Integer[heuristics.size()]);
		Arrays.sort(keys);
		IPiece nearest = heuristics.get(keys[0]);
		return getDirectionTo(nearest.getX(), nearest.getY());
	}

	private Map<Integer, IPiece> getHeuristics(ICastle[] castles, IKnight[] knights, IPeasant[] peasants) {
		Map<Integer, IPiece> heuristics = new HashMap<>();
		
		for (IPeasant peasant : peasants) {
			if (peasant != null && peasant.isAlive()) {
				// O valor heurístico para cada camponês inimigo é o dobro de sua distância até mim
				heuristics.put(getDistanceTo(peasant.getX(), peasant.getY()) * 2, peasant);
			}
		}		
		for (ICastle castle : castles) {
			if (castle != null && castle.isAlive()) {
				// O valor heurístico para cada cavaleiro inimigo é a sua distância até mim
				heuristics.put(getDistanceTo(castle.getX(), castle.getY()), castle);
			}
		}		
		for (IKnight knight : knights) {
			if (knight != null && knight.isAlive()) {
				// O valor heurístico para cada castelo inimigo é a sua distância até mim
				heuristics.put(getDistanceTo(knight.getX(), knight.getY()), knight);
			}
		}
		
		return heuristics;
	}

	private int getNextDirection(int dir, int scape) {
		int direction = dir + scape;
		switch (direction) {
		case -7:
			return 1;
		case -6:
			return 2;
		case -5:
			return 3;
		case -4:
			return 4;
		case -3:
			return 5;
		case -2:
			return 6;
		case -1:
			return 7;
		case 0:
			return 8;
		case 9:
			return 1;
		case 10:
			return 2;
		case 11:
			return 3;
		case 12:
			return 4;
		case 13:
			return 5;
		case 14:
			return 6;
		case 15:
			return 7;
		case 16:
			return 8;
		default:
			return direction;
		}
	}
}
