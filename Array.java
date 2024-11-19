public class Array<T> {

	private T[] arr;
	private int count;
	private int current;

	public Array(int n) {
		arr = (T[]) new Object[n];
		count = 0;
		current = -1;
	}

	public boolean full() {
		return count == arr.length;
	}

	public boolean empty() {
		return count == 0;
	}

	public boolean last() {
		return current == count - 1;
	}

	public void findFirst() {
		current = 0;
	}

	public void findNext() {
		current++;
	}

	public T retrieve() {
		return arr[current];
	}

	public void update(T data) {
		arr[current] = data;
	}

	public void insert(T data) {
		for (int i = count - 1; i > current; i--)
			arr[i + 1] = arr[i];

		arr[++current] = data;
		count++;
	}

	public void remove() {
		for (int i = current + 1; i < count; i++)
			arr[i - 1] = arr[i];

		count--;

		if (count == 0)
			current = -1;
		else if (current == count)
			current = 0;
	}
}