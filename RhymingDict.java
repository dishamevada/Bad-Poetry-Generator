import java.io.*;
import java.util.*;
import java.util.Arrays;

public class RhymingDict {
	private static Random random;
   	
	// Given a pronunciation, get the rhyme group
	// get the more *heavily emphasized vowel* and follwing syllables
	// For "tomato", this is "-ato", and not "-omato", or "-o"
	// Tomato shares a rhyming group with "potato", but not "grow"
	private static String getRhymeGroup(String line) {

		int firstSpace = line.indexOf(" "); 

		String pronunciation = line.substring(firstSpace + 1, line.length());

		int stress0 = pronunciation.indexOf("0");
		int stress1 = pronunciation.indexOf("1");
		int stress2 = pronunciation.indexOf("2");

		if (stress2 >= 0)
			return pronunciation.substring(stress2 - 2, pronunciation.length());
		if (stress1 >= 0)
			return pronunciation.substring(stress1 - 2, pronunciation.length());
		if (stress0 >= 0)
			return pronunciation.substring(stress0 - 2, pronunciation.length());
		
		// No vowels at all? ("hmmm", "mmm", "shh")
		return pronunciation;
	}

	private static String getWord(String line) {
		int firstSpace = line.indexOf(" ");

		String word = line.substring(0, firstSpace);

		return word; 
	}

	// Load the dictionary
	public static void loadDictionary(DictionaryInterface rhymingDictionary) {
		
		// Load the file and read it
		try {
			String path = "cmudict/cmudict-abridged.dict";
			FileInputStream stream = new FileInputStream(new File(path));
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

			for(String line = reader.readLine(); line != null; line = reader.readLine()) {
				storeRhyme(rhymingDictionary, line);
			}
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	} 

	// TO DO 2: Store a rhymeGroup (key) and word (value) in the Dictionary (hashtable)
	public static void storeRhyme(DictionaryInterface rhymingDict, String line)
	{
		//Store a word and rhhymegroup with the methods
		String word = getWord(line);
		String rhymeGroup = getRhymeGroup(line);
		
		//If it is null then we add the word to the list
		if (rhymingDict.get(rhymeGroup) == null)
		{
			MySortedLinkedList list = new MySortedLinkedList();
			list.add(word);
			rhymingDict.put(rhymeGroup, list);
		}
		else 
		{
			MySortedLinkedList list2 =  (MySortedLinkedList) rhymingDict.get(rhymeGroup);
			list2.add(word);
		}

	}

	// Get two random indexes that are not the same
	public static int[] getTwoDifferentRandomIndexes(int length) {
		// Get the first random index.  Pick any index in the length
		int index0 = random.nextInt(length);

		// Tricky: this index is offset from the other by a 
		// random number less than the total length but 1 or more, 
		// so we know that the two indexes can't be the same
		int offset = random.nextInt(length - 1) + 1;
		int index1 = (index0 + offset)%length;
		
		int[] indexes =  {index0, index1};
		return indexes;
	} 

	// Select two rhymeGroups, select two random words from each rhyme group, and make a rhyme. 
	public static void createRhyme(DictionaryInterface rhymingDict) {
		
		// Get all the keys as an array
		String[] keys = rhymingDict.getKeys();
		//System.out.printf("%d choices for rhyming groups\n", keys.length);
		
		// Pick out the two rhyme groups
		int[] groupIndexes = getTwoDifferentRandomIndexes(keys.length);
		String group0Key = keys[groupIndexes[0]];
		String group1Key = keys[groupIndexes[1]];

		// Uncomment to see which word groups were selected
		// System.out.printf("Create rhyme with groups '%s' and '%s' \n", group0Key, group1Key);
		
		MyLinkedList group0 = (MyLinkedList)rhymingDict.get(group0Key);
		MyLinkedList group1 = (MyLinkedList)rhymingDict.get(group1Key);
		
		// Uncomment to check the length of the word sets we selected
		//System.out.printf("\t%d words, %d words \n", group0.getLength(), group1.getLength());
		
		// Pick out word indexes from the two rhyme groups
		// We need four words, so two words from each group
		int[] group0Indexes = getTwoDifferentRandomIndexes(group0.size());
		int[] group1Indexes = getTwoDifferentRandomIndexes(group1.size());
		
		String word0A = (String)group0.get(group0Indexes[0]);
		String word0B = (String)group0.get(group0Indexes[1]);
		String word1A = (String)group1.get(group1Indexes[0]);
		String word1B = (String)group1.get(group1Indexes[1]);
		
		// Uncomment to check which words it picked
		//System.out.printf("\tWords: %s %s %s %s \n", word0A, word0B, word1A, word1B);

		// Choose between two poems
		if (random.nextFloat() > 0.5f)
			System.out.printf("\nRoses are %s,\nviolets are %s.\nI am %s\nand you are %s.\n", word0A, word1A, word0B, word1B);
		else
			System.out.printf("\nIf I were %s\nthen you'd be the %s,\nAnd we'd both be %s\nand never be %s\n", word0A, word1A, word0B, word1B);
	
	} 

	// TO DO #3: Remove any of the unrhymables
	public static void removeUnrhymables(DictionaryInterface rhymingDict) 
	{
		//Store all the keys in a string array
		String [] totalKeys = rhymingDict.getKeys();

		//Loop through the array of keys 
		for(int i = 0; i < totalKeys.length; i++)
		{
			//Add them to a list 
			MySortedLinkedList list = (MySortedLinkedList)rhymingDict.get(totalKeys[i]);
			
			//If the list is only one size then that means there are no other words that rhyme with it and remove it 
			if(list.size() == 1) 
			{
				rhymingDict.remove(totalKeys[i]);
			}

		}

	} 
	
	// Once you've implemented your dictionary, you can use the testDictionary function to test 
	// that your dictionary is doing what you expect. 
	public static void testDictionary(DictionaryInterface dict) {
		dict.put("f", "food");
		dict.put("c", "candy");
		System.out.println(dict); // Should have the two keys and values above

		dict.put("b", "bar");
		System.out.println(dict); // Should have added one more key/value pair
		
		dict.put("b", "box");
		System.out.println(dict); // Should have replaced the value stored with "b"

		dict.put("c", "cat");
		dict.remove("b");
		System.out.println(dict); // Now should have new value stored with key "c", and no key "b"
	}	
	
	// Reads in the pronunciation dictionary and makes a dictionary
	public static void main(String[] args) {

		// Uncomment this to test that your hashtable is doing what you expect. 
		// MyHashtable testTable = new MyHashtable(100);
		// testDictionary(testTable);

		// Save a seed.  
		// Like in Lab01, the seeds allow us generate the same random numbers (the same poems) again
		long seed = System.currentTimeMillis() % 1000;
		if (args.length > 0)
			seed = Long.parseLong(args[0]);
		random = new Random(seed);

		// Set the number of poems to write
		int poemCount = 3;
		if (args.length > 1)
			poemCount = Integer.parseInt(args[1]);
				
		MyHashtable rhymingDict = new MyHashtable(20000);

		// Read in all the dictionary lines
		loadDictionary(rhymingDict);

		// Comment in if you want to experiment with the bucket size. 
		/* 
		String[] keys = rhymingDict.getKeys();
		System.out.println(keys.length);
		System.out.println("Biggest bucket = " + rhymingDict.biggestBucket() + " average bucket = " + rhymingDict.averageBucket());
		*/

		removeUnrhymables(rhymingDict);
		for (int i = 0; i < poemCount; i++) {
			createRhyme(rhymingDict);
		}
	}

}