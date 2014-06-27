import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MapGenerator {

	// Пары координат
	class Pair {
		public int x, y;
		
		public Pair (int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		public String toString() {
			return "(" + x + "," + y + ")";
		}

		private MapGenerator getOuterType() {
			return MapGenerator.this;
		}
	}
	
	private Spot start, finish;
	private Spot[][] aMap;
	
	
	// Дэк шагов
	private Deque<Pair> steps = new LinkedList<Pair>();
	private List<Pair> ways = new LinkedList<Pair>();
	
	private int heigth, width;
	
	private Random rand = new Random();

	
/*	*//**
	 * По чарным битам восстанавливает структуру графа
	 *//*
	private void reconnect() {
		for (int h=0; h < heigth; h++) {
			for (int w=0; w<width; w++) {
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
	}*/
	
	/**
	 * 
	 * @param width
	 * @param heigth
	 * @param length - длина пути
	 */
	public MapGenerator(int width, int heigth, int length) {
		this.width = width;
		this.heigth = heigth;
		
		// Всё начинается с массива спотов...
		aMap = new Spot[heigth][width];
		for (int h=0; h < heigth; h++) {
			for (int w=0; w<width; w++) {
					aMap[h][w] = new Spot((char)79, w, h);
					if ( h > 0 && h < heigth-1 && w > 0 && w < width-1 )
						aMap[h][w].dijkstraFlag = false;
					else
						aMap[h][w].dijkstraFlag = true;
			}
		}
		
		// Возводим граф расставляя связи
		//this.reconnect();
		
		// Выбираем старт
		int sx = rand.nextInt(this.width-2) + 1;
		int sy = rand.nextInt(this.heigth-2) + 1;
		
		// И делаем его началом нашего пути
		steps.addLast(new Pair(sx, sy));
		aMap[sy][sx].dijkstraFlag = true;
		
		// "Идем пока не дойдем" (c) Искариот
		while (steps.size() <= length) { 
			Pair l = steps.getLast(); // Откуда шагаем
			
			// Собираем список всех доступных клеток
			List<Pair> lp = new ArrayList<Pair>();
			
			if (l.x > 1 && !aMap[l.y][l.x-1].dijkstraFlag)
				lp.add(new Pair(l.x - 1, l.y));
			if (l.x < width - 2 && !aMap[l.y][l.x+1].dijkstraFlag)
				lp.add(new Pair(l.x + 1, l.y));
			if (l.y > 1 && !aMap[l.y-1][l.x].dijkstraFlag)
				lp.add(new Pair(l.x, l.y - 1));
			if (l.y < heigth - 2 && !aMap[l.y+1][l.x].dijkstraFlag)
				lp.add(new Pair(l.x, l.y + 1));
			
			System.out.println(steps.getLast().toString());
			
			// Выбираем куда идти
			if (lp.size() == 0) { // Если мы в тупике, то шаг назад
				// Делаем шаг назад
				Pair p = steps.removeLast();
				// Смотрим куда отшагнули
				Pair pp = steps.getLast();
				
				if (pp.x - p.x == 1) {
					aMap[pp.y][pp.x].removeLeft();
				} else if (pp.x - p.x == -1) {
					aMap[pp.y][pp.x].removeRight();
				} else if (pp.y - p.y == 1) {
					aMap[pp.y][pp.x].removeTop();
				} else {
					aMap[pp.y][pp.x].removeBottom();
				}
			} else { // Иначе шагаем
				// Выбираем куда шагаем
				Pair p = lp.get(rand.nextInt(lp.size()));
				
				
				if (l.x - p.x == 1) {
					aMap[l.y][l.x].setLeft(aMap[p.y][p.x]);
				} else if (l.x - p.x == -1) {
					aMap[l.y][l.x].setRight(aMap[p.y][p.x]);
				} else if (l.y - p.y == 1) {
					aMap[l.y][l.x].setTop(aMap[p.y][p.x]);
				} else {
					aMap[l.y][l.x].setBottom(aMap[p.y][p.x]);
				}
				
				steps.addLast(p);
				aMap[p.y][p.x].dijkstraFlag = true;
			}
		}
		
		this.start = aMap[steps.getFirst().y][steps.getFirst().x];
		this.start.setStart();
		
		this.finish = aMap[steps.getLast().y][steps.getLast().x];
		this.finish.setFinish();
		//################## На этом генерация основного пути закончилась
		// Чистим следы шагов из тупиков
		for (int i = 0; i < aMap.length; i++) {
			for (int j = 0; j < aMap.length; j++) {
				if (!steps.contains(new Pair(j, i))) {
					aMap[i][j].dijkstraFlag = false;
				}
			}
		}
		
		ways.addAll(steps);
		
		// Генерируем дополнительные тупики
		int z = 0;
		while (z++ < length * 2) { // this.countOfAllBlock() - this.countOfBlock() < this.countOfAllBlock() * (double)3 / 2  
			this.makeFakeWay(rand.nextInt(length));
		}
		
		
		
		Dijkstra.calcStartToFinishDistance(aMap, this.finish);
		
		//Dijkstra.calcStartToFinishDistance(aMap, this.finish);
		System.out.println(this.start.dijkstraDistance);
		
	}

	/**
	 * Количество установленных стен на карте
	 * @return
	 */
	private int countOfBlock() {
		int block = 0;
		
		for (int i = 1; i < aMap.length - 1; i++) {
			for (int j = 1; j < aMap.length - 1; j++) {
				block += aMap[i][j].hasLeft() && aMap[i][j].hasBottom() && aMap[i][j].hasRight() && aMap[i][j].hasTop()?1:0; 
			}
		}
		
		return block;
	}
	
	/**
	 * Максимальное количество стен карты
	 * @return
	 */
	private int countOfAllBlock() {
		return (this.heigth - 2) * (this.width - 2);
	}
	
	/**
	 * Прокладываем фейковый путь
	 */
	private void makeFakeWay(int maxlen) {
		Deque<Pair> way = new LinkedList<Pair>();
		
		Pair[] exSpot = ways.toArray(new Pair[]{new Pair(1, 2)});
		way.addLast(exSpot[rand.nextInt(exSpot.length)]);
		
		while (way.size() < maxlen) {
			Pair l = way.getLast(); // Откуда шагаем
			
			// Собираем список всех доступных клеток
			List<Pair> lp = new ArrayList<Pair>();
			
			if (l.x > 1 && !steps.contains(new Pair(l.x - 1, l.y)))
				lp.add(new Pair(l.x - 1, l.y));
			if (l.x < width - 2 && !steps.contains(new Pair(l.x + 1, l.y)))
				lp.add(new Pair(l.x + 1, l.y));
			if (l.y > 1 && !steps.contains(new Pair(l.x, l.y - 1)))
				lp.add(new Pair(l.x, l.y - 1));
			if (l.y < heigth - 2 && !steps.contains(new Pair(l.x, l.y + 1)))
				lp.add(new Pair(l.x, l.y + 1));
			
			// Выбираем куда идти
			if (lp.size() == 0) {
				break;
			} else { // Иначе шагаем
				// Выбираем куда шагаем
				Pair p = lp.get(rand.nextInt(lp.size()));
				
				if (l.x - p.x == 1) {
					aMap[l.y][l.x].setLeft(aMap[p.y][p.x]);
				} else if (l.x - p.x == -1) {
					aMap[l.y][l.x].setRight(aMap[p.y][p.x]);
				} else if (l.y - p.y == 1) {
					aMap[l.y][l.x].setTop(aMap[p.y][p.x]);
				} else {
					aMap[l.y][l.x].setBottom(aMap[p.y][p.x]);
				}
				
				way.addLast(p);
				aMap[p.y][p.x].dijkstraFlag = true;
			}
		}
		
		ways.addAll(way);
		System.out.println("Провели фейковый путь длины: " + way.size());
	}
	
	public void writeMapToFile(String src) {
		try {
			FileWriter fw = new FileWriter(src);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(this.heigth + "/" + this.width);
			bw.newLine();
			
			for (int h = 0; h < this.heigth; h++) {
				for (int w = 0; w < this.width; w++) {
					bw.write(aMap[h][w].cType);
				}
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
