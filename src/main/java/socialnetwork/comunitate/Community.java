package socialnetwork.comunitate;

import socialnetwork.domain.Friendship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Community {

    private List<ArrayList<Long>> adjacentList = new ArrayList<ArrayList<Long>>();
    private Map<Long,Long> visited = new HashMap<>();
    private Iterable<Friendship> listFriendship;
    private int V = 100;


    public Community(Iterable<Friendship> listFriendship) {
    //create the adjacent matrix
        this.listFriendship = listFriendship;

        for(int i = 0;i < V;i++){
            adjacentList.add(new ArrayList<Long>());
        }

        listFriendship.forEach(prietenie ->{
           adjacentList.get(prietenie.getId().getLeft().intValue()).add(prietenie.getId().getRight());
           adjacentList.get(prietenie.getId().getRight().intValue()).add(prietenie.getId().getLeft());
            resetVisited();
        });

    }

    /**
     * reset the list of visited
     */
    private void resetVisited(){
        listFriendship.forEach(elem->{
            visited.put(elem.getId().getRight(),0l);
            visited.put(elem.getId().getLeft(),0l);
        });
    }

    /**
     *
     * @param vertex
     *          vertex where its starts
     * @return
     *          number of friendship
     */
    private int  DFS(Long vertex){
        visited.put(vertex,1L);
        int numberOfFriendship = 1;
        for(Long elem : adjacentList.get(vertex.intValue())){
            if(visited.get(elem) == 0)
                numberOfFriendship += DFS(elem);
        }
        return numberOfFriendship;
    }

    /**
     * print number of communities
     */

    public void printNumberOfCommunities(){
        int numberOfComunities = 0;
    for(Long elem : visited.keySet()){
        if(visited.get(elem)== 0){
            DFS(elem);
            numberOfComunities++;
        }
    }
        System.out.println("number of comunities : " + numberOfComunities);

    }

    /**
     * print most sociable communities
     */
    public void printMostSociableCommunities(){

        int numberOfPeopleMax = 0;
        long indexOfMaxCommunity = 0;
        for(Long elem: visited.keySet()){
            if(visited.get(elem) == 0){
                int numberOfPeople = DFS(elem);
                if(numberOfPeopleMax < numberOfPeople){
                    numberOfPeopleMax = numberOfPeople;
                    indexOfMaxCommunity = elem;
                }

            }
        }

        resetVisited();
        DFS(indexOfMaxCommunity);
        System.out.print("The most sociable community has "+ numberOfPeopleMax +" users: ");
        for(Long elem : visited.keySet()){
            if(visited.get(elem) == 1){
                System.out.print(elem+" ");
            }
        }
    }
}
