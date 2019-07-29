class MyHashtable implements DictionaryInterface {

	//Fields of MyHashTable
	protected int tableSize;
	protected int size;
	protected MyLinkedList[] table;
	
	
	protected class Entry
	{
		//Stores a key and value in the Object part of a Node 
		String key;
		Object value;
		
		//The constructor for Entry
		Entry(String k, Object o)
		{
			key = k;
			value = o;
		}
	}
	
	//The constructor for MyHashtable
	public MyHashtable(int tableSize)
	{
		this.tableSize = tableSize;
		int size = 0;
		table = new MyLinkedList[tableSize];
	}

	//Returns if the list is empty or not 
	public boolean isEmpty()
	{
		//If the size is 0 then the list is empty 
		if(size() == 0)
		{
			return true;
		}

		//If the size is not 0 then the list is not empty 
		else 
		{
			return false;
		}
	}
	
	//Gives the size
	public int size()
	{
		return size;
	}

	//Returns a value at the given key
	public Object get(String key)
	{
		//Compute the array index using a hashcode
		int hashCode = key.hashCode();
		int arrayIndex = Math.abs(hashCode) % tableSize;
		
		
		// If the location is null that means theres nothing there and return null
		if(table[arrayIndex] == null)
		{
			return null;
		}
		
		// If location isn't null then that means theres a bucket there and you have to search it  
		else
		{
			//Loop through the linked list at the index and return the value of the key
			for(int i = 0; i < table[arrayIndex].size(); i++)
			{
				if(key.equals(((Entry)table[arrayIndex].get(i)).key))
				{
					return ((Entry)(table[arrayIndex].get(i))).value;
				}
			}
						
		}

		//If key is not found, then that means the key is not there and return null
		return null;
	}
		
	public Object put(String key, Object value)
	{
		//Compute the array index using a hashcode
		int hashCode = key.hashCode();
		int arrayIndex = Math.abs(hashCode) % tableSize;
		

		//If the index at the table is null then that means there is nothing there so we add the given key and value
		if(table[arrayIndex] == null) 
		{
			MyLinkedList bucket = new MyLinkedList();
			bucket.add(0, new Entry(key,value));
			table[arrayIndex] = bucket;
			size++;
		}

		//If it isnt null that means there is already an existing value there and we have to replace it 
		else
		{
			//Loop through the array 
			for(int i = 0; i < table[arrayIndex].size; i++)
			{
				if(key.equals(((Entry)table[arrayIndex].get(i)).key))
				{
					//Store the old value in a variable 
					Object oldValue = ((Entry)table[arrayIndex].get(i)).value;
					((Entry)table[arrayIndex].get(i)).value = value;
					return oldValue;
				}
			}

			//Add the new value to the table and increase the size
			table[arrayIndex].add(0, new Entry(key, value));
			size++;
		}
		
		return null;
	}
	
	//We remove the nodes based off the given key
	public void remove(String key)
	{
		int hashCode = key.hashCode();
		int arrayIndex = Math.abs(hashCode) % tableSize;
		
		//If the linked list is not null then we remove 
		if(table[arrayIndex] != null)
		{
			for(int i = 0; i < table[arrayIndex].size; i++)
			{
				if(key.equals(((Entry)table[arrayIndex].get(i)).key))
				{
					table[arrayIndex].remove(i);
					size--;
				}
			}
		}
	}
	
	//This gets all the keys in the table
	public String[] getKeys()
	{
		//Make a new string array to store the keys and a counter
		String[] allKeys = new String[size];
		int count = 0;
		
		//Loop through the entire table 
		for(int i = 0; i < tableSize; i++) 
		{
			if(table[i] != null)
			{

				for(int j = 0; j < table[i].size; j++) 
				{
					//Add each key to the string array
					allKeys[count] = ((Entry)table[i].get(j)).key;
					count++;
				}
			}
		}
		//Return the array of keys
		return allKeys;
	}

	//Clears the entire table and set size equal to zero
	public void clear() 
	{
		table = new MyLinkedList[0];
		size = 0;
	}

		
	// Returns the size of the biggest bucket (most collisions) in the hashtable. 
	public int biggestBucket() {
		int biggestBucket = 0; 
		for(int i = 0; i < table.length; i++) {
			// Loop through the table looking for non-null locations. 
			if (table[i] != null) {
				// If you find a non-null location, compare the bucket size against the largest
				// bucket size found so far. If the current bucket size is bigger, set biggestBucket
				// to this new size. 
				MyLinkedList bucket = table[i];
				if (biggestBucket < bucket.size())
					biggestBucket = bucket.size();
			}
		}
		return biggestBucket; // Return the size of the biggest bucket found. 
	}

	// Returns the average bucket length. Gives a sense of how many collisions are happening overall.
	public float averageBucket() {
		float bucketCount = 0; // Number of buckets (non-null table locations)
		float bucketSizeSum = 0; // Sum of the size of all buckets
		for(int i = 0; i < table.length; i++) {
			// Loop through the table 
			if (table[i] != null) {
				// For a non-null location, increment the bucketCount and add to the bucketSizeSum
				MyLinkedList bucket = table[i];
				bucketSizeSum += bucket.size();
				bucketCount++;
			}
		}

		// Divide bucketSizeSum by the number of buckets to get an average bucket length. 
		return bucketSizeSum/bucketCount; 
	}

	public String toString() {
		String s = "";
		for(int tableIndex = 0; tableIndex < tableSize; tableIndex++) {
			if (table[tableIndex] != null) {
				MyLinkedList bucket = table[tableIndex];
				for(int listIndex = 0; listIndex < bucket.size(); listIndex++) {
					Entry e = (Entry)bucket.get(listIndex);
					s = s + "key: " + e.key + ", value: " + e.value + "\n";
				}
			}
		}
		return s; 
	}
}