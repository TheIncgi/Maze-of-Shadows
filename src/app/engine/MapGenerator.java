package app.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import app.engine.tiles.BaseTile;
import app.engine.tiles.Emissive;
import app.engine.tiles.GoalTile;
import app.engine.tiles.GoldOnTheFloorTile;
import app.misc.DoublePosition;
import app.misc.IntegerPosition;
import app.misc.Utils;
import app.ui.elements.BaseDrawable;
import app.ui.elements.IDrawable;
import javafx.scene.paint.Color;

public class MapGenerator {
	private int size;
	public static final double GOLD_CHANCE = 1/50d;
	public static final double MONSTER_CHANCE = 1/20d; 

	public MapGenerator() {
	}

	public Map generate() {
		Map map = new Map();
		map.setTile(quickTile(Color.RED),-3,-3);
		map.setTile(quickTile(Color.GREEN),3,-3);
		map.setTile(quickTile(Color.BLUE),-0,2);
		for(int x = -25; x<=20; x++)
			map.setTile(quickTile(), x, 3);
		for(int x = -5; x<=20; x++)
			map.setTile(quickTile(), x, 4);
		for(int x = -15; x<=20; x++)
			map.setTile(quickTile(), x*2, -4);

		return map;
	}
	Long seed;
	public Map generate(int level) {
		size = level*2+20;
		System.out.println("Generating map of size "+size);
		int wallThickness = 2; //corridor length
		int corridorThickness = 2;// must be <= to wall thickness
		
		
		corridorThickness--;
		Random random = seed!=null? new Random(seed) : new Random(); //random seed configurable
		BaseTile wall = quickTile();
		//BaseTile exit = quickTile(Color.GREEN);
		System.out.println("Tiles for map aquired");

		Map map = new Map();
		int boundLeft = 0, boundRight = 0, boundUp = 0, boundDown = 0;
		ArrayList<IntegerPosition> open = new ArrayList<>();
		HashMap<IntegerPosition, Boolean> closed = new HashMap<>();
		IntegerPosition origin = new IntegerPosition(0, 0);
		open.add(origin);
		IntegerPosition up, down, left, right;
		ArrayList<IntegerPosition> tmp = new ArrayList<>(); //used to hold local options
		//ArrayList<IntegerPosition> justOpened = new ArrayList<>();
		System.out.println("Generating....");
		for(int i = 0; i<size; i++) {
			System.out.println("  "+(i+1)+" of "+size);
			int numOpen = open.size(); //changed inside loop
			//justOpened.clear();
			for(int j = 0; j<numOpen; j++) {
				IntegerPosition pos = open.get(random.nextInt(open.size())); //choose random pos
				tmp.clear();
				up =  pos.add( 0, -(wallThickness+1));
				down = pos.add( 0,  wallThickness+1);
				left = pos.add(-(wallThickness+1),  0);
				right = pos.add( wallThickness+1,  0);

				//add only open options
				if( !closed.containsKey(up) )
					tmp.add(up );
				if( !closed.containsKey(down))
					tmp.add( down );
				if( !closed.containsKey(left))
					tmp.add( left );
				if( !closed.containsKey(right))
					tmp.add( right );

				//no other tiles may branch to this one
				closed.putIfAbsent(pos, true);
				//if all options are exausted remove from list of branchingpoints
				if(tmp.size()<=2) { //0 if closed from another path
					open.remove(pos);
				}
				if(tmp.size() >= 2){

					IntegerPosition choice = tmp.get(random.nextInt( tmp.size() )); //choose random option
					int dx = (int) Math.signum(choice.getX()-pos.getX()), dy = (int) Math.signum(choice.getY()-pos.getY());

					//close the path to the next open
					for(int m = 1; m<=wallThickness; m++) {
						closed.put(pos.add(dx*m, dy*m), true);
					}
					//mark last pos in path as open
					open.add(pos = pos.add(dx*(wallThickness+1), dy*(wallThickness+1)));
					//update the map bounds
					boundLeft = Math.min(boundLeft, pos.getX());
					boundRight = Math.max(boundRight, pos.getX());
					boundUp = Math.min(boundUp, pos.getY());
					boundDown = Math.max(boundDown, pos.getY());
				}
			}
//			while(!justOpened.isEmpty())
//				open.add(justOpened.remove(justOpened.size()-1));
		}
		//remove remaining open to closed
//		while(!open.isEmpty()) {
//			closed.put(open.remove(open.size()-1), true);
//		}
		IntegerPosition goalPos = open.get(random.nextInt(open.size()));
		System.out.println("Goal:"+goalPos);
		map.goalPos = goalPos;
		map.setTile(new GoalTile(), goalPos.getX(), goalPos.getY());

		IntegerPosition tmpPos = new IntegerPosition(0, 0), tmp2;  //FIXME this code removes extra tiles to make the halways wider, needs fixing
		for(int i = 0; i<corridorThickness; i++) {
			for(int y = boundUp-1; y<=boundDown+1; y++) {
				for(int x = boundLeft-1; x<=boundRight+1; x++) {
					tmpPos.set(x, y);
					if(!closed.containsKey(tmpPos)) continue; //closed is part of the path
					
					tmp2 = tmpPos.add(0, -1);
					if(closed.containsKey(tmp2))
						closed.put(tmp2, true);
					tmp2 = tmpPos.add(-1, 0);
					if(closed.containsKey(tmp2))
						closed.put(tmp2, true);
				}
			}
		}
		
		
		
		System.out.println("Filling map data....");
		for(int y = boundUp-10; y<=boundDown+10+corridorThickness; y++) {
			for(int x = boundLeft-10; x<=boundRight+10+corridorThickness; x++) {
				tmpPos.set(x, y);
				if(!closed.containsKey(tmpPos))
					map.setTile(wall, x, y);
				else if(map.getTile(x, y)==null){ //SPECIAL TILE SECTION
					if(random.nextDouble() < GOLD_CHANCE) {
						map.setTile(new GoldOnTheFloorTile(x,y), x, y);
					}
					if(random.nextDouble() < MONSTER_CHANCE && Utils.distance(x, y, 0, 0) > 12) {
						map.monsterSpawns.add(new IntegerPosition(x, y));
					}
				}
			}
		}
		System.out.println("Beware of "+map.monsterSpawns.size()+" monsters!");
		System.out.println("Adding origin light");
		map.lightEmitters.add(new Emissive() {
			DoublePosition pos = new DoublePosition(.5, .5);
			@Override
			public DoublePosition getSource() {
				return pos;
			}
			@Override
			public Color getLightColor() {
				return super.getLightColor();
			}
			@Override
			public double brightness() {
				return 3-level/2;
			}
		});
		map.setLeftBound(boundLeft);
		map.setRightBound(boundRight);
		map.setUpperBound(boundUp);
		map.setLowerBound(boundDown);
		System.out.println("Map genration complete");
		return map;
	}

	public void setSeed(int seed ) {
		setSeed(Long.valueOf(seed));
	}
	public void setSeed(Long seed) {
		this.seed = seed;
	}
	
	public BaseTile quickTile(Color color) {
		return new BaseTile() {


			@Override
			public boolean isPassable() {
				return true;
			}

			@Override
			public boolean isOpaque() {
				return false;
			}

			@Override
			public IDrawable getDrawable() {
				return new BaseDrawable() {
				};
			}
			@Override
			public List<Emissive> getEmissives(final IntegerPosition tilePos) {
				List<Emissive> e = new ArrayList<>();
				e.add(new Emissive() {
					@Override
					public DoublePosition getSource() {
						return tilePos.add(.5, .5);
					}
					@Override
					public Color getLightColor() {
						return color;
					}
				});
				return e;
			}
		};
	}
	
	private static final BaseDrawable wallDrawable = new BaseDrawable() {
		public String getResourceName() {return "wall.png";};
	};
	public BaseTile quickTile() {
		return new BaseTile() {


			@Override
			public boolean isPassable() {
				return false;
			}

			@Override
			public boolean isOpaque() {
				return true;
			}

			@Override
			public IDrawable getDrawable() {
				return wallDrawable;
			}

		};
	}
	
}
