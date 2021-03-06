package tk.astris.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import tk.astris.entity.Entity;
import tk.astris.entity.ZombieType;
import tk.astris.levels.Level;
import tk.astris.physics.collisions.Collision;
import tk.astris.player.Player;
import tk.astris.tile.Tile;

@SuppressWarnings("serial")
public class Frame extends JFrame{
	
	paint zeichnen;
	Player player;
	
	ArrayList<Entity> entitys = new ArrayList<Entity>();
	ArrayList<ZombieType> zombies2 = new ArrayList<ZombieType>();
	public ArrayList<Tile> tiles = new ArrayList<Tile>();
	
	boolean loadFirst = true;
	
	int tileSize = 50;

	Level lvl;
	
	boolean debug = true;
	
	public Frame(Player p, Level level){
		super("ZombieApocalypse ALPHA V." + Main.VERSION);		
		
		lvl = level;
		player = p;
		entitys.add(player);
		zeichnen = new paint();
		
		zeichnen.setBounds(0, 0, Main.WIDTH, Main.HEIGHT);
		setSize(Main.WIDTH, Main.HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		addKeyListener(new keyCheck());
		add(zeichnen);
	}
	
	public void repaintScreen(){
		zeichnen.repaint();
	}
	
	public void update() {
		
		
		if(keyCheck.keysCheck(KeyEvent.VK_W)){
			Main.camera.y = Main.camera.y + player.speed;
			player.trueY = player.trueY - player.speed;
			onMove();
		}
		if(keyCheck.keysCheck(KeyEvent.VK_S)){
			Main.camera.y = Main.camera.y - player.speed;
			player.trueY = player.trueY + player.speed;
			onMove();
		}
		if(keyCheck.keysCheck(KeyEvent.VK_A)){
			Main.camera.x = Main.camera.x + player.speed;
			player.trueX = player.trueX - player.speed;
			onMove();
		}
		if(keyCheck.keysCheck(KeyEvent.VK_D)){
			Main.camera.x = Main.camera.x - player.speed;
			player.trueX = player.trueX + player.speed;
			onMove();
		}
		
		for(int i = 0; i < entitys.size(); i++){
			Entity entity = entitys.get(i);
			entity.update();
		}
		
		for(int i = 0; i < zombies2.size(); i++){
			
			ZombieType zt = zombies2.get(i);
			
			if(!zt.isDead){
			
			zt.update();
			if(Collision.testIntersection(zt.hearRaduis, player.hearRaduis)){
				zt.move();
				zt.canHear = true;
			}else{
				zt.canHear = false;
			}
			
			if(zt.bounding.intersects(player.bounding)){
				player.hp = zt.zombie.damage;
				player.illness = Main.randInt(0, zt.zombie.posionLevel);
			}
			
			}else{
				zombies2.remove(i);
			}
		}

		/*if(keyCheck.keysCheck(KeyEvent.VK_W)){
			for(int i = 0; i < tiles.size(); i++){
				Tile tile = tiles.get(i);
				tile.y = tile.y + player.speed;
			}
			onMove();
		}
		if(keyCheck.keysCheck(KeyEvent.VK_S)){
			for(int i = 0; i < tiles.size(); i++){
				Tile tile = tiles.get(i);
				tile.y = tile.y - player.speed;
			}
			onMove();
		}
		if(keyCheck.keysCheck(KeyEvent.VK_A)){
			for(int i = 0; i < tiles.size(); i++){
				Tile tile = tiles.get(i);
				tile.x = tile.x + player.speed;
			}
			onMove();
		}
		if(keyCheck.keysCheck(KeyEvent.VK_D)){
			for(int i = 0; i < tiles.size(); i++){
				Tile tile = tiles.get(i);
				tile.x = tile.x - player.speed;
			}
			onMove();
		}*/
	}

	
	public void onMove(){
		
	}
	
	

	private class paint extends JLabel{
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D) g;
			
			/*if(loadFirst){			
				for(int x = 0; x < Main.WIDTH; x = x + tileSize){
					for(int y = 0; y < Main.HEIGHT; y = y + tileSize){
				       tiles.add(new Tile(x, y, Main.images.getSpriteSheet().getSubimage(0, 0, tileSize, tileSize)));
					}
				}
				loadFirst = false;
			}
						
			for(int i = 0; i < tiles.size(); i++){
				Tile tile = tiles.get(i);
				g2d.drawImage(tile.image, (int)tile.x, (int)tile.y, null);
			}*/
			

			for(int x = 0; x < lvl.size.x; x++){
				for(int y = 0; y < lvl.size.y; y++){
				    Tile tile0 = Main.tiles.tiles.get(lvl.tiles[x][y]);
					g2d.drawImage(tile0.image, (int)(y*tile0.size.y + Main.camera.x), (int)(x*tile0.size.x + Main.camera.y),  null);
				}
			}
			
			if(player.revertTexture){
				g2d.drawImage(player.image, (int)player.x + player.size.x, (int)player.y, -player.size.x, player.size.y, null);
			}else{
			g2d.drawImage(player.image, (int)player.x, (int)player.y, null);
			}

			
			/*for(int i = 0; i < zombies.size(); i++){
				Zombie z = zombies.get(i);
				System.out.println(zombies.size() + " : " + z.x + " : " + z.y);
				g2d.drawImage(z.walkBack[0], (int)(z.x + Main.camera.x), (int)(z.y + Main.camera.y), null);
			}*/
			
			for(ZombieType zt : zombies2){
				if(!zt.revertTexture){
					g2d.drawImage(zt.image, (int)(zt.x + Main.camera.x), (int)(zt.y + Main.camera.y), null);

				}else{
					g2d.drawImage(zt.image, (int)(zt.x + Main.camera.x) + zt.zombie.size.x, (int)(zt.y + Main.camera.y), -zt.zombie.size.x, zt.zombie.size.y,  null);
				}
				
				if(debug){
					g2d.draw(zt.hearRaduis);
					g2d.draw(zt.bounding);
				}
			}
			
			if(debug){
				//g2d.drawRect((int)(player.trueX + Main.camera.x), (int)(player.trueY + Main.camera.y), player.size.x, player.size.y);
				g2d.draw(player.bounding);
				g2d.draw(player.hearRaduis);
			}

			int rdm = Main.randInt(0, 100);
			if(rdm == 100){
				zombies2.add(new ZombieType(Main.zombies.zombie.get("0"), player.x + Main.randInt(Main.WIDTH, Main.WIDTH + 500), player.y + Main.randInt(Main.HEIGHT, Main.HEIGHT + 500)));
			}
			if(rdm == 200){
				zombies2.add(new ZombieType(Main.zombies.zombie.get("0"), -player.x - Main.randInt(Main.WIDTH, Main.WIDTH + 500), player.y + Main.randInt(Main.HEIGHT, Main.HEIGHT + 500)));
			}
			if(rdm == 300){
				zombies2.add(new ZombieType(Main.zombies.zombie.get("0"), player.x + Main.randInt(Main.WIDTH, Main.WIDTH + 500), -player.y - Main.randInt(Main.HEIGHT, Main.HEIGHT + 500)));
			}
			if(rdm == 400){
				zombies2.add(new ZombieType(Main.zombies.zombie.get("0"), -player.x - Main.randInt(Main.WIDTH, Main.WIDTH + 500), -player.y - Main.randInt(Main.HEIGHT, Main.HEIGHT + 500)));
			}
		}
	}	
}

