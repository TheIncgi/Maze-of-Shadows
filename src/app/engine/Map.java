package app.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import app.engine.tiles.BaseTile;
import app.engine.tiles.Emissive;
import app.engine.tiles.Lighting;
import app.misc.IntegerPosition;

public class Map {

	HashMap<IntegerPosition, BaseTile> tiles = new HashMap<>(1024);
	HashMap<IntegerPosition, Lighting> lighting = new HashMap<>(1024);
	ArrayList<Emissive> lightEmitters = new ArrayList<>();
	public double minLightLevel = .04;

	public static double lightFactor(double height, double pathDistance) {
		double angleOfImpact = Math.atan2(pathDistance, height);
		return Math.cos(angleOfImpact);
	}


	//Made this to calculate
	//https://www.desmos.com/calculator/ld2gzlwiyi
	public void calculateLighting(Emissive playerEmissive) {
		for (int i = 0; i < lightEmitters.size(); i++) {
			Emissive e = lightEmitters.get(i);
			solveLightForEmissive(e);
		}
		if(playerEmissive!=null)
			solveLightForEmissive(playerEmissive);
	}
	
	private void solveLightForEmissive(Emissive e) {
		//using the inverse of the function from light factor
		//determine the maximum number of tiles away that need to be calculated
		double maxDistance = Math.tan(Math.acos(minLightLevel / e.lightBrightness()))*e.lightHeight();
		Stack<IntegerPosition> toCalculate = new Stack<>();
		Stack<IntegerPosition> completed = new Stack<>();
		HashMap<IntegerPosition, Boolean> added = new HashMap<>();
		IntegerPosition start = e.getSource().floorToIntPos();
		toCalculate.push( start );
		added.put(start, true);
		for(int pathDist = 0; pathDist<=maxDistance; pathDist++) {
			while(!toCalculate.isEmpty()) {
				calculateLighting(e, pathDist, completed.push(toCalculate.pop()));
			}
			if(pathDist!=maxDistance)
				while(!completed.isEmpty()) {
					IntegerPosition pos = completed.pop();
					IntegerPosition tmp;
					
					tmp = pos.add( 0,  1);
					if(!added.getOrDefault(tmp, false) && usesLighting(tmp)) added.put(toCalculate.push(tmp), true);

					tmp = pos.add( 0, -1);
					if(!added.getOrDefault(tmp, false) && usesLighting(tmp)) added.put(toCalculate.push(tmp), true);

					tmp = pos.add( 1, 0);
					if(!added.getOrDefault(tmp, false) && usesLighting(tmp)) added.put(toCalculate.push(tmp), true);

					tmp = pos.add(-1, 0);
					if(!added.getOrDefault(tmp, false) && usesLighting(tmp)) added.put(toCalculate.push(tmp), true);
				}
		}
	}
	
	private Function<IntegerPosition, Lighting> computeNewLighting = new Function<IntegerPosition, Lighting>() {
		public Lighting apply(IntegerPosition t) {
			return new Lighting();
		}
	};
	private void calculateLighting(Emissive e, double pathLen, IntegerPosition pos) {
		BaseTile tile = getTile(pos);
		if(tile!=null && tile.isOpaque()) return;

		double factor = lightFactor(e.lightHeight(), pathLen);
		factor *= e.lightBrightness();

		Lighting lightData = lighting.computeIfAbsent(pos, computeNewLighting);
		lightData.add(e.getLightColor(), factor);
	}

	
	public void setTile(BaseTile tile, int x, int y) {
		if(tile==null) throw new NullPointerException("Attempt to set tile to null");
		IntegerPosition pos = new IntegerPosition(x, y);
		List<Emissive> emissives = tile.getEmissives(pos);
		if(emissives!=null) {
			for (Emissive emissive : emissives) {
				lightEmitters.add(emissive);
			}
		}
		tiles.put(pos, tile);
		//System.out.println("Tile set at "+pos);
	}
	
	public BaseTile getTile(int x, int y) {
		IntegerPosition pos = new IntegerPosition(x, y);
		return getTile(pos);
	}
	public BaseTile getTile(IntegerPosition pos) {
		return tiles.get(pos);
	}
	public Lighting getLight(int x, int y) {
		return getLight(new IntegerPosition(x, y));
	}
	public Lighting getLight(IntegerPosition pos ) {
		return lighting.get(pos);
	}
	/**
	 * Check if lighting needs to be calculated for some tile space
	 * */
	public boolean usesLighting(IntegerPosition pos) {
		return getTile(pos) == null || !getTile(pos).isOpaque();
	}
}
