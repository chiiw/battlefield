package targaryen;

import java.awt.Color;

import net.sf.battlefieldjava.ruler.ICastle;
import net.sf.battlefieldjava.ruler.IKnight;
import net.sf.battlefieldjava.ruler.IPeasant;
import net.sf.battlefieldjava.ruler.Ruler;
import net.sf.battlefieldjava.ruler.World;

public class TRuler extends Ruler {
	
	private final String RULER_NAME = "Targaryen";
	private final String SCHOOL_NAME = "Nathaniel";

	public TRuler(World world, Color color) {
		super(world, color);
	}
	
	@Override
	public IKnight createKnight() {
		return new TKnight(this);
	}
	
	@Override
	public IPeasant createPeasant() {
		return new TPeasant(this);
	}

	@Override
	public String getRulerName() {
		return RULER_NAME;
	}

	@Override
	public String getSchoolName() {
		return SCHOOL_NAME;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void orderSubjects(int arg0) {
		
		ICastle[] ec = World.getOtherCastles();
		IKnight[] ek = World.getOtherKnights();
		IPeasant[] ep = World.getOtherPeasants();
		
		for (IKnight knight : getKnights()) {
			((TKnight) knight).thinkAndDo(ec, ek, ep);
		}
		
		for (IPeasant peasant : getPeasants()) {
			((TPeasant) peasant).thinkAndDo();
		}
	}

}
