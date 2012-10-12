
public class RunGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MapGenerator mg = new MapGenerator(30, 20, 100);
		
		mg.writeMapToFile("data/gg1.lmap");

	}

}
