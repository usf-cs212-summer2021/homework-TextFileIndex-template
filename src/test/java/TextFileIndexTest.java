import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests the {@link TextFileIndex} class.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2021
 */
@TestMethodOrder(MethodName.class)
public class TextFileIndexTest {

	/** Placeholder for index object being tested. */
	private SimpleIndex<Path> index;

	/** Sample text file. */
	private static final Path animals = Path.of("src", "test", "resources", "animals.text");

	/** Sample text file. */
	private static final Path sentences = Path.of("src", "test", "resources", "sentences.md");

	/**
	 * Tests toString implementation.
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class A_StringTests {

		/**
		 * Tests the toString() implementation.
		 */
		@Order(1)
		@Test
		public void testEmpty() {
			// If below does not compile, TextFileIndex is not implementing SimpleIndex properly
			TextFileIndex textFileIndex = new TextFileIndex();
			index = textFileIndex;
			Assertions.assertFalse(index.toString().startsWith("TextFileIndex@"),
					"Override toString() with a useful implementation!");
		}

		/**
		 * Tests the toString() implementation.
		 */
		@Order(2)
		@Test
		public void testOutput() {
			// If below does not compile, TextFileIndex is not implementing SimpleIndex properly
			TextFileIndex textFileIndex = new TextFileIndex();
			index = textFileIndex;
			index.add(Path.of("hello.txt"), "hello");
			Assertions.assertTrue(index.toString().contains("hello"), 
					"Override toString() with a useful implementation!");
		}
	}

	/**
	 * Tests of an empty index.
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class B_EmptyIndexTests {

		/** Path to use for testing. */
		private Path location;

		/**
		 * Creates an empty index before each test.
		 */
		@BeforeEach
		public void createEmpty() {
			TextFileIndex textFileIndex = new TextFileIndex();
			index = textFileIndex;
			location = Path.of("empty.txt");
		}

		/**
		 * Tests that there are no locations.
		 */
		@Test
		@Order(1)
		public void testNumPaths() {
			Assertions.assertEquals(0, index.size(), index.toString());
		}

		/**
		 * Tests that there are no words for a location not in our index.
		 */
		@Test
		@Order(2)
		public void testNumWords() {
			Assertions.assertEquals(0, index.size(location), index.toString());
		}

		/**
		 * Tests that a location does not exist as expected.
		 */
		@Test
		@Order(3)
		public void testContainsPath() {
			Assertions.assertFalse(index.contains(location), index.toString());
		}

		/**
		 * Tests that a word does not exist as expected.
		 */
		@Test
		@Order(4)
		public void testContainsWord() {
			Assertions.assertFalse(index.contains(location, "empty"), index.toString());
		}

		/**
		 * Tests that no locations are fetched as expected.
		 */
		@Test
		@Order(5)
		public void testGetPaths() {
			Assertions.assertTrue(index.get().isEmpty(), index.toString());
		}

		/**
		 * Tests that no words are fetched as expected.
		 */
		@Test
		@Order(6)
		public void testGetWords() {
			Assertions.assertTrue(index.get(location).isEmpty(), index.toString());
		}
	}

	/**
	 * Tests of an index with a single initial location and word.
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class C_SingleAddTests {

		/** Path to use for testing. */
		private Path location;

		/**
		 * Creates an index with a single location and word before each test.
		 */
		@BeforeEach
		public void createEmpty() {
			TextFileIndex textFileIndex = new TextFileIndex();
			index = textFileIndex;
			location = Path.of("hello.txt");
			index.add(location, "hello");
		}

		/**
		 * Test number of locations.
		 */
		@Test
		@Order(1)
		public void testNumPaths() {
			Assertions.assertEquals(1, index.size(), index.toString());
		}

		/**
		 * Tests number of positions for element.
		 */
		@Test
		@Order(2)
		public void testNumWords() {
			Assertions.assertEquals(1, index.size(location), index.toString());
		}

		/**
		 * Tests location exists in index.
		 */
		@Test
		@Order(3)
		public void testContainsPath() {
			Assertions.assertTrue(index.contains(location), index.toString());
		}

		/**
		 * Tests word does NOT exist for a location.
		 */
		@Test
		@Order(4)
		public void testContainsWordFalse() {
			Assertions.assertFalse(index.contains(location, "world"), index.toString());
		}

		/**
		 * Tests word DOES exist for element.
		 */
		@Test
		@Order(5)
		public void testContainsWordTrue() {
			Assertions.assertTrue(index.contains(location, "hello"), index.toString());
		}

		/**
		 * Tests location is fetched properly.
		 */
		@Test
		@Order(6)
		public void testGetElements() {
			Assertions.assertTrue(index.get().contains(location), index.toString());
		}

		/**
		 * Tests word is fetched properly.
		 */
		@Test
		@Order(7)
		public void testGetPositions() {
			Assertions.assertTrue(index.get(location).contains("hello"), index.toString());
		}

		/**
		 * Tests size of elements fetched.
		 */
		@Test
		@Order(8)
		public void testGetPathsSize() {
			Assertions.assertEquals(1, index.get().size(), index.toString());
		}

		/**
		 * Tests size of positions fetched.
		 */
		@Test
		@Order(9)
		public void testGetWordsSize() {
			Assertions.assertEquals(1, index.get(location).size(), index.toString());
		}

		/**
		 * Tests adding same location/word pair twice has no impact.
		 */
		@Test
		@Order(10)
		public void testDoubleAdd() {
			index.add(location, "hello");
			Assertions.assertEquals(1, index.size(location), index.toString());
		}

		/**
		 * Tests adding new word for a location.
		 */
		@Test
		@Order(11)
		public void testAddNewWord() {
			index.add(location, "world");
			Assertions.assertEquals(2, index.size(location), index.toString());
		}

		/**
		 * Tests adding new location.
		 */
		@Test
		@Order(12)
		public void testAddNewPath() {
			index.add(Path.of("world.txt"), "world");
			Assertions.assertEquals(2, index.size(), index.toString());
		}

		/**
		 * Tests adding array of one duplicate word.
		 */
		@Test
		@Order(13)
		public void testAddAllFalse() {
			index.add(location, new String[] { "hello" });
			Assertions.assertEquals(1, index.size(location), index.toString());
		}

		/**
		 * Tests adding array with two words.
		 */
		@Test
		@Order(14)
		public void testAddAllTrue() {
			index.add(location, new String[] { "hello", "world" });
			Assertions.assertEquals(2, index.size(location), index.toString());
		}

		/**
		 * Tests that attempts to modify paths in index fails.
		 */
		@Test
		@Order(15)
		public void testPathsModification() {
			Collection<Path> elements = index.get();

			try {
				elements.clear();
			}
			catch (Exception e) {
				System.err.println("Unable to modify.");
			}

			Assertions.assertTrue(index.contains(location), index.toString());
		}

		/**
		 * Tests that attempts to modify words in index fails.
		 */
		@Test
		@Order(16)
		public void testPositionsModification() {
			Collection<String> elements = index.get(location);

			try {
				elements.clear();
			}
			catch (Exception e) {
				System.err.println("Unable to modify.");
			}

			Assertions.assertTrue(index.contains(location, "hello"));
		}
	}

	/**
	 * Tests real text files.
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class D_RealIndexTests {

		/**
		 * Creates an index with a single location and word before each test.
		 * 
		 * @throws IOException if an I/O error occurs
		 */
		@BeforeEach
		public void createEmpty() throws IOException {
			TextFileIndex textFileIndex = new TextFileIndex();
			index = textFileIndex;

			index.add(animals, getWords(animals));
			index.add(sentences, getWords(sentences));
		}

		/**
		 * Testing whether index was created properly.
		 */
		@Order(1)
		@Test
		public void testAnimalPaths() {
			Assertions.assertTrue(index.contains(animals), index.toString());
		}

		/**
		 * Testing whether index was created properly.
		 */
		@Order(2)
		@Test
		public void testSentencesPaths() {
			Assertions.assertTrue(index.contains(sentences), index.toString());
		}

		/**
		 * Testing whether index was created properly.
		 */
		@Order(3)
		@Test
		public void testAnimals() {
			Assertions.assertEquals(8, index.size(animals), index.toString());
		}

		/**
		 * Testing whether index was created properly.
		 */
		@Order(4)
		@Test
		public void testSentences() {
			Assertions.assertEquals(41, index.size(sentences), index.toString());
		}

		/**
		 * Testing whether index was created properly.
		 */
		@Order(5)
		@Test
		public void testPaths() {
			Set<Path> expected = Set.of(animals, sentences);
			Assertions.assertTrue(index.get().containsAll(expected), index.toString());
		}

		/**
		 * Testing whether index was created properly.
		 */
		@Order(6)
		@Test
		public void testWords() {
			Set<String> expected = Set.of("okapi", "mongoose", "loris", "axolotl", 
					"narwhal", "platypus", "echidna", "tarsier");

			Assertions.assertTrue(index.get(animals).containsAll(expected), index.toString());
		}
	}

	/**
	 * Helper method to quickly read in a small text file and return words.
	 *
	 * @param path the path
	 * @return the words
	 * @throws IOException if an I/O error occurs
	 */
	private static String[] getWords(Path path) throws IOException {
		return Files.readString(path, StandardCharsets.UTF_8).toLowerCase().split("\\W+");
	}
}
