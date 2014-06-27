public class TestMask {

	/**
	 * @param args
	 */
	
	static Map map;
	static Screen screen;
	
	public static void main(String[] args){
		
		try {
			map = new Map("test404060"); // minimap
		} catch (Exception e) {
			System.out.println("GenMap: " + e);
		}

		
		screen = new Screen(map.getSize()[1], map.getSize()[0]);
		
		screen.aMap = map.aMap;
		screen.px = map.start.x;
		screen.py = map.start.y;
		
		screen.addDrawble(map);
		screen.start();
	}

}
