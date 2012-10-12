import java.awt.Graphics;


public class Spot{
	public Spot left, top, right, bottom;
	public int x, y;
	public SpotType type;
	/**
	 * #######
	 *  bits:
	 *  1 - bottom
	 *  2 - right
	 *  4 - top
	 *  8 - left
	 * 16 - finish
	 * 32 - start
	 * 64 - service bit
	 * #######
	 */
	private final char BOTTOM = 1;
	private final char RIGHT = 2;
	private final char TOP = 4;
	private final char LEFT = 8;
	private final char FINISH = 16;
	private final char START = 32;
	public char cType;
	
	//########### ����� ��� ��������
	public boolean dijkstraFlag = false;
	public int dijkstraDistance = Integer.MAX_VALUE;
	
	//########### SPRITES
	public static Sprite solid, floor, start, finish;
	
	//########### Constructor
	public Spot(char cType, int x, int y) {
		this.cType = cType;
		this.x = x;
		this.y = y;
		
		updateType();
	}
	
	public void updateType() {
		this.type = new SpotType(cType);
	}
	
	//########### IS AND SET START/FINISH
	/**
	 * ��������, ��� ������ �������� �������
	 * @return
	 */
	public boolean isStart() {
		return (this.cType & START) == START;
	}
	
	/**
	 * ������������� ��� ������
	 */
	public void setStart() {
		this.cType |= START;
	}
	
	/**
	 * ��������, ��� ������ �������� �������
	 * @return
	 */
	public boolean isFinish() {
		return (this.cType & FINISH) == FINISH;
	}
	
	public void setFinish() {
		this.cType |= FINISH;
	}
	
	//########### LEFT
	/**
	 * �������� ������������� ����� �����
	 * @return
	 */
	public boolean hasLeft() {
		return (this.cType & LEFT) == LEFT;
	}
	
	/**
	 * ��������� ����� ����� � ���������� ��������� �����
	 * @param spot - ����� �����
	 */
	public void setLeft(Spot spot) {
		this.friendLeft(spot);
		spot.friendRight(this);
	}
	
	/**
	 * ��������� ����� ����� ��� ��������� ��������� �����
	 * @param friend
	 */
	public void friendLeft(Spot friend) {
		this.left = friend;
		this.cType ^= LEFT;
	}
	
	/**
	 * �������� ����� ����� � ���������� ������� ������
	 */
	public void removeLeft() {
		if (!this.hasLeft()) {
			this.left.quarrelRight();
			this.quarrelLeft();
		}
	}
	
	/**
	 * �������� ����� ����� ��� ��������� ������� ������ 
	 */
	public void quarrelLeft() {
		this.left = null;
		this.cType |= 8;
	}
	
	//########### TOP
	/**
	 * �������� ������������� ����� ������
	 * @return
	 */
	public boolean hasTop() {
		return (this.cType & TOP) == TOP;
	}
	
	/**
	 * ��������� ����� ������
	 * @param spot - ������� �����
	 */
	public void setTop(Spot spot) {
		this.friendTop(spot);
		spot.friendBottom(this);
	}
	
	/**
	 * ��������� ����� ������ ��� ��������� ��������� �����
	 * @param friend
	 */
	public void friendTop(Spot friend) {
		this.top = friend;
		this.cType ^= TOP;
	}
	
	/**
	 * �������� ����� ������ � ���������� ������� ������
	 */
	public void removeTop() {
		if (!this.hasTop()) {
			this.top.quarrelBottom();
			this.quarrelTop();
		}
	}
	
	/**
	 * �������� ����� ������ ��� ��������� ������� ������ 
	 */
	public void quarrelTop() {
		this.top = null;
		this.cType |= TOP;
	}
	
	//########### RIGHT
	/**
	 * �������� ������������� ����� ������
	 * @return
	 */
	public boolean hasRight() {
		return (this.cType & RIGHT) == RIGHT;
	}
	
	/**
	 * ��������� ����� ������
	 * @param spot - ������ �����
	 */
	public void setRight(Spot spot) {
		this.friendRight(spot);
		spot.friendLeft(this);
	}
	
	/**
	 * ��������� ����� ������ ��� ��������� ��������� �����
	 * @param friend
	 */
	public void friendRight(Spot friend) {
		this.right = friend;
		this.cType ^= RIGHT;
	}
	
	/**
	 * �������� ����� ������ � ���������� ������� ������
	 */
	public void removeRight() {
		if (!this.hasRight()) {
			this.right.quarrelLeft();
			this.quarrelRight();
		}
	}
	
	/**
	 * �������� ����� ������ ��� ��������� ������� ������ 
	 */
	public void quarrelRight() {
		this.right = null;
		this.cType |= RIGHT;
	}
	
	//########### BOTTOM
	/**
	 * �������� ������������� ����� �����
	 * @return
	 */
	public boolean hasBottom() {
		return (this.cType & BOTTOM) == BOTTOM;
	}
	
	/**
	 * ��������� ����� �����
	 * @param spot - ������ �����
	 */
	public void setBottom(Spot spot) {
		this.friendBottom(spot);
		spot.friendTop(this);
	}
	
	/**
	 * ��������� ����� ����� ��� ��������� ��������� �����
	 * @param friend
	 */
	public void friendBottom(Spot friend) {
		this.bottom = friend;
		this.cType ^= BOTTOM;
	}
	
	/**
	 * �������� ����� ����� � ���������� ������� ������
	 */
	public void removeBottom() {
		if (!this.hasBottom()) {
			this.bottom.quarrelTop();
			this.quarrelBottom();
		}
	}
	
	/**
	 * �������� ����� ����� ��� ��������� ������� ������ 
	 */
	public void quarrelBottom() {
		this.bottom = null;
		this.cType |= BOTTOM;
	}
	
	//########### DRAW
	public void draw(Graphics g, int scale) {
		if (!type.ways[2] && !type.ways[3] && !type.ways[4] && !type.ways[5]) {
			solid.draw(g, x * scale, y * scale, scale);
		} else {
			floor.draw(g, x * scale, y * scale, scale);
			
			if (type.ways[0])
				start.draw(g, x * scale, y * scale, scale);
			
			if (type.ways[1])
				finish.draw(g, x * scale, y * scale, scale);
			
			if (!type.ways[2])
				g.drawLine( x * scale , y * scale, x * scale, (y + 1) * scale);
			
			if (!type.ways[3])
				g.drawLine( x * scale , y * scale, (x + 1) * scale, y * scale);
			
			if (!type.ways[4])
				g.drawLine( (x + 1) * scale , y * scale, (x + 1) * scale, (y + 1) * scale);
			
			if (!type.ways[5])
				g.drawLine( x * scale , (y + 1) * scale, (x + 1) * scale, (y + 1) * scale);
		} 
	}
}
