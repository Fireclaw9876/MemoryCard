package SpringFinal2023;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.color.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class MemoryCard {
	private Image frontImage, backImage, correctImage;
	String frontImgName, backImgName, correctImgName;
	private int xLocation, yLocation, width, height;
	private boolean isFlipped, isMatched;
	
	//constructor with location, img, and width and height as parameters
	public MemoryCard(int x, int y, int w, int h, String fName, String bName, String cName) {
		frontImgName = fName;
		backImgName = bName;		
		correctImgName = cName;
		xLocation = x;
		yLocation = y;
		width = w;
		height = h;
		
		this.isFlipped = false;
		this.isMatched = false;
		
		// TODO Auto-generated constructor stub
		try {
			//screenshot photos again to redo
			frontImage = ImageIO.read(new File(frontImgName)).getScaledInstance(w, h, Image.SCALE_SMOOTH);
			
			backImage = ImageIO.read(new File(backImgName)).getScaledInstance(w, h, Image.SCALE_SMOOTH);
			
			correctImage = ImageIO.read(new File(correctImgName)).getScaledInstance(w, h, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.out.println("Image file not found.");
		}
	}
	
	// if mouse touches the image rectangle that we just built
    public boolean contains(int mouseX, int mouseY) {
        return new Rectangle(xLocation, yLocation, width, height).contains(mouseX, mouseY);
    }

    public Image getImage() {
        return frontImage;
    }

    public boolean isFlipped() {
        return isFlipped;
    }
    
    public boolean isMatched() {
        return isMatched;
    }
    
    public String getFrontImgName() {
    	return frontImgName;
    }

    // match to then change to back image
    public void setMatched(boolean matched) {
        isMatched = matched;
    }
    
    // show it for a little
	public void setFlipped (boolean flip) {
		isFlipped = flip;
	}

	// still need to draw the card based on its state of match or flip or none
	public void draw(Graphics g) {
		
		if (isMatched) {
			g.drawImage(correctImage, xLocation, yLocation, null);
		}
		else {
			if (isFlipped && !isMatched) {
				g.drawImage(frontImage, xLocation, yLocation, null);
			}
			else {
				g.drawImage(backImage, xLocation, yLocation, null);
			}
		}
	}
}
