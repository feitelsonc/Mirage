
public class Author {
	
	public String AuthorId;
	public String AuthorFirstName;
	public String AuthorTitle;
	public String AuthorLastName;
	
	public Author(String authorId, String authorFirstName, String authorTitle, String authorLastName) {
		AuthorId = authorId;
		AuthorFirstName = authorFirstName;
		AuthorTitle = authorTitle;
		AuthorLastName = authorLastName;
	}
	
	public String toString() {
		return "AuthorId: " + AuthorId + "\n"
				+ "AuthorFirstName: " + AuthorFirstName + "\n"
				+ "AuthorTitle: " + AuthorTitle + "\n"
				+ "AuthorLastName: " + AuthorLastName;
	}

}
