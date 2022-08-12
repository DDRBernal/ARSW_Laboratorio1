package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.lang.*;
import java.util.LinkedList;

public class SearchThread extends Thread{

    private String ipaddress;
    private int rangeBegin, rangeEnd;
    private int BLACK_LIST_ALARM_COUNT = 5;

    public SearchThread(String ipaddress, int rangeBegin, int rangeEnd){
        this.ipaddress = ipaddress;
        this.rangeBegin=rangeBegin;
        this.rangeEnd=rangeEnd;
    }

    public void run(){
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();

        int ocurrencesCount=0;

        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();

        int checkedListsCount=0;

        for (int i=rangeBegin;i<rangeEnd && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)){
                System.out.println("se contro la ip");
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
        }
    }
}
