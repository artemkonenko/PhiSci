import java.awt.Graphics;
import java.awt.Image;

public class Sprite {
    private Image image; //изображение
    
    public Sprite(Image image) {
        this.image = image;
    }
    
    public int getWidth() { //получаем ширину картинки
        return image.getWidth(null);
    }

    public int getHeight() { //получаем высоту картинки
        return image.getHeight(null);
    }
    
    public void draw(Graphics g,int x,int y, int scale) { //рисуем картинку
        g.drawImage(image, x, y, scale, scale, null);
    }
}