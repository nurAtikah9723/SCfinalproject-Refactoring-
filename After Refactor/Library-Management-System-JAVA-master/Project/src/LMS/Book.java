
package LMS;

import java.io.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Book {
   
    private BookProduct bookProduct = new BookProduct();
	private int bookID;           // ID given by a library to a book to make it distinguishable from other books
    private boolean isIssued;        // this will be true if the book is currently issued to some borrower.
    private ArrayList<HoldRequest> holdRequests; // record of all hold request on that book
 
    static int currentIdNumber = 0;     //This will be unique for every book, since it will be incremented when everytime
                                        //when a book is created
    
  
    public Book(int id,String t, String s, String a, boolean issued)    // Parameterise cons.
    {
        currentIdNumber++;
        if(id==-1)
        {
            bookID = currentIdNumber;
        }
        else
            bookID=id;
        
        bookProduct.setTitle(t);
        bookProduct.setSubject(s);
        bookProduct.setAuthor(a);
        isIssued = issued;
        
        holdRequests = new ArrayList();
    }
    
    // adding a hold req.
    public void addHoldRequest(HoldRequest hr)
    {
        holdRequests.add(hr);
    }
    
    // removing a hold req.
    public void removeHoldRequest()
    {
        if(!holdRequests.isEmpty())
        {
            holdRequests.remove(0);
        }
    }
    
    // printing all hold req on a book.
    public void printHoldRequests()
    {
        if (!holdRequests.isEmpty())
        { 
            System.out.println("\nHold Requests are: ");
            
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");            
            System.out.println("No.\t\tBook's Title\t\t\tBorrower's Name\t\t\tRequest Date");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
            
            for (int i = 0; i < holdRequests.size(); i++)
            {                      
                System.out.print(i + "-" + "\t\t");
                holdRequests.get(i).print();
            }
        }
        else
            System.out.println("\nNo Hold Requests.");                                
    }
    
    // printing book's Info
    public void printInfo()
    {
        bookProduct.printInfo();
    }
    
    // changign Info of a Book
    public void changeBookInfo() throws IOException
    {
        bookProduct.changeBookInfo();
        
    }
    
    /*------------Getter FUNCs.---------*/
    
    public String getTitle()
    {
        return bookProduct.getTitle();
    }

    public String getSubject()
    {
        return bookProduct.getSubject();
    }

    public String getAuthor()
    {
        return bookProduct.getAuthor();
    }
    
    public boolean getIssuedStatus()
    {
        return isIssued;
    }
    
    public void setIssuedStatus(boolean s)
    {
        isIssued = s;
    }
    
     public int getID()
    {
        return bookID;
    }
     
     public ArrayList<HoldRequest> getHoldRequests()
    {
        return holdRequests;
    }
    /*-----------------------------------*/
     
    // Setter Static Func.
    public static void setIDCount(int n)
    {
        currentIdNumber = n;
    }
    

    
    
    //-------------------------------------------------------------------//
    
    // Placing book on Hold
    public void placeBookOnHold(Borrower bor)
    {
        HoldRequest hr = new HoldRequest(bor,this, new Date());
        
        addHoldRequest(hr);        //Add this hold request to holdRequests queue of this book
        bor.addHoldRequest(hr);      //Add this hold request to that particular borrower's class as well
        
        System.out.println("\nThe book " + bookProduct.getTitle() + " has been successfully placed on hold by borrower " + bor.getName() + ".\n");
    }
    
    


   // Request for Holding a Book
    public void makeHoldRequest(Borrower borrower)
    {
        boolean makeRequest = true;

        //If that borrower has already borrowed that particular book. Then he isn't allowed to make request for that book. He will have to renew the issued book in order to extend the return deadline.
        for(int i=0;i<borrower.getBorrowedBooks().size();i++)
        {
            if(borrower.getBorrowedBooks().get(i).getBook()==this)
            {
                System.out.println("\n" + "You have already borrowed " + bookProduct.getTitle());
                return;                
            }
        }
        
        
        //If that borrower has already requested for that particular book. Then he isn't allowed to make the same request again.
        for (int i = 0; i < holdRequests.size(); i++)
        {
            makeRequest = makeRequest(borrower, makeRequest, i);
			if ((holdRequests.get(i).getBorrower() == borrower))
            {
                break;
            }
        }

        holdRequests(borrower, makeRequest);
		if (makeRequest)
        {
        }
        else
            System.out.println("\nYou already have one hold request for this book.\n");
    }

	private void holdRequests(Borrower borrower, boolean makeRequest) {
		if (makeRequest) {
			placeBookOnHold(borrower);
		}
	}

	private boolean makeRequest(Borrower borrower, boolean makeRequest, int i) {
		if ((holdRequests.get(i).getBorrower() == borrower)) {
			makeRequest = false;
		}
		return makeRequest;
	}

    
    // Gertting Info of a Hold Request
    public void serviceHoldRequest(HoldRequest hr)
    {
        removeHoldRequest();
        hr.getBorrower().removeHoldRequest(hr);
    }

    
        
    // Issuing a Book
    public void issueBook(Borrower borrower, Staff staff)
    {        
        //First deleting the expired hold requests
        Date today = new Date();        
        
        ArrayList<HoldRequest> hRequests = holdRequests;
        
        for (int i = 0; i < hRequests.size(); i++)
        {
            HoldRequest hr = hRequests.get(i);            
            
            //Remove that hold request which has expired
            long days =  ChronoUnit.DAYS.between(today.toInstant(), hr.getRequestDate().toInstant());        
            days = 0-days;
            
            if(days>Library.getInstance().getHoldRequestExpiry())
            {
                removeHoldRequest();
                hr.getBorrower().removeHoldRequest(hr);
            } 
        }
               
        if (isIssued)
        {
            System.out.println("\nThe book " + bookProduct.getTitle() + " is already issued.");
            System.out.println("Would you like to place the book on hold? (y/n)");
             
            Scanner sc = new Scanner(System.in);
            String choice = sc.next();
            
            if (choice.equals("y"))
            {                
                makeHoldRequest(borrower);
            }
        }
        
        else
        {               
            if (!holdRequests.isEmpty())
            {
                boolean hasRequest = false;
                
                for (int i = 0; i < holdRequests.size() && !hasRequest;i++)
                {
                    hasRequest = hasRequest(borrower, hRequests, hasRequest, i);
                        
                }
                
                if (hasRequest)
                {
                    //If this particular borrower has the earliest request for this book
                    if (holdRequests.get(0).getBorrower() == borrower)
                        serviceHoldRequest(holdRequests.get(0));       

                    else
                    {
                        System.out.println("\nSorry some other users have requested for this book earlier than you. So you have to wait until their hold requests are processed.");
                        return;
                    }
                }
                else
                {
                    System.out.println("\nSome users have already placed this book on request and you haven't, so the book can't be issued to you.");
                    
                    System.out.println("Would you like to place the book on hold? (y/n)");

                    Scanner sc = new Scanner(System.in);
                    String choice = sc.next();
                    
                    if (choice.equals("y"))
                    {
                        makeHoldRequest(borrower); 
                    }                    
                    
                    return;
                }               
            }
                        
            //If there are no hold requests for this book, then simply issue the book.            
            setIssuedStatus(true);
            
            Loan iHistory = new Loan(borrower,this,staff,null,new Date(),null,false);
            
            Library.getInstance().addLoan(iHistory);
            borrower.addBorrowedBook(iHistory);
                                    
            System.out.println("\nThe book " + bookProduct.getTitle() + " is successfully issued to " + borrower.getName() + ".");
            System.out.println("\nIssued by: " + staff.getName());            
        }
    }

	private boolean hasRequest(Borrower borrower, ArrayList<HoldRequest> hRequests, boolean hasRequest, int i) {
		if (holdRequests.get(i).getBorrower() == borrower)
			hasRequest = true;
		return hasRequest;
	}
        
        
    // Returning a Book
    public void returnBook(Borrower borrower, Loan l, Staff staff)
    {
        l.returnBookcs(borrower, staff);            
    }
    
}   // Book Class Closed