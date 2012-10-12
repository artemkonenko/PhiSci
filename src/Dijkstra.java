import java.util.Comparator;
import java.util.PriorityQueue;

class SpotCompare implements Comparator<Spot> {
	@Override
	public int compare(Spot o1, Spot o2) {
		return o1.dijkstraDistance - o2.dijkstraDistance;
	}	
}

public class Dijkstra {
	private static PriorityQueue<Spot> que = new PriorityQueue<Spot>(11, new SpotCompare());

	static void check(Spot cur, Spot broth) {
		if (!que.contains(broth) && !broth.dijkstraFlag) {
			broth.dijkstraDistance = cur.dijkstraDistance + 1;
			que.offer(broth);
		} else {
			if (broth.dijkstraDistance > cur.dijkstraDistance + 1) {
				broth.dijkstraDistance = cur.dijkstraDistance + 1;
			}
		}
	}
	
	static int calcStartToFinishDistance(Spot aMap[][], Spot finish) {
		for (Spot[] spots : aMap) {
			for (Spot spot : spots) {
				spot.dijkstraDistance = Integer.MAX_VALUE;
				spot.dijkstraFlag = false;
			}
		}
		
		aMap[finish.y][finish.x].dijkstraDistance = 0;
		que.offer(aMap[finish.y][finish.x]);
		
		while (que.peek() != null) {
			Spot b = que.poll();
			
			if (b.left != null)
				check(b, b.left);
			
			if (b.top != null)
				check(b, b.top);
			
			if (b.right != null)
				check(b, b.right);
			
			if (b.bottom != null)
				check(b, b.bottom);

			b.dijkstraFlag = true;
		}
		
		return 1;
	}
	
}
