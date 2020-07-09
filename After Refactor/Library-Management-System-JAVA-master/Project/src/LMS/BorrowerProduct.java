package LMS;


import java.util.ArrayList;

public class BorrowerProduct {
	private ArrayList<HoldRequest> onHoldBooks;

	public ArrayList<HoldRequest> getOnHoldBooks() {
		return onHoldBooks;
	}

	public void setOnHoldBooks(ArrayList<HoldRequest> onHoldBooks) {
		this.onHoldBooks = onHoldBooks;
	}

	public void removeHoldRequest(HoldRequest hr) {
		onHoldBooks.remove(hr);
	}

	public void printOnHoldBooks() {
		if (!onHoldBooks.isEmpty()) {
			System.out.println("\nOn Hold Books are: ");
			System.out.println("------------------------------------------------------------------------------");
			System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
			System.out.println("------------------------------------------------------------------------------");
			for (int i = 0; i < onHoldBooks.size(); i++) {
				System.out.print(i + "-" + "\t\t");
				onHoldBooks.get(i).getBook().printInfo();
				System.out.print("\n");
			}
		} else
			System.out.println("\nNo On Hold books.");
	}
}