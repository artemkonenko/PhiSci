import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Map implements IDrawble{
	
	// Ссылки на старт и финиш. Другие точки входа в граф нам пока нем нужны.
	public Spot start, finish;
	public Spot[][] aMap;
	
	private int[] size;
	public int[] getSize() {
		return size;
	}

	/**
	 * 
	 * @param mapSrc
	 * @throws IOException
	 */
	private void parseMap (String mapSrc) throws IOException {
		FileReader fr = new FileReader("./data/" + mapSrc + ".lmap"); //
		BufferedReader br = new BufferedReader(fr);
		
		// Парсим размерности (первая строчка файла карты)
		String sSize[] = br.readLine().split("/");
		size = new int[2];
		for (int i = 0; i < sSize.length; i++)
			size[i] = Integer.parseInt(sSize[i]);
		
		// Заводим временный массивчик для удобной генерации графа 
		aMap = new Spot[size[0]][size[1]];
		
		for (int i = 0; i < size[0]; i++) {
			char[] cSpot = br.readLine().toCharArray();
			for (int j = 0; j < size[1]; j++) {
				aMap[i][j] = new Spot(cSpot[j], j, i);
			}
		}
		
		reconnect();
		
		br.close();
		fr.close();
	}
	
	/**
	 * По чарным битам восстанавливает структуру графа
	 */
	private void reconnect() {
		int heigth = size[0];
		int width = size[1];
		
		for (int h=0; h < heigth; h++) {
			for (int w=0; w<width; w++) {
				if (aMap[h][w].type.ways[0]) {
					this.start = aMap[h][w];
				}
				
				if (aMap[h][w].type.ways[1]) {
					this.finish = aMap[h][w];
				}
				
				if (aMap[h][w].type.ways[2]) {
					aMap[h][w].setLeft(aMap[h][w-1]);
				} else if (w > 0){
					aMap[h][w-1].right = null;
					aMap[h][w-1].cType = (char) (aMap[h][w-1].cType | (char)2);
					aMap[h][w-1].updateType();
				}
				
				if (aMap[h][w].type.ways[3]) {
					aMap[h][w].setTop(aMap[h-1][w]);
				} else if (h > 0){
					aMap[h-1][w].bottom = null;
					aMap[h-1][w].cType = (char) (aMap[h-1][w].cType | (char)1);
					aMap[h-1][w].updateType();
				}
				
				if (aMap[h][w].type.ways[4]) {
					aMap[h][w].setRight(aMap[h][w+1]);
				} else if (w < width-1){
					aMap[h][w+1].left = null;
					aMap[h][w+1].cType = (char) (aMap[h][w+1].cType | (char)8);
					aMap[h][w+1].updateType();
				}
				
				if (aMap[h][w].type.ways[5]) {
					aMap[h][w].setBottom(aMap[h+1][w]);
				} else if (h < heigth-1){
					aMap[h+1][w].top = null;
					aMap[h+1][w].cType = (char) (aMap[h+1][w].cType | (char)4);
					aMap[h+1][w].updateType();
				}
			}
		}
	}
	
	public Map(String mapSrc) {
		try {
			parseMap(mapSrc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Dijkstra.calcStartToFinishDistance(aMap, this.finish);
	}

	@Override
	public void draw(Graphics g, int scale) {
		for (int i = 0; i < size[0]; i++) {
			for (int j = 0; j < size[1]; j++) {
				aMap[i][j].draw(g, scale);
			}
		}
	}
	
}
