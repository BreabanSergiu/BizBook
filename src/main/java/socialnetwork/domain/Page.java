package socialnetwork.domain;

public class Page {

    private int sizePage;
    private int numberPage;

    /**
     * Constructor that creates a new Page
     * @param sizePage int, representing the size of the page
     * @param numberPage int, representing the number of the current page
     */
    public Page(int sizePage, int numberPage) {
        this.sizePage = sizePage;
        this.numberPage = numberPage;
    }

    public int getSizePage() {
        return sizePage;
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }

    public int getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(int numberPage) {
        this.numberPage = numberPage;
    }

    public void nextPage(){
        setNumberPage(getNumberPage()+1);
    }

    public void previousPage(){
        setNumberPage(getNumberPage()-1);
    }
}
