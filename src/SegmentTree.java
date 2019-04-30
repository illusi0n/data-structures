
/**
 * Data structure that enables querying sum on interval and update of the input
 * array in O(logN) time complexity.
 * 
 * @author illusi0n
 *
 */
public class SegmentTree {
	private int[] segmentTree;
	private int[] inputData;
	private int size;

	public SegmentTree(int[] inputData) {
		this.segmentTree = new int[4 * inputData.length];
		this.inputData = inputData;
		this.size = this.inputData.length;
		this.build();
	}

	private void build() {
		buildSegmentTree(1, 0, this.size);
	}

	/**
	 * Build segment tree recursively.
	 * 
	 * @param index
	 *            represents position in the data array
	 * @param left
	 *            left boundary of the interval (inclusive)
	 * @param right
	 *            right boundary of the interval (exclusive)
	 */
	private void buildSegmentTree(int index, int left, int right) {
		if (left + 1 == right) {
			this.segmentTree[index] = inputData[left];
			return;
		}

		int middle = (left + right) / 2;
		buildSegmentTree(2 * index, left, middle);
		buildSegmentTree(2 * index + 1, middle, right);

		this.segmentTree[index] = this.segmentTree[2 * index] + this.segmentTree[2 * index + 1];
	}

	/**
	 * Update inputData array.
	 * 
	 * @param index
	 *            position of the element
	 * @param newValue
	 *            new value of the element
	 */
	public void update(int index, int newValue) {
		update(index, newValue, 1, 0, this.size);
	}

	/**
	 * Update segment tree
	 * 
	 * @param index
	 *            position of the element
	 * @param newValue
	 *            new value of the element
	 * @param segmentIndex
	 *            current index in the segment tree
	 * @param left
	 *            left boundary of the interval
	 * @param right
	 *            right boundary of the interval
	 */
	private void update(int index, int newValue, int segmentIndex, int left, int right) {
		this.segmentTree[segmentIndex] += newValue - inputData[index];

		if (left + 1 == right) {
			inputData[index] = newValue;
			this.segmentTree[left] = newValue;
			return;
		}

		int middle = (left + right) / 2;
		if (index < middle) {
			update(index, newValue, 2 * segmentIndex, left, middle);
		} else {
			update(index, newValue, 2 * segmentIndex + 1, middle, right);
		}
	}

	/**
	 * Find sum of elements on the interval [left, right)
	 * 
	 * @param left
	 *            left boundary of the query interval
	 * @param right
	 *            right boundary of the query interval
	 * @return sum on the query interval
	 */
	public int sum(int left, int right) {
		return sum(left, right, 1, 0, this.size);
	}

	/**
	 * Find sum of elements on the interval [left, right)
	 * 
	 * @param left
	 *            left boundary of the query interval
	 * @param right
	 *            right boundary of the query interval
	 * @param segmentIndex
	 *            current index in the segment tree
	 * @param segmentLeft
	 *            left boundary of the current segment interval
	 * @param segmentRight
	 *            right boundary of the current segment interval
	 * @return sum on the segment interval
	 */
	private int sum(int left, int right, int segmentIndex, int segmentLeft, int segmentRight) {
		if (left >= segmentRight || right <= segmentLeft) {
			return 0;
		}

		// segment interval is inside the query interval
		if (left <= segmentLeft && right >= segmentRight) {
			return this.segmentTree[segmentIndex];
		}
		int middle = (segmentLeft + segmentRight) / 2;
		return sum(left, right, 2 * segmentIndex, segmentLeft, middle)
				+ sum(left, right, 2 * segmentIndex + 1, middle, segmentRight);
	}
}
