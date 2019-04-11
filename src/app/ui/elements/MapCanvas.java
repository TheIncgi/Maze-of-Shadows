package app.ui.elements;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import app.Game;
import app.engine.Map;
import app.engine.entity.BoundingBox;
import app.engine.entity.Entity;
import app.engine.tiles.BaseTile;
import app.engine.tiles.Lighting;
import app.misc.DoublePosition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MapCanvas extends Pane{
	Map map;
	DoublePosition focus = new DoublePosition(0d, 0d);
	Canvas tiles, lighting;
	boolean smoothLighting = true;
	GaussianBlur blur;
	boolean debugLighting = true;

	private LinkedList<Entity> entities = new LinkedList<>();


	double scale = 32;
	public MapCanvas(double wid, double hei) {
		tiles = new Canvas(wid, hei);
		lighting = new Canvas(wid+scale*2, hei+scale*2);
		lighting.setTranslateX(-scale);
		lighting.setTranslateY(-scale);
		lighting.setBlendMode(BlendMode.MULTIPLY);
		blur = new GaussianBlur(scale*.65);
		blur.setInput(Game.instance().getSettings().colorAdjust);
		if(smoothLighting)
			lighting.setEffect(blur);
		this.getChildren().add(tiles);
		this.getChildren().add(lighting);
		this.setOnMouseClicked(e->{
			double tileX = (e.getX()-tiles.getWidth()/2)/scale - focus.getX();
			double tileY = (e.getY()-tiles.getHeight()/2)/scale - focus.getY();
			int tx = (int)Math.floor(tileX);
			int ty = (int)Math.floor(tileY);
			System.out.printf("Click <%d, %d> %s %s\n", tx, ty, map.getTile(tx, ty), map.getLight(tx, ty));
			if(e.getButton().equals(MouseButton.SECONDARY)) {
				System.out.println("Snapshot!");
				snapshotFrames();
			}
		});
	}

	/**
	 * Pixel width of each tile on the canvas
	 * */

	public void draw() {
		GraphicsContext g = tiles.getGraphicsContext2D();
		GraphicsContext l = lighting.getGraphicsContext2D();
		g.clearRect(0, 0, tiles.getWidth(), tiles.getHeight());
		l.clearRect(0, 0, lighting.getWidth(), lighting.getHeight());
		l.setFill(Color.BLACK);
		l.fillRect(0, 0, lighting.getWidth(), lighting.getHeight());
		Image defaultFloor = Game.instance().genericFloor;
		if(map==null) {
			g.setFill(Color.BLACK);
			g.fillRect(0, 0, tiles.getWidth(), tiles.getHeight());
		}else {
			map.calculateLighting();
			double halfHeight = tiles.getHeight()/2;
			double halfWidth  = tiles.getWidth()/2;
			int upTiles = (int) Math.ceil(halfHeight / scale) +1;
			int leftTiles = (int) Math.ceil(halfWidth / scale) +1;

			for(int y = -upTiles; y<=upTiles; y++) {
				for(int x = -leftTiles; x<=leftTiles; x++) {

					//tile phase
					int tileX = x-focus.getFloorX();
					int tileY = y-focus.getFloorY();

					BaseTile t = map.getTile(x, y);
					//System.out.printf("Drawing: <%d, %d> %s\n", tileX, tileY, t);

					double px = (x-focus.getX())*scale + halfWidth;
					double py = (y-focus.getY())*scale + halfHeight;
					if(t!=null) {
						t.getDrawable().draw(g, px, py, scale, scale);
					}else{
						g.drawImage(defaultFloor, px, py, scale, scale);
					}

					//due to the fact that the lighting canvas is larger and shifted over by -scale pixels in the x and y dir
					//the draw coords must also be shifted
					px+=scale;
					py+=scale;

					//lighting phase
					Lighting lightDat;
					if(t!=null && t.isOpaque()) {
						if(!isSmoothLighting()) {
							lightDat = map.getLight(x, y+1);
							if(lightDat!=null) {
								clipSide(px, py, scale, scale, Side.TOP);
								l.setFill(lightDat.getColor());
								l.fillRect(px, py, scale, scale);
							}

							lightDat = map.getLight(x, y-1);
							if(lightDat!=null) {
								clipSide(px, py, scale, scale, Side.BOTTOM);
								l.setFill(lightDat.getColor());
								l.fillRect(px, py, scale, scale);
							}
							lightDat = map.getLight(x+1, y);
							if(lightDat!=null) {
								clipSide(px, py, scale, scale, Side.RIGHT);
								l.setFill(lightDat.getColor());
								l.fillRect(px, py, scale, scale);
							}
							lightDat = map.getLight(x-1, y);
							if(lightDat!=null) {
								clipSide(px, py, scale, scale, Side.LEFT);
								l.setFill(lightDat.getColor());
								l.fillRect(px, py, scale, scale);
							}
							clipNone();
						}
					}else {//not opaque or null tile
						lightDat = map.getLight(tileX, tileY);
						if(lightDat!=null) {
							l.setFill(lightDat.getColor());
							l.fillRect(px, py, scale, scale);

						}
					}

				}
			}
			synchronized (entities) {
				BoundingBox box = new BoundingBox(leftTiles, -upTiles, -leftTiles, upTiles);
				for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext();) {
					Entity e = iterator.next();
					DoublePosition pos = e.getPos();
					double iw, ih;
					iw = e.getDrawable().getImage().getWidth();
					ih = e.getDrawable().getImage().getHeight();
					
					double tileX = pos.getX()-focus.getFloorX();
					double tileY = pos.getY()-focus.getFloorY();
					//System.out.printf("Drawing: <%d, %d> %s\n", tileX, tileY, t);

					double px = (pos.getX()-focus.getX())*scale + halfWidth;
					double py = (pos.getY()-focus.getY())*scale + halfHeight;
					
					e.getDrawable().draw(g, px, py, iw, ih);
				}
			}
		}
		//snapshotFrames();
	}

	public void addEntity(Entity e) {
		synchronized (entities) {
			entities.add(e);
		}
	}
	public void removeEntity(Entity e) {
		synchronized (entities) {
			entities.remove(e);
		}
	}
	public void clearEntities() {
		synchronized (entities) {
			entities.clear();
		}
	}
	
	//debug tool
	private void snapshotFrames() {
		try {
			SnapshotParameters sp = new SnapshotParameters();
			WritableImage lImg = new WritableImage((int)tiles.getWidth(), (int)tiles.getHeight());
			tiles.snapshot(sp, lImg);
			File tmp = new File("tiles.png");
			System.out.println("Writing to "+tmp.getAbsolutePath());
			ImageIO.write(SwingFXUtils.fromFXImage(lImg, null), "png", tmp);
			System.out.println(tmp.exists());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			SnapshotParameters sp = new SnapshotParameters();
			WritableImage lImg = new WritableImage((int)lighting.getWidth(), (int)lighting.getHeight());
			lighting.snapshot(sp, lImg);
			ImageIO.write(SwingFXUtils.fromFXImage(lImg, null), "png", new File("light.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSmoothLighting(boolean status) {
		if(status==smoothLighting)return;
		smoothLighting = status;
		lighting.setEffect(smoothLighting?blur:null);
	}
	public boolean isSmoothLighting() {
		return smoothLighting;
	}


	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public DoublePosition getFocus() {
		return focus;
	}
	/**
	 * Ex: setting the players position
	 *     will keep the map focused on the player
	 * */
	public void setFocus(DoublePosition focus) {
		this.focus = focus;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}

	private boolean clipping = false;
	private void clipNone() {
		if(!clipping) return;
		clipping = false;
		lighting.getGraphicsContext2D().restore();
	}
	private void clipSide(double px, double py, double w, double h, Side s) {
		GraphicsContext g= lighting.getGraphicsContext2D();

		if(clipping) clipNone();
		g.save();
		double cx = px + w/2, cy = py + h/2;
		g.beginPath();
		g.moveTo(cx, cy);
		switch (s) {
		case BOTTOM:
			g.lineTo(px, py+h);
			g.lineTo(px+w, py+h);
			break;
		case LEFT:
			g.lineTo(px, py);
			g.lineTo(px, py+h);
			break;
		case RIGHT:
			g.lineTo(px+w, py);
			g.lineTo(px+w, py+h);
			break;
		case TOP:
			g.lineTo(px, py);
			g.lineTo(px+w, py);
			break;
		default:
			break;
		}
		g.closePath();
		g.clip();
		clipping = true;
	}

	public static enum Side{
		TOP,BOTTOM, LEFT, RIGHT
	}
}
