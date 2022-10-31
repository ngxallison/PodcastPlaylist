/*
THIS CODE WAS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
CODE WRITTEN BY OTHER STUDENTS OR COPIED FROM ONLINE RESOURCES.
ALLISON_NG
*/

import org.w3c.dom.Node;


/*  This class represents a Playlist of podcast episodes, where each
/*  episode is implemented as an object of type Episode. A user navigating
/*  a Playlist should be able to move between songs using Next or Previous.
/*
/*  To enable flexible navigation, the Playlist is implemented as
/*  a Circular Doubly Linked List where each episode has a link to both
/*  the next and the prev episodes in the list.
/*
/*  Additionally, the last Episode is linked to the head of the list (via next),
/*  and the head of the list is linked to that last Episode (via prev). That said,
/*  there is NO special "last" reference keeping track of the last Episode.
/*  But there is a "head" reference that should always refer to the first Episode.
*/
public class Playlist {
	private Episode head;
	private int size;

	public Playlist(){
		head = null;
		size = 0;
	}

	public boolean isEmpty() {
		return head == null;
	}

	// Ensure that "size" is updated properly in other methods, to
	// always reflect the correct number of episodes in the current playlist
	public int getSize() {
		return this.size;
	}

	// Displays the Episodes starting from the head and moving forward
	// Example code and its expected output:
	/*   Playlist pl = new Playlist();
	/*   pl.addLast("PlanetMoney",26.0);
	/*   pl.addLast("TodayExplained",10);
	/*   pl.addLast("RadioLab",25.5);
	/*   System.out.println(pl.displayPlaylistForward());
	/* [BEGIN] (PlanetMoney|26.0MIN) -> (TodayExplained|10.0MIN) -> (RadioLab|25.5MIN) [END]
	*/
	public String displayPlaylistForward() {
		String output = "[BEGIN] ";
		Episode current = head;
		if ( current != null ) {
			while( current.next != head ) {
				output += current + " -> ";
				current = current.next;
			}
			output += current + " [END]\n";
		}
		else {
			output += " [END]\n";
		}
		return output;
	}

	// Displays the Episodes starting from the end and moving backwards
	// Example code and its expected output:
	/*   Playlist pl = new Playlist();
	/*   pl.addLast("PlanetMoney",26.0);
	/*   pl.addLast("HowIBuiltThis",10);
	/*   pl.addLast("RadioLab",25.5);
	/*   System.out.println(pl.displayPlaylistForward());
	/* [END] (RadioLab|25.5MIN) -> (HowIBuiltThis|10.0MIN) -> (PlanetMoney|26.0MIN) [BEGIN]
	*/
	public String displayPlaylistBackward() {
		//Starts the output string with the [END] tag
		String output = "[END] ";
		//Sets current to the last episode in the playlist (episode before head)
		Episode current = head;
		//conditional that runs while the current episode is not null
		if ( current != null ) {
			current = head.prev;
			//While loop which runs through the entire playlist while the episode before current does not equal the last episode in the playlist
			while( current.prev != head.prev ) {
				output += current + " -> "; //adds the current episode and an arrow
				current = current.prev; //moves current backwards (sets it to the previous episode)
			}
			output += current + " [BEGIN]\n"; //adds the last episode to the output
		} else {
			output += " [BEGIN]\n"; //adds the [BEGIN] tag to the output string
		}
		return output; //returns output
	}

	// Add a new Episode at the beginning of the Playlist
	public void addFirst( String title, double length ) {
		if (isEmpty()) { //checks if the playlist is empty
			Episode newEp = new Episode (title, length, null, null); //creates a new episode with the appropriate title & length, sets previous and next episodes to null
			//updates connections so that the new episode is circularly linked to itself
			newEp.next = newEp; //sets the next episode of newEp to itself
			newEp.prev = newEp; //sets the previous episode of newEp to itself
			head = newEp; //sets the head to newEP
			size++; //increments size
		} else {
			Episode last = head.prev; //creates episode "last" which is equal to the last episode of the playlist
			Episode newEp = new Episode (title, length, head, head.prev); //creates a new episode that has the head as its next episode and the last episode in the playlist as its previous 
			last.next = newEp; //sets the next episode of the last episode in the playlist to newEp
			head.prev = newEp; //sets the previous episode of the old head to newEp
			head = newEp; //updates head to newEp
			size++; //increments size
		}

	}

	// Add a new Episode at the end of the Playlist
	public void addLast( String title, double length ) {
		// TODO .. //
		if (isEmpty()) { //checks if the playlist is empty
			addFirst (title, length); //just use addFirst to avoid rewriting code, as it's the same when there are no episodes in the playlist. Adds the new episode
		} else {
			Episode oldLast = head.prev; //creates episode "oldLast" which is equal to the last episode of the playlist
			Episode newLast = new Episode (title, length, head, oldLast); //creates "newLast", the new episode
			head.prev = newLast; //sets the previous episode of the head to newLast
			oldLast.next = newLast; //sets the next episode of the oldLast to the newLast
			size++; //increments size
		}
	}

	// Add a new Episode at the given index, assuming that index
	// zero corresponds to the first node
	public void add( String title, double length, int index ) {
		int count = 0; //sets count to 0, used to keep track of what index the while loop is running
		if (index < 0) { //checks if the index is less than 0
			throw new RuntimeException ("[ERROR] You cannot have a negative index"); //throws a RuntimeException with error message
		}
		if (index > this.getSize()) { //checks if the index is greater than the playlist's size
			throw new RuntimeException ("[ERROR] Index is out of bounds"); //throws a RuntimeException with error message
		}
		if (index == 0) { //checks if the index is 0
			addFirst (title, length); //adds the new episode to the playlist (used addFirst to avoid rewriting code)
		} else {
			Episode current = head; //creates episode "current" which is equal to the head
			while (count != index) { //While loop that runs as long as count does not equal the index
				Episode temp = current; //Creates episode "temp" which is equal to the current episode
				current = current.next; //sets current to the next episode
				current.prev = temp; //sets the previous episode of current to temp (which is the old current)
				count++; //increments count
			}
		Episode newEp = new Episode (title, length, current, current.prev); //creates new episode "newEp" that is connected to current and the previous of current (placed inbetween them)
		current.prev.next = newEp; //sets the next episode of the previous episode of current to newEp 
		//updates connections of adjacent episodes
		Episode next = current.next; //creates episode "next" which is equal to the next episode of current
		next.prev = newEp; //sets the previous episode of "next" to newEp
		size++; //increments size
	  }
	}

	// Delete the first Episode in the Playlist
	public Episode deleteFirst() {
		if (isEmpty()) { //checks if the playlist is empty
			throw new RuntimeException ("There are no episodes in this playlist"); //throws a RuntimeException with error message
		}
		if (size == 1) { //checks if there is only one episode in the playlist
			Episode oldFirst = head; //sets episode "oldFirst" to the head
			head = null; //sets the head to null (which deletes the existing episode)
			size--; //decrements size
			return oldFirst; //returns the oldFirst (which has been deleted)
		}
		Episode oldFirst = head; //sets episode "oldFirst" to the head
		Episode newFirst = head.next; //sets episode "newFirst" to the episode after the head
		Episode last = head.prev; //sets episode "last" to the last episode in the playlist
		newFirst.prev = last; //sets the previous episode of "newFirst" to the last episode in the playlist
		last.next = newFirst; //sets the next episode of "last" to "newFirst"
		head = newFirst; //sets the head to "newFirst"
		size--; //decrements size
		return oldFirst; //returns oldFirst
	}

	// Delete the last Episode in the Playlist
	// (There is no special "last" variable in this Playlist;
	// think of alternative ways to find that last Episode)
	public Episode deleteLast() {
		Episode last = head.prev; //creates episode "last" which is equal to the last episode of the playlist
		if (isEmpty()) { //checks if the playlist is empty
			throw new RuntimeException ("[ERROR] There are no episodes in this playlist."); //throws a RuntimeException with an error message 
		} else if (this.getSize() == 1) { //checks if the size of the playlist is 1
			last = head; //sets the last episode to the head
			head = null; //sets head to null (deleting the existing episode)
			size--; //decrements size
			return last; //returns the deleted last episode
		} else if (this.getSize() == 2) { //checks if the size of the playlist is 2
			//circularly links the head to itself as there is only one episode left in the playlist
			head.next = head; //sets the next episode of the head to the itself
			head.prev = head; //sets the previous episode of the head to itself
			size--; //decrements size
			return last; //returns the deleted last episode
		} else {
			Episode newLast = last.prev; //creates episode "newLast" which is the episode before the last episode in the playlist
			//deletes the last episode in the playlist by connecting "newLast" to the head and vice versa
			newLast.next = head; //sets the next episode of "newLast" to the head
			head.prev = newLast; //sets the previous episode of the head to "newLast"
			size--; //decrements size
			return last; //returns the deleted last episode
		}
	}

	// Remove (delete) the Episode that has the given "title"
	// You can assume there will be no duplicate titles in any Playlist
	public Episode deleteEpisode(String title) {
		Episode deleted = null; //creates new episode "deleted" which is set to null
		if (isEmpty()) { //checks if the playlist is empty
			throw new RuntimeException ("[ERROR] There are no episodes in this playlist."); //throws a RuntimeException with an error message
		} else if (this.getSize() == 1) { //checks if the size of the playlist is 1
			if (head.getTitle().equals (title)) { //checks if the head of the playlist (the only episode) is equal to the title we're searching for
				deleted = head; //sets "deleted" to the head
				head = null; //sets the head to null (deleting the episode)
				size--; //decrements size
				return deleted; //returns deleted
			}
		} else {
			Episode current = head; //creates episode "current' which is equal to the head
			Episode beforeCurrent = current; //creates episode "beforeCurrent" which is equal to current
			for (int i = 0; i < this.getSize(); i++) { //for loop that runs through the entire playlist
				if (current.getTitle().equals((title))) { //checks if the title of "current" is equal to the title we're searching for
					deleted = current; //sets "deleted" to current (which is the episode to be deleted)
					Episode afterCurrent = current.next; //creates episode "afterCurrent" which is equal to the episode after current
					//deletes the desired episode by updating connections of adjacent episodes
					beforeCurrent.next = afterCurrent; //sets the next episode of "beforeCurrent" to afterCurrent
					afterCurrent.prev = beforeCurrent; //sets the previous episode of "afterCurrent" to "beforeCurrent"
					size--; //decrements size
					return deleted; //returns the deleted episode
				} else {
					beforeCurrent = current; //sets "beforeCurrent" to current
					current = current.next; //sets "current" to the next episode
					current.prev = beforeCurrent; //sets the previous episode of "current" to beforeCurrent
				} 
			}
		} throw new RuntimeException ("[ERROR] Title not found."); //The entire playlist has been searched and the title was not found, so a RuntimeException is thrown with an error message
	}

	// Remove (delete) every m-th Episode in the user's circular Playlist,
	// until only one Episode survives. Return the survived Episode.
	public Episode deleteEveryMthEpisode(int m) {
		if (isEmpty()) { //checks if the playlist is empty
			throw new RuntimeException ("[ERROR] This playlist is empty."); //throws a RuntimeException with an error message
		} else if (m <= 0) { //checks if m is less than or equal to 0
			throw new RuntimeException ("[ERROR] Given integer is invalid."); //throws a RurntimeException with an error message
		} else {
			Episode current = head; //creates episode "current" which is equal to the head
			while (this.getSize() != 1) { //While loop that runs while the size of the playlist is greater than 1
				for (int i = 1; i < m; i++) { //for loop that runs to move current m steps
					current = current.next; //sets current to the next episode
				}
				current.prev.next = current.next; //Connects the episode before current to the episode after current
				current.next.prev = current.prev; //Connects the episode after current to the episode before current
				current = current.next; //Moves current to the next episode (to account for how the for loop starts on 1 and not 0)
				size--; //Decrements size
			}
			head = current; //sets head to current
			//updates connections so that the head's next and previous episodes are the head (circular link)
			head.next = head; //sets the next episode of the head to itself
			head.prev = head; //sets the previous episode of the head to itself
		}
		return head; //returns the head (which is now the only episode left in the playlist
	}

} // End of Playlist class
