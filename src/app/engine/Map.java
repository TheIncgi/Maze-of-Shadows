package app.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import app.engine.tiles.BaseTile;
import app.engine.tiles.Emissive;
import app.engine.tiles.Lighting;
import app.misc.Position;

public class Map {

	HashMap<Position<Integer>, BaseTile> tiles = new HashMap<>(1024);
	HashMap<Position<Integer>, Lighting> lighting = new HashMap<>(1024);
	ArrayList<Emissive> lightEmitters = new ArrayList<>();
	public double minLightLevel = .02;

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
		Stack<Position<Integer>> toCalculate = new Stack<>();
		Stack<Position<Integer>> completed = new Stack<>();
		HashMap<Position<Integer>, Boolean> added = new HashMap<>();
		Position<Integer> start = e.getSource().floorToIntPos();
		toCalculate.push( start );
		added.put(start, true);
		for(int pathDist = 0; pathDist<=maxDistance; pathDist++) {
			while(!toCalculate.isEmpty()) {
				calculateLighting(e, pathDist, completed.push(toCalculate.pop()));
			}
			if(pathDist!=maxDistance)
				while(!completed.isEmpty()) {
					Position<Integer> pos = completed.pop();
					Position<Integer> tmp;
					
					tmp = pos.iAdd( 0,  1);
					if(!added.getOrDefault(tmp, false) && usesLighting(tmp)) added.put(toCalculate.push(tmp), true);

					tmp = pos.iAdd( 0, -1);
					if(!added.getOrDefault(tmp, false) && usesLighting(tmp)) added.put(toCalculate.push(tmp), true);

					tmp = pos.iAdd( 1, 0);
					if(!added.getOrDefault(tmp, false) && usesLighting(tmp)) added.put(toCalculate.push(tmp), true);

					tmp = pos.iAdd(-1, 0);
					if(!added.getOrDefault(tmp, false) && usesLighting(tmp)) added.put(toCalculate.push(tmp), true);
				}
		}
	}
	
	private Function<Position<Integer>, Lighting> computeNewLighting = new Function<Position<Integer>, Lighting>() {
		public Lighting apply(Position<Integer> t) {
			return new Lighting();
		}
	};
	private void calculateLighting(Emissive e, double pathLen, Position<Integer> pos) {
		BaseTile tile = getTile(pos);
		if(tile!=null && tile.isOpaque()) return;

		double factor = lightFactor(e.lightHeight(), pathLen);
		factor *= e.lightBrightness();

		Lighting lightData = lighting.computeIfAbsent(pos, computeNewLighting);
		lightData.add(e.getLightColor(), factor);
	}

	
	public void setTile(BaseTile tile, int x, int y) {
		if(tile==null) throw new NullPointerException("Attempt to set tile to null");
		Position<Integer> pos = new Position<Integer>(x, y);
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
		Position<Integer> pos = new Position<Integer>(x, y);
		return getTile(pos);
	}
	public BaseTile getTile(Position<Integer> pos) {
		return tiles.get(pos);
	}
	public Lighting getLight(int x, int y) {
		return getLight(new Position<Integer>(x, y));
	}
	public Lighting getLight(Position<Integer> pos ) {
		return lighting.get(pos);
	}
	/**
	 * Check if lighting needs to be calculated for some tile space
	 * */
	public boolean usesLighting(Position<Integer> pos) {
		return getTile(pos) == null || !getTile(pos).isOpaque();
	}
}
