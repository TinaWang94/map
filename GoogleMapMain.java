package mapservice;

/**
 * Demonstration of map manager and maker.  This should
 * create a file, test.js, with the created points.
 */
public class GoogleMapMain {
	/**
	 * File containing JS-encoded array of locations
	 */
	private static final String LOCATIONFILE = "test.js";

	/**
	 * Test main
	 * @param args ignored
	 */
	public static void main(String[] args) {
		// Create map manager in memory
		MapManager mgr = new MemoryMapManager();
		// Register listener to update Google location file
		mgr.register(new GoogleMapMaker(LOCATIONFILE, mgr));
		// Add first location
		mgr.addLocation(new Location("One", "3", "4", "One place", Location.Color.BLUE));
		// Location file should now contain one location
		
		// Add second location
		mgr.addLocation(new Location("two", "4", "5", "Two place", Location.Color.GREEN));
		// Location file should now contain two locations
	}
}
