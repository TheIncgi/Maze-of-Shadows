package app.engine;

import java.util.ArrayList;
import java.util.List;

import app.engine.tiles.BaseTile;
import app.engine.tiles.Emissive;
import app.misc.DoublePosition;
import app.misc.IntegerPosition;
import app.ui.elements.BaseDrawable;
import app.ui.elements.IDrawable;
import javafx.scene.paint.Color;

public class MapGenerator {
	private int size;

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
//		map.setTile(new BaseTile() {
//			
//			@Override
//			public boolean isPassable() {
//				return true;
//			}
//			
//			@Override
//			public boolean isOpaque() {
//				return false;
//			}
//			
//			@Override
//			public IDrawable getDrawable() {
//				return new BaseDrawable() {
//				};
//			}
//			@Override
//			public List<Emissive> getEmissives(final Position<Integer> tilePos) {
//				List<Emissive> e = new ArrayList<>();
//				e.add(new Emissive() {
//					@Override
//					public Position<Double> getSource() {
//						return tilePos.dAdd(.5, .5);
//					}
//					@Override
//					public Color getLightColor() {
//						return Color.WHITE;
//					}
//					@Override
//					public double lightBrightness() {
//						return super.lightBrightness()/3;
//					}
//					@Override
//					public double lightHeight() {
//						return super.lightHeight()*3;
//					}
//				});
//				return e;
//			}
//		}, -14, -14);
		return map;
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
					return new BaseDrawable() {
					};
				}
				
			};
	}
}
