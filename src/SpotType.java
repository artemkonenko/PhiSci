
public class SpotType {

	final public boolean[] ways = new boolean[6];
	
	//final private char service = 64;
	final private char start = 32;
	final private char finish = 16;
	final private char left = 8;	
	final private char top = 4;
	final private char right = 2;
	final private char bottom = 1;
	
	public SpotType (char type) {
		ways[0] = (type & start) == start;
		ways[1] = (type & finish) == finish;
		ways[2] = (type & left) != left;
		ways[3] = (type & top) != top;
		ways[4] = (type & right) != right;
		ways[5] = (type & bottom) != bottom;
	}
	
	
}
